package org.example.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.commands.BaseCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.example.config.RabbitMQConfig.CLIENTS_REQUESTS_QUEUE;

@Service
public class CommandProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CommandProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendCommand(BaseCommand command) {
        Map<String, Object> message = new HashMap<>();
        message.put("command_type", command.getClass().getSimpleName());
        message.put("data", command);

        try {
            String messageJson = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(CLIENTS_REQUESTS_QUEUE, messageJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}