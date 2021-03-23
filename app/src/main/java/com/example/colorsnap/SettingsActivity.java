package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

//Activity for user settings
public class SettingsActivity extends Activity implements View.OnClickListener, ToggleButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    //Create variables
    private Button buttonSaveSettings;
    private ToggleButton toggleDarkMode;
    private RadioGroup groupRGBHex;
    private RadioButton radioRGB, radioHex;

    private boolean darkMode;
    private boolean rgbDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Getting shared preference data to decide if light or dark theme will be used
        //Implementing dark theme referenced from https://blog.prototypr.io/implementing-dark-theme-in-android-dfe63e62145d
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
        setContentView(R.layout.activity_settings);

        //Initialize variables
        buttonSaveSettings = (Button) findViewById(R.id.buttonSaveSettings);
        toggleDarkMode = (ToggleButton) findViewById(R.id.toggleDarkMode);
        groupRGBHex = (RadioGroup) findViewById(R.id.radioRGBHexGroup);
        radioRGB = (RadioButton) findViewById(R.id.radioRGB);
        radioHex = (RadioButton) findViewById(R.id.radioHex);

        //Set listeners
        buttonSaveSettings.setOnClickListener(this);
        toggleDarkMode.setOnCheckedChangeListener(this);
        groupRGBHex.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Change UI appearance to match shared preferences data.
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        if(!(sharedPrefs==null)){
            toggleDarkMode.setChecked(sharedPrefs.getBoolean("darkMode", false));
            if(sharedPrefs.getBoolean("usingRGB", false) == true){
                radioRGB.setChecked(true);
                radioHex.setChecked(false);
            }
            else{
                radioRGB.setChecked(false);
                radioHex.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //save settings on press of the button
        if(buttonSaveSettings.isPressed()){
            saveSettings();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //set the the dark mode using the toggle button
        if(isChecked){
            darkMode = true;
        }
        else{
            darkMode = false;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //set the display for color codes usng the radio buttons
        switch (checkedId){
            case R.id.radioRGB:
                rgbDisplay = true;
                break;
            case R.id.radioHex:
                rgbDisplay = false;
                break;
        }
    }

    public void saveSettings(){
        //Save the settings according to the UI
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("darkMode", darkMode);
        editor.putBoolean("usingRGB", rgbDisplay);
        Toast.makeText(this, "Reset app to view changes", Toast.LENGTH_LONG).show();
        editor.commit();
    }


}
