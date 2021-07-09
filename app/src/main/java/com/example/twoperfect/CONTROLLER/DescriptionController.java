package com.example.twoperfect.CONTROLLER;

import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Description;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class DescriptionController {
    public static Description setDescriptionToSQLiteByID(int id) throws IOException {
        Description description;
        HttpResponse response = Request.response("description/"+id);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        description = objectMapper.readValue(jsonContent, Description.class);

        return description;
    }
}
