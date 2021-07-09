package com.example.twoperfect.CONTROLLER;

import com.example.twoperfect.MODEL.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class ClientController {
    public static Client setClientToSQLiteByID(int id) throws IOException {
        Client client;
        HttpResponse response = Request.response("ClientForTechnician/"+id);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        client = objectMapper.readValue(jsonContent, Client.class);

        return client;
    }
}
