package com.example.sustainabilityapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Iterator;


public class JSONStringParser  {

    private final static String CLIENT = "CLIENT";

    private String jsonString;

     public JSONStringParser(String jsonString) {
        this.jsonString = jsonString;
    }

    public void toPing() {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("route0");
            Iterator<String> keys = jsonObject1.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if(jsonObject1.get(key) instanceof  JSONObject) {
                    if(!jsonObject1.getJSONObject(key).get("aqi").equals("?")) {
                        Log.i(CLIENT, "lat: " + jsonObject1.getJSONObject(key).get("lat") + "\n"
                                + "lon: " + jsonObject1.getJSONObject(key).get("lon") + "\n"
                                + "aqi: " + jsonObject1.getJSONObject(key).get("aqi") + "\n\n");
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
