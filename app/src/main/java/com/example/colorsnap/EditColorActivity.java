package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class EditColorActivity extends Activity implements View.OnClickListener {

    Button buttonSaveColor;

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
        setContentView(R.layout.activity_edit_color);

        buttonSaveColor = (Button) findViewById(R.id.buttonSaveColor);

        buttonSaveColor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(buttonSaveColor.isPressed()){
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            startActivity(i);
        }

    }
}
