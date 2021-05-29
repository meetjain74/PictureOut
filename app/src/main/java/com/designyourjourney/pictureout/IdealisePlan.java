package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


public class IdealisePlan extends AppCompatActivity {
    private String planName;
    private ArrayList<City> citiesSelected = new ArrayList<>();
    private String startDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idealise_plan);
        getData(); //To get data through intent

        Log.d("Data from intent : ", planName + " " + citiesSelected + " " + startDate + " " + endDate);
        for (int i=0;i<citiesSelected.size();i++) {
            Log.d("City : ", citiesSelected.get(i).getCityDetails());
        }
    }

    private void getData() {
        Intent intent = getIntent();
        planName = intent.getStringExtra("com.designyourjourney.pictureout.PLAN_NAME");
        citiesSelected= (ArrayList<City>) intent.getSerializableExtra("com.designyourjourney.pictureout.CITIES_SELECTED");
        startDate = intent.getStringExtra("com.designyourjourney.pictureout.START_DATE");
        endDate = intent.getStringExtra("com.designyourjourney.pictureout.END_DATE");
    }
}