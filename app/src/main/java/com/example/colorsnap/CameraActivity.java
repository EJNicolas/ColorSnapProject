package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class CameraActivity extends Activity implements View.OnClickListener {

    Button buttonEditColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_camera);

        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        buttonEditColor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(buttonEditColor.isPressed()){
            Intent i = new Intent(this, EditColorActivity.class);
            startActivity(i);
        }
    }
}
