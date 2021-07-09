package com.example.twoperfect.CONTROLLER;

import com.example.twoperfect.MODEL.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class TicketController {

    public static Ticket setTicketToSQLiteByID(int id) throws IOException {
        Ticket ticket;
        HttpResponse response = Request.response("ticket/"+id);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        ticket = objectMapper.readValue(jsonContent, Ticket.class);
        return ticket;
    }
}
