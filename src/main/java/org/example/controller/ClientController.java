package org.example.controller;

import org.example.entity.Client;
import org.example.entity.Document;
import org.example.service.BureaucraticServer;
import org.example.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private BureaucraticServer server;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

 //   @PatchMapping
//    public Client updateClient(@RequestBody Client client, @RequestBody Document document) {
//        //server.requestDocument(client, document);
//        //return clientService.updateClient(client);
//    }
}
