package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
    private AutoCompleteTextView citydropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        cities=new ArrayList<String>();
        initialise_data();
    }

    private void initialise_data() {
        //Set up a progress dialog until the response arrives
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Create your own plan");
        progressDialog.show();

        //  for stop dialog dismiss on click hardware back button
        progressDialog.setCancelable(false);
        // for stop dialog dismiss on click screen any where
        progressDialog.setCanceledOnTouchOutside(false);

        // Creating a request queue and adding json request to the given URL
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
                    progressDialog.dismiss();
                    setFields();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FetchCity","Error: "+String.valueOf(error));
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void setFields() {
        citydropdown=findViewById(R.id.cityListDropdown);
        Log.d("Cities Retrieved",String.valueOf(cities));

        // Set autocomplete values to text view using adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line,cities);
        citydropdown.setAdapter(arrayAdapter);
    }
}