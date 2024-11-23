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
import java.util.Optional;
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
    public ResponseEntity<List<ClientDto>> getAllClients() {

        List<Client> clients = clientService.findAllClients();

        List<ClientDto> clientDtos = clients.stream()
                .map(clientMapper::mapClientToClientDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clientDtos);
    }

    @PostMapping
    public ClientDto createClient(@RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientMapper.mapClientCreateDtoToClient(clientCreateDto);
        Client savedClient = clientService.saveClient(client, clientCreateDto.getOwnedDocumentsIds(), clientCreateDto.getRequestedDocumentIds());
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
            List<String> documentNames = client.getRequestedDocument().stream()
                    .map(Document::getName)
                    .collect(Collectors.toList());
            response.put("requestedDocuments", documentNames);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            //return ResponseEntity.ok(clientMapper.mapClientToClientDto(client));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientCreateDto> updateClientName(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Client> updatedClient = clientService.updateClientName(id, updates);

        if (updatedClient.isPresent()) {
            return ResponseEntity.ok(clientMapper.mapClientToClientCreateDto(updatedClient.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    @PatchMapping("/{id}/documents")
    public ResponseEntity<ClientDto> updateClientDocuments(@PathVariable Long id, @RequestBody Map<String, List<Long>> updates) {
        Optional<Client> updatedClient = clientService.partialUpdateDocuments(id, updates);

        if (updatedClient.isPresent()) {
            return ResponseEntity.ok(clientMapper.mapClientToClientDto(updatedClient.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}


