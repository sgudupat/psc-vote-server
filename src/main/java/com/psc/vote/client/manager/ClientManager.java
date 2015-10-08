package com.psc.vote.client.manager;

import com.psc.vote.client.dao.ClientDao;
import com.psc.vote.client.dao.ClientRegretDao;
import com.psc.vote.client.domain.Client;
import com.psc.vote.client.domain.ClientRegret;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientManager {

    @Autowired
    ClientDao clientDao;

    @Autowired
    ClientRegretDao clientRegretDao;

    public Client getClient(Client client) {
        return clientDao.getClient(client);
    }

    public void registerClient(Client client) throws Exception {
        clientDao.registerClient(client);
    }

    public void updatePassword(Client client) throws Exception {
        clientDao.updatePassword(client);
    }

    public void updatePasswordCode(Client client) throws Exception {
        clientDao.updatePasswordCode(client);
    }

    public void updateClient(Client client) throws Exception {
        clientDao.updateClient(client);
    }

    public void insertRegret(ClientRegret regret) throws Exception {
        clientRegretDao.insertRegret(regret);
    }
}