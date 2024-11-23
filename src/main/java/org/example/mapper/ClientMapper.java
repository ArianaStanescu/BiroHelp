package org.example.mapper;

import org.example.commands.CreateUserRequestCommand;
import org.example.dto.ClientCreateDto;
import org.example.dto.ClientDto;
import org.example.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClientMapper {

    @Autowired
    private DocumentMapper documentMapper;

    public ClientDto mapClientToClientDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setRequestedDocuments(client.getRequestedDocument().stream().map(documentMapper::mapDocumentToDocumentCreateDto).collect(Collectors.toList()));
        clientDto.setOwnedDocuments(client.getOwnedDocuments().stream().map(documentMapper::mapDocumentToDocumentCreateDto).collect(Collectors.toList()));
        return clientDto;
    }

    public Client mapClientCreateDtoToClient(ClientCreateDto clientCreateDto) {
        Client client = new Client();
        client.setName(clientCreateDto.getName());
        return client;
    }

    public ClientCreateDto mapClientToClientCreateDto(Client client) {
        ClientCreateDto clientCreateDto = new ClientCreateDto();
        clientCreateDto.setName(client.getName());
        return clientCreateDto;
    }

    public CreateUserRequestCommand mapClientToCreateUserRequestCommand(Client client) {
        CreateUserRequestCommand createUserRequestCommand = new CreateUserRequestCommand();
        createUserRequestCommand.setId(client.getId());
        createUserRequestCommand.setName(client.getName());
        createUserRequestCommand.setRequestedDocuments(client.getRequestedDocument());
        createUserRequestCommand.setOwnedDocuments(client.getOwnedDocuments());
        return createUserRequestCommand;
    }
}
