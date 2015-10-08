package com.psc.vote.client.service;

import com.psc.vote.client.domain.Client;
import com.psc.vote.client.domain.ClientRegret;
import com.psc.vote.client.manager.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientService {

    @Autowired
    ClientManager clientManager;

    public Client getClient(Client client) {
        return clientManager.getClient(client);
    }

    public void registerClient(Client client) throws Exception {
        clientManager.registerClient(client);
    }

    public void updatePassword(Client client) throws Exception {
        clientManager.updatePassword(client);
    }

    public void updatePasswordCode(Client client) throws Exception {
        clientManager.updatePasswordCode(client);
    }

    public void updateClient(Client client) throws Exception {
        clientManager.updateClient(client);
    }

    public void insertRegret(ClientRegret regret) throws Exception {
        clientManager.insertRegret(regret);
    }
}