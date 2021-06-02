package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class IdealisePlan extends AppCompatActivity implements OnMapReadyCallback {
    private String planName;
    private ArrayList<City> citiesSelected = new ArrayList<>();
    private String startDate;
    private String endDate;
    private City currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idealise_plan);
        getData(); //To get data through intent

        // Show selected cities on map
        showOnMap();

        Log.d("Data from intent : ", planName + " " + citiesSelected + " " + startDate + " " + endDate);
        for (int i = 0; i < citiesSelected.size(); i++) {
            Log.d("City : ", citiesSelected.get(i).getCityDetails());
        }
    }

    private void getData() {
        Intent intent = getIntent();
        planName = intent.getStringExtra("com.designyourjourney.pictureout.PLAN_NAME");
        citiesSelected = (ArrayList<City>) intent.getSerializableExtra("com.designyourjourney.pictureout.CITIES_SELECTED");
        startDate = intent.getStringExtra("com.designyourjourney.pictureout.START_DATE");
        endDate = intent.getStringExtra("com.designyourjourney.pictureout.END_DATE");
        currentCity= (City) intent.getSerializableExtra("com.designyourjourney.pictureout.MakePlan.CURRENTLOCATION");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // If location is valid then show on map
        if (currentCity.getLatitude()!=360 || currentCity.getLongitude()!=360) {
            // Show user's current location on map
            LatLng latLng = new LatLng(currentCity.getLatitude(), currentCity.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            googleMap.addMarker(markerOptions);
        }

        // Show selected cities on map
        for (int i=0;i<citiesSelected.size();i++) {
            City temp = citiesSelected.get(i);
            LatLng latLng = new LatLng(temp.getLatitude(),temp.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(temp.getName());
            googleMap.addMarker(markerOptions);
        }
    }

    private void showOnMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(IdealisePlan.this);
    }

}