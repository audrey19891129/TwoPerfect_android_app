package com.example.twoperfect;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.twoperfect.CONTROLLER.EmployeeController;
import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.database.communication.CommunicationDB;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.twoperfect", appContext.getPackageName());
    }

    @Test
    public void loginTechnician_1() throws IOException, JSONException {
        assertEquals(true, EmployeeController.getLogIn("user1", "pass1"));
    }

    @Test
    public void loginTechnician_2() throws IOException, JSONException {
        assertEquals(true, EmployeeController.getLogIn("user2", "pass2"));
    }

    @Test
    public void loginTechnician_3() throws IOException, JSONException {
        assertEquals(true, EmployeeController.getLogIn("user3", "pass3"));
    }

    @Test
    public void loginTechnician_4() throws IOException, JSONException {
        assertEquals(true, EmployeeController.getLogIn("user4", "pass4"));
    }

    @Test
    public void loginTechnician_5() throws IOException, JSONException {
        assertEquals(true, EmployeeController.getLogIn("user5", "pass5"));
    }


    String service = "employee";
    @Test
    public void resquestHttpGetServerStatusCode200() throws IOException {
        HttpResponse serverResponse = Request.response(service);
        Log.e("Response", serverResponse.toString());
        assertEquals(200, serverResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void TestJsonObject(){
        //--------------------------------------------------------
        // TEST [ PASSED ] => THE RECEIVED JSONObject IS EMPTY
        //--------------------------------------------------------
        // TEST [ FAILED ] => THE RECEIVED JSONObject is NOT NULL
        //--------------------------------------------------------
        JSONObject obj = new JSONObject();
        assertEquals(obj, Request.GET_JSONObject(service));
    }

    @Test
    public void TestJsonArray(){
        //--------------------------------------------------------
        // TEST [ PASSED ] => THE RECEIVED JSONArray IS EMPTY
        //--------------------------------------------------------
        // TEST [ FAILED ] => THE RECEIVED JSONObject is NOT NULL
        //--------------------------------------------------------
        JSONArray array = new JSONArray();
        assertEquals(array, Request.GET_JSONArray(service));
    }


}