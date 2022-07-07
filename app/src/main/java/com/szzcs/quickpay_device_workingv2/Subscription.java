package com.szzcs.quickpay_device_workingv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Subscription extends AppCompatActivity {
    Button blinkback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        blinkback = (Button)findViewById(R.id.linkback);


        blinkback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Subscription.this, MainActivity.class);

                startActivity(intent);

            }

        });




    }
}