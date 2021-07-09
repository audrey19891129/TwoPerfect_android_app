package com.example.twoperfect.CONTROLLER;


import android.content.Context;
import android.util.Log;

import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import kotlin.text.Charsets;

public class InterventionController {
    public static ArrayList<Intervention> getInterventions(int id) throws IOException, JSONException {
        ArrayList<Intervention> list = new ArrayList<>();
        HttpResponse response = Request.response("intervention/byEmployee/"+id);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();

        JSONTokener jsonToken = new JSONTokener(jsonContent);
        JSONArray jsonList = new JSONArray(jsonToken);

        for(int i = 0; i < jsonList.length(); i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            list = objectMapper.readValue(jsonContent, new TypeReference<ArrayList<Intervention>>() {});
        }
        return list;
    }

    public static ArrayList<Intervention> getNewInterventions(Context context) throws IOException, JSONException {
        InterventionDB idb = new InterventionDB(context);
        idb.openForRead();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        ArrayList<Intervention> list = idb.getInterventionsByDate(today);
        idb.close();
        ArrayList<Intervention> newInterventions = new ArrayList<Intervention>();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        HttpResponse response = Request.getFromArray(Technician.getTechnician().getId(), json);
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
        String jsonContent = br.readLine();
        JSONTokener jsonToken = new JSONTokener(jsonContent);
        JSONArray jsonList = new JSONArray(jsonToken);
        if(jsonList != null) {
            for (int i = 0; i < jsonList.length(); i++) {
                ObjectMapper objectMapper = new ObjectMapper();
                newInterventions = objectMapper.readValue(jsonContent, new TypeReference<ArrayList<Intervention>>() {
                });
            }
        }
        return newInterventions;
    }
}
