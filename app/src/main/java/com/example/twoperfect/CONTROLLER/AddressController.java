package com.example.twoperfect.CONTROLLER;

import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class AddressController {
    public static Address setAddressToSQLiteByID(int id) throws IOException {
        Address address;
        HttpResponse response = Request.response("address/"+id);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        address = objectMapper.readValue(jsonContent, Address.class);

        return address;
    }
}
