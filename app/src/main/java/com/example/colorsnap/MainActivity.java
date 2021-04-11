package com.example.colorsnap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Activity for the main menu. Mainly used for navigation to other activities
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Create variables
    Button buttonCamera, buttonColorSchemes, buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Getting shared preference data to decide if light or dark theme will be used
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        if(!(sharedPrefs==null)){
            boolean dark = sharedPrefs.getBoolean("darkMode", false);
            if(dark){
                setTheme(R.style.DarkTheme);
            }
            else{
                setTheme(R.style.LightTheme);
            }
        }
        else{
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_main);

        //Initialize variables
        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonColorSchemes = (Button) findViewById(R.id.buttonColorSchemes);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);

        buttonCamera.setOnClickListener(this);
        buttonColorSchemes.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);

        //Grants permission to access the camera when the app is started
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        //creates a new database
        Constants.dbColorSchemes = new MyDatabase(this);
    }

    @Override
    public void onClick(View v) {
        //intents to move to different activities using buttons
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