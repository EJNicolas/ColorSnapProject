package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

public class SettingsActivity extends Activity implements View.OnClickListener, ToggleButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private Button buttonSaveSettings;
    private ToggleButton toggleDarkMode;
    private RadioGroup groupRGBHex;

    private boolean darkMode;
    private boolean rgbDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        buttonSaveSettings = (Button) findViewById(R.id.buttonSaveSettings);
        toggleDarkMode = (ToggleButton) findViewById(R.id.toggleDarkMode);
        groupRGBHex = (RadioGroup) findViewById(R.id.radioRGBHexGroup);

        buttonSaveSettings.setOnClickListener(this);
        toggleDarkMode.setOnCheckedChangeListener(this);
        groupRGBHex.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        if(buttonSaveSettings.isPressed()){
            saveSettings();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            Toast.makeText(this, "Dark mode turned on", Toast.LENGTH_SHORT).show();
            darkMode = true;
        }
        else{
            Toast.makeText(this, "Dark mode turned off", Toast.LENGTH_SHORT).show();
            darkMode = false;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
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
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("darkMode", darkMode);
        editor.putBoolean("usingRGB", rgbDisplay);
        editor.commit();

    }


}
