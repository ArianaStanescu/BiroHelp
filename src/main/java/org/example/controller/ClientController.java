package org.example.controller;

import org.example.commands.CreateUserRequestCommand;
import org.example.dto.ClientCreateDto;
import org.example.dto.ClientDto;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.mapper.ClientMapper;
import org.example.producer.CommandProducer;
import org.example.service.BureaucraticServer;
import org.example.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientMapper clientMapper;

    private BureaucraticServer server;
    @Autowired
    private CommandProducer commandProducer;

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        Client client = clientService.getById(id);
        if (client != null) {
            ClientDto clientDto = clientMapper.mapClientToClientDto(client);
            return ResponseEntity.ok(clientDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public List<Client> getClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public ClientDto createClient(@RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientMapper.mapClientCreateDtoToClient(clientCreateDto);
        Client savedClient = clientService.saveClient(client,clientCreateDto.getOwnedDocumentsIds(), clientCreateDto.getRequestedDocumentIds());
        return clientMapper.mapClientToClientDto(savedClient);
    }

    @PostMapping("/{userId}/request-documents")
    public ResponseEntity<?> requestDocuments(@PathVariable Long userId) {
        Client client = clientService.getById(userId);
        if (client != null) {
            CreateUserRequestCommand command = clientMapper.mapClientToCreateUserRequestCommand(client);
            commandProducer.sendCommand(command);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "CreateUserRequestCommand sent to queue");
            response.put("name", command.getName());
            List<String> documentNames = command.getRequestedDocuments().stream()
                    .map(Document::getName)
                    .collect(Collectors.toList());
            response.put("requestedDocuments", documentNames);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            //return ResponseEntity.ok(clientMapper.mapClientToClientDto(client));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //   @PatchMapping
//    public Client updateClient(@RequestBody Client client, @RequestBody Document document) {
//        //server.requestDocument(client, document);
//        //return clientService.updateClient(client);
//    }
}
