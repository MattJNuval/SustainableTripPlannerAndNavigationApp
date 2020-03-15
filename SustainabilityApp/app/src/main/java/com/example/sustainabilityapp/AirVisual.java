package com.example.sustainabilityapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.Unirest;

public class AirVisual extends AsyncTask {


    private static final String AIR = "AIR";

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            get("http://api.airvisual.com/v2/countries");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void get(String urlString) throws JSONException {
        HttpResponse<JsonNode> responseJson = Unirest.get(urlString).header("accept", "application/json").queryString("key","d7664ac9-d9fb-4ed4-b6f6-2e8feac28693").asJson();
        JSONArray responseArray = responseJson.getBody().getArray();
        JSONObject jsonObject = null;
        for(int i = 0; i < responseArray.length(); i++) {
            jsonObject = (JSONObject) responseArray.get(i);
            Log.d(AIR, jsonObject+"\n");
        }

    }
}
