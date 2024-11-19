package org.example.controller;

import org.example.dto.ClientCreateDto;
import org.example.dto.ClientDto;
import org.example.entity.Client;
import org.example.entity.Document;
import org.example.mapper.ClientMapper;
import org.example.service.BureaucraticServer;
import org.example.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientMapper clientMapper;

    private BureaucraticServer server;



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

 //   @PatchMapping
//    public Client updateClient(@RequestBody Client client, @RequestBody Document document) {
//        //server.requestDocument(client, document);
//        //return clientService.updateClient(client);
//    }
}
