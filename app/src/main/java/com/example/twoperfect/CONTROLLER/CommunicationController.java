package com.example.twoperfect.CONTROLLER;

import android.content.Context;
import android.util.Log;

import com.example.twoperfect.MODEL.Communication;
import com.example.twoperfect.database.communication.CommunicationDB;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class CommunicationController {
    public static void setCommunicationToSQLite(Communication communication, Context context) {
        CommunicationDB communicationDB = new CommunicationDB(context);
        communicationDB.openForWrite();
        communicationDB.insertCommunication(communication);
        communicationDB.close();
    }

    public static void setCommunicationToMySQL(Communication communication) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCom = objectMapper.writeValueAsString(communication);
        int response = Request.POST("communication", jsonCom);
        Log.e("RESPONSE", String.valueOf(response));
        Log.e("RESPONSE", String.valueOf(jsonCom));
    }
}
