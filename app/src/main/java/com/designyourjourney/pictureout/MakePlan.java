package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MakePlan extends AppCompatActivity {
    private ArrayList<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        cities=new ArrayList<String>();

        // Get all cities name from some API
        String url="https://countriesnow.space/api/v0.1/countries";
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data= null;
                JSONObject countrydata;
                JSONArray citiesdata;
                String country,city;
                try {
                    //Fetch the corresponding data from API
                    data = response.getJSONArray("data");
                    for (int i=0;i<data.length();i++) {
                        countrydata=data.getJSONObject(i);
                        country=countrydata.getString("country");
                        citiesdata=countrydata.getJSONArray("cities");
                        for (int j=0;j<citiesdata.length();j++) {
                            city= (String) citiesdata.get(j);
                            //Add it to array list
                            cities.add(city+","+country);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("A",String.valueOf(cities));
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FetchCity","Some error occured");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}