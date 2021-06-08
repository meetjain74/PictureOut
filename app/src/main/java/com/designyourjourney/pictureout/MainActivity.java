package com.designyourjourney.pictureout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.designyourjourney.pictureout.db.City;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private Button createPlan;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private ImageView arrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Set on click listener to button
        createPlan=findViewById(R.id.planButton);
        createPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new intent to go to another screen (explicit intent)
                Intent intent=new Intent(getApplicationContext(),MakePlan.class);
                passData(intent);
                startActivity(intent);
            }
        });

        // Fetch current location of user
        // FusedLocationProviderClient ->  a location service that combines GPS location and network location
        // GPS location is used to provide accuracy and network location is used to get location when the user is indoors.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        arrowButton=findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllPlansView.class);
                startActivity(intent);
            }
        });
    }

    private void fetchLocation() {
        // Check for permissions
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request for permissions
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, R.string.location_not_determined, Toast.LENGTH_SHORT).show();
            // Request for permissions
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        // Getting last location from Fused Location Provider Client Object
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    currentLocation = location;
                    /* Toast.makeText(MainActivity.this, currentLocation.getLatitude() +
                            "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show(); */
                }
            }
        });
    }

    private void requestNewLocationData() {
        // Initializing LocationRequest object with appropriate methods
        LocationRequest locationRequest = new LocationRequest();

        //  For a city level accuracy(low accuracy), use PRIORITY_LOW_POWER.
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        // Set the desired interval for active location updates, in milliseconds.
        locationRequest.setInterval(5);

        // Explicitly set the fastest interval for location updates, in milliseconds
        locationRequest.setFastestInterval(0);

        // Set the number of location updates.
        locationRequest.setNumUpdates(1);

        // setting LocationRequest on FusedLocationClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for permissions
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request for permissions
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, R.string.location_not_determined, Toast.LENGTH_SHORT).show();
            // Request for permissions
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location=locationResult.getLastLocation();
            currentLocation=location;
            /* Toast.makeText(MainActivity.this, currentLocation.getLatitude() +
                    "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show(); */
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
            else {
                // Permission not granted
                currentLocation=null;
            }
        }
    }

    private void passData(Intent intent) {
        // Create a new city to store latitude and longitude value of current location
        City currentCity = new City();
        if (currentLocation!=null) {
            currentCity.setLatitude(currentLocation.getLatitude());
            currentCity.setLongitude(currentLocation.getLongitude());
        }
        else {
            // Current location is null as user denied permission for location access
            // Set latitude and longitude value to be a invalid value
            // Valid range is -90 to 90 for latitude and -180 to 180 for longitude.
            currentCity.setLatitude(360); // Invalid value
            currentCity.setLongitude(360); // Invalid value
        }

        // Pass this city through intent
        intent.putExtra("com.designyourjourney.pictureout.MainActivity.CURRENTLOCATION",currentCity);
    }
}
