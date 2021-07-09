package com.example.twoperfect.CONTROLLER;
import android.util.Log;

import com.example.twoperfect.MODEL.Coordinates;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class Request {
    private static int timeout = 15; // <--- 15 seconds tu peut le modifier pour tester

    private static String BASE_URL =  "https://twoperfect.herokuapp.com/twoperfect/services/"; //"http://192.168.0.12:8080/twoperfect/services/";

    public static HttpResponse response(String service) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
        .setConnectTimeout(timeout * 1000)
        .setConnectionRequestTimeout(timeout * 1000)
        .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        //

        HttpGet request = new HttpGet(BASE_URL + service);
        request.addHeader("accept", "application/json");
        HttpResponse response = client.execute(request);
        return response;
    }

    public static HttpResponse getFromArray(int id, String array) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = new HttpPost(BASE_URL + "NEW_interventions/" + id);
        StringEntity entity = new StringEntity(array, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.addHeader("accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPost);
        return response;
    }

    public static HttpResponse getLogIn(String service, String username, String password) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet request = new HttpGet(BASE_URL + service + "/" + username + "/" + password);
        request.addHeader("accept", "application/json");
        HttpResponse response = client.execute(request);
        return response;
    }


    public static int setCoordinates(String coordinates) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPut httpPut = new HttpPut (BASE_URL + "coordinates");
        StringEntity entity = new StringEntity(coordinates);
        httpPut.setEntity(entity);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPut);
        System.out.println(response);
        return response.getStatusLine().getStatusCode();
    }

    public static int PUT(String service, String object) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPut httpPut = new HttpPut (BASE_URL + service);
        StringEntity entity = new StringEntity(object, "UTF-8");
        httpPut.setEntity(entity);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPut);
        System.out.println(response);
        return response.getStatusLine().getStatusCode();
    }

    public static int POST(String service, String object) throws IOException {
        // ResquestConfig pour Set un TimeOut et il s'ajoute a ton HttpClient
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = new HttpPost(BASE_URL + service);
        StringEntity entity = new StringEntity(object, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httpPost);
        System.out.println(response);
        return response.getStatusLine().getStatusCode();
    }

    public static JSONObject GET_JSONObject(String service){
        SyncHttpClient clients = new SyncHttpClient();
        JSONObject jsonObject[] = new JSONObject[1];
        clients.get(BASE_URL + service, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                jsonObject[0] = response;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                jsonObject[0] = errorResponse;
            }
        });
        return jsonObject[0];
    }

    public static JSONArray GET_JSONArray(String service){
        SyncHttpClient clients = new SyncHttpClient();
        final JSONArray[] jsonElements = {new JSONArray()};
        clients.get(BASE_URL + service, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                jsonElements[0] = response;
            }
        });
        return jsonElements[0];
    }


}
