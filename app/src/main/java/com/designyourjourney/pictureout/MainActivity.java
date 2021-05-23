package com.designyourjourney.pictureout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button createPlan;

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
                startActivity(intent);
            }
        });
    }
}
