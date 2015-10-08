package com.psc.vote.common.util;

import com.psc.vote.client.domain.Client;
import org.json.simple.JSONObject;

public class ClientUtil {

    public static String toJSONClient(Client client) {
        JSONObject clientJSON = new JSONObject();
        clientJSON.put("client_id", client.getClientId());
        clientJSON.put("client_name", client.getClientName());
        clientJSON.put("email_address", client.getEmailAddress());
        clientJSON.put("creation_date", client.getCreationDate());
        clientJSON.put("website_url", client.getWebsiteURL());
        clientJSON.put("about", client.getAbout());
        clientJSON.put("country", client.getOperatingCountry());
        System.out.println("clientJSON:" + clientJSON.toJSONString());
        return clientJSON.toJSONString();
    }
}