package com.example.colorsnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //test test test.
    Button buttonCamera, buttonColorSchemes, buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonColorSchemes = (Button) findViewById(R.id.buttonColorSchemes);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);

        buttonCamera.setOnClickListener(this);
        buttonColorSchemes.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(buttonCamera.isPressed()){
            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);
        }
        else if(buttonColorSchemes.isPressed()){
            Intent i = new Intent(this, ColorSchemesActivity.class);
            startActivity(i);
        }
        else if(buttonSettings.isPressed()){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
    }
}