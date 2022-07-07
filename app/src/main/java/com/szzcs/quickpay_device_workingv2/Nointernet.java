package com.szzcs.quickpay_device_workingv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Nointernet extends AppCompatActivity {
    Button btnrelaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        btnrelaunch = (Button)findViewById(R.id.relaunch);


        btnrelaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nointernet = new Intent(Nointernet.this, MainActivity.class);
                startActivity(nointernet);


            }

        });



    }
}