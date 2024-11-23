package org.example.controller;

import org.example.dto.ClientCreateDto;
import org.example.dto.ClientDto;
import org.example.dto.DocumentCreateDto;
import org.example.dto.DocumentDto;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.mapper.ClientMapper;
import org.example.service.BureaucraticServer;
import org.example.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {

        List<Client> clients = clientService.findAllClients();

        List<ClientDto> clientDtos = clients.stream()
                .map(clientMapper::mapClientToClientDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clientDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {

        Client client = clientService.findById(id).orElse(null);

        if (client != null) {
            return ResponseEntity.ok(clientMapper.mapClientToClientDto(client));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping
    public ClientDto createClient(@RequestBody ClientCreateDto clientCreateDto) {
        Client client = clientMapper.mapClientCreateDtoToClient(clientCreateDto);
        Client savedClient = clientService.saveClient(client, clientCreateDto.getOwnedDocumentsIds(), clientCreateDto.getRequestedDocumentIds());
        return clientMapper.mapClientToClientDto(savedClient);
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


