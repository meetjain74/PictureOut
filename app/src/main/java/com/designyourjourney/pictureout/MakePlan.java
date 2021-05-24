package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MakePlan extends AppCompatActivity {
    private ArrayList<String> cities;
    private AutoCompleteTextView citydropdown;
    private TextView start_date;
    private DatePicker datePicker_start;
    private TextView end_date;
    private  DatePicker datePicker_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);
        cities = new ArrayList<>();
        initialise_data();

        start_date = findViewById(R.id.startDate);
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });

        end_date=findViewById(R.id.endDate);
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate();
            }
        });
    }

    private void initialise_data() {
        //Set up a progress dialog until the response arrives
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Create your own plan");
        progressDialog.show();

        //  for stop dialog dismiss on click hardware back button
        progressDialog.setCancelable(false);
        // for stop dialog dismiss on click screen any where
        progressDialog.setCanceledOnTouchOutside(false);

        // Creating a request queue and adding json request to the given URL
        String url = "https://countriesnow.space/api/v0.1/countries";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray data = null;
                        JSONObject countrydata;
                        JSONArray citiesdata;
                        String country, city;
                        try {
                            //Fetch the corresponding data from API
                            data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                countrydata = data.getJSONObject(i);
                                country = countrydata.getString("country");
                                citiesdata = countrydata.getJSONArray("cities");
                                for (int j = 0; j < citiesdata.length(); j++) {
                                    city = (String) citiesdata.get(j);
                                    //Add it to array list
                                    cities.add(city + "," + country);
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
                        Log.d("FetchCity", "Error: " + error);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void setFields() {
        citydropdown = findViewById(R.id.cityListDropdown);
        Log.d("Cities Retrieved", String.valueOf(cities));

        // Set autocomplete values to text view using adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        citydropdown.setAdapter(arrayAdapter);
    }

    private void setStartDate() {
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog start=new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker_start=new DatePicker(getApplicationContext());
                        datePicker_start.init(year,month,dayOfMonth,null);
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month,dayOfMonth-1);
                        int week_number=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        String week=getWeek(week_number);
                        String date=week+" "+dayOfMonth+"/"+month+"/"+(year%100);
                        start_date.setText(date);
                    }
                },
                year,month,day
        );
        //Disable date before today
        start.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set date to previously selected date
        if (datePicker_start != null) {
            start.updateDate(datePicker_start.getYear(),datePicker_start.getMonth(),datePicker_start.getDayOfMonth());
        }
        start.show();
    }

    private void setEndDate() {
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog end=new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker_end=new DatePicker(getApplicationContext());
                        datePicker_end.init(year,month,dayOfMonth,null);
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month,dayOfMonth-1);
                        int week_number=gregorianCalendar.get(Calendar.DAY_OF_WEEK);
                        String week=getWeek(week_number);
                        String date=week+" "+dayOfMonth+"/"+month+"/"+(year%100);
                        end_date.setText(date);
                    }
                },
                year,month,day
        );
        //Disable date before today
        end.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set date to previously selected date
        if (datePicker_end != null) {
            end.updateDate(datePicker_end.getYear(),datePicker_end.getMonth(),datePicker_end.getDayOfMonth());
        }
        end.show();
    }

    private String getWeek(int n) {
        switch(n) {
            case 1: return "Mon"; //Monday
            case 2: return "Tue"; //Tuesday
            case 3: return "Wed"; //Wednesday
            case 4: return "Thu"; //Thursday
            case 5: return "Fri"; //Friday
            case 6: return "Sat"; //Saturday
            case 7: return "Sun"; //Sunday
            default: return "Error"; //Invalid day
        }
    }
}