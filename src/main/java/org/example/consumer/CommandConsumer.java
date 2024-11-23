package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.commands.CreateUserRequestCommand;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.service.ClientService;
import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.config.RabbitMQConfig.CLIENTS_REQUESTS_QUEUE;

@Service
public class CommandConsumer {

    private final ObjectMapper objectMapper;
    private final ClientService clientService;

    public CommandConsumer(ObjectMapper objectMapper, ClientService clientService) {
        this.objectMapper = objectMapper;
        this.clientService = clientService;
    }

    @RabbitListener(queues = CLIENTS_REQUESTS_QUEUE)
    public void receiveMessage(String messageJson) {
        try {
            Map<String, Object> message = objectMapper.readValue(messageJson, Map.class);
            String commandType = (String) message.get("command_type");
            Object data = message.get("data");
            System.out.println("data " + data);

            if ("CreateUserRequestCommand".equals(commandType)) {
                CreateUserRequestCommand command = objectMapper.convertValue(data, CreateUserRequestCommand.class);
                createUserRequestCommand(command);
//                System.out.println("inside CreateUserRequestCommand if");

            } else {
                System.out.println("Unknown command type: " + commandType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUserRequestCommand(CreateUserRequestCommand command) {

        Long clientId = command.getId();
        Client client = clientService.getById(clientId);
        if (client != null) {
            System.out.println("User with name " + command.getName() + ": requested documents: " + command.getRequestedDocuments() + ", owned documents: " + command.getOwnedDocuments());
            List<String> ownedDocumentNames = command.getOwnedDocuments().stream()
                    .map(Document::getName)
                    .collect(Collectors.toList());
            System.out.println("Owned documents: " + ownedDocumentNames);
            List<String> requestedDocumentNames = command.getRequestedDocuments().stream()
                    .map(Document::getName)
                    .collect(Collectors.toList());
            System.out.println("Requested documents: " + requestedDocumentNames);
            new Thread(client).start();
        } else {
            System.out.println("Client not found with ID: " + clientId);
        }


    }
}