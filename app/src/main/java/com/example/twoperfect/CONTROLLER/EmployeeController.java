package com.example.twoperfect.CONTROLLER;
import android.util.Log;

import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Technician;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import kotlin.text.Charsets;

public class EmployeeController {


    public static void getEmployees() throws IOException, JSONException {

        HttpResponse response = Request.response("employee");
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        JSONTokener jsonToken = new JSONTokener(jsonContent);
        JSONArray jsonList = new JSONArray(jsonToken);

        for(int i = 0; i < jsonList.length(); i++) {
            JSONObject obj = jsonList.getJSONObject(i);
            System.out.println(obj);
            System.out.println("\n");
        }
    }

    public static boolean getLogIn(String username, String password) throws IOException, JSONException {
        boolean bool = false;
        HttpResponse response = Request.getLogIn("employee", username , password);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        JSONObject obj = new JSONObject(jsonContent);
        if(!(obj.has("ERROR"))){
            if(obj.getString("title").equalsIgnoreCase("technicien")) {
                bool = true;
                ObjectMapper objectMapper = new ObjectMapper();
                Employee emp = objectMapper.readValue(jsonContent, Employee.class);
                Technician.setTechnician(emp);
                Log.e("TECHNICIAN", Technician.getTechnician().toString());
            }
        }

        return bool;
    }

}
