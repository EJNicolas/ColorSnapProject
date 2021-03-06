package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
//Activity for editing the colour
public class EditColorActivity extends Activity implements View.OnClickListener, SensorEventListener {

    // creates the variables needed to store and change the colour displayed as a LinearLayout
    private LinearLayout colorDisplay;
    private Sensor sensorGyroscope;
    private SensorManager sensorManager = null;
    private int currentColor;
    private String originalColor, colorColumn, hexColor;
    private TextView colorName;
    private Button buttonSaveColor;
    private Button buttonChangeColor;
    private boolean usingRGB;
    private String colorSchemeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_edit_color);
        //Initialize variables
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        buttonSaveColor = (Button) findViewById(R.id.buttonSaveColor);
        buttonChangeColor = (Button) findViewById(R.id.buttonChangeColor);

        colorDisplay = (LinearLayout)findViewById(R.id.colorChange);
        colorName = (TextView) findViewById(R.id.textViewEditColor);

        //Getting intent data. Takes the color to edit and the color's column to place it back into
        Intent i = getIntent();
        originalColor = i.getStringExtra("EDIT_COLOR");
        colorColumn = i.getStringExtra("COLOR_COLUMN");
        colorSchemeName = i.getStringExtra("COLOR_SCHEME_NAME");
        try{
            currentColor = Color.parseColor("#" + originalColor);
        }
        catch(Exception e){
            Toast.makeText(this, "Error parsing color", Toast.LENGTH_SHORT).show();
            currentColor = Color.parseColor("#123123");
        }
        hexColor = originalColor;
        colorDisplay.setBackgroundColor(currentColor);

        //Set listeners
        buttonSaveColor.setOnClickListener(this);
        buttonChangeColor.setOnClickListener(this);

        //Look at the user's preferences and see if they prefer seeing colors displayed in rgb or hexadecimal
        sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        usingRGB = false;
        if(!(sharedPrefs==null)){
            usingRGB = sharedPrefs.getBoolean("usingRGB", false);
        }
        if(usingRGB){
            colorName.setText(convertHexToRGB(hexColor));
        }
        else{
            colorName.setText("#" + hexColor);
        }

    }

    public void editColor(float rotX, float rotY){
        //Method used to handle editing the color. This only happens while the button to change color is continously held down.
        if(buttonChangeColor.isPressed()){
            int color = currentColor;
            float[] hsv= new float [3];
            // converting color to hsv
            Color.colorToHSV(color, hsv);
            //Change the saturation and brightness according to how much the user rotates their phone
            hsv[1]+=rotY/10;
            hsv[2]+=rotX/10;

            //Changes the HSV back to a color value for us to save
            int newColor = Color.HSVToColor(hsv);
            currentColor = newColor;
            //Converting Color to String learned from https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
            hexColor = String.format("%06X", (0xFFFFFF & currentColor));
            if(usingRGB){
                colorName.setText(convertHexToRGB(hexColor));
            }
            else{
                colorName.setText("#" + hexColor);
            }
            colorDisplay.setBackgroundColor(newColor);
        }
    }


    @Override
    protected void onResume() {
        //register sensor listener to this class on resume
        super.onResume();
        sensorManager.registerListener(this,sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        //unregister sensor listener to this class on pause and stop tone if its playing
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if(buttonSaveColor.isPressed()){
            //if a colorColumn didnt come through the intent and no color scheme nave to save to, this means that this color isnt editing a previously made color (came from the camera at the start)
            if(colorColumn==null && colorSchemeName==null){
                Intent i = new Intent(this, ColorSchemesActivity.class);
                i.putExtra("NEW_COLOR", hexColor);
                startActivity(i);
            }
            //If there is a color scheme name found, it goes to that activity to save this color (adding a color to a color scheme)
            else if(!(colorSchemeName==null)){
                Intent i = new Intent(this, ViewColorSchemeActivity.class);
                i.putExtra("SAVED_COLOR_SCHEME_NAME", colorSchemeName);
                i.putExtra("NEW_COLOR", hexColor);
                startActivity(i);
            }
            //if there is a colorColumn found, then it did come from a made color scheme. This method finishes the startActivityForResult() (editing a color from a color scheme)
            else{
                Intent i = new Intent();
                i.putExtra("EDITED_COLOR", hexColor);
                i.putExtra("COLOR_COLUMN", colorColumn);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //set sensor
        int type = event.sensor.getType();
        //Use the Gyroscope and pass the readings to edit the color
        if(type == Sensor.TYPE_GYROSCOPE){
            try{
                editColor(event.values[0], event.values[1]);
            }
            catch(Exception e){
                Toast.makeText(this, "Error retrieving Gyroscope", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String convertHexToRGB(String hex){
        //Method to convert a hexadecimal number to decimal and put it into an rgb() form
        String[] rgbValues = new String[3];
        String rgbString = hex;

        //How to split a string by number of characters learned from https://kodejava.org/how-to-split-a-string-by-a-number-of-characters/
        //Splits the hex value into 3 parts, each part containg two digits
        try{
            rgbValues[0] = hex.substring(0, 2);
            rgbValues[1] = hex.substring(2, 4);
            rgbValues[2] = hex.substring(4, 6);

            //Converting hex to decimal learned from https://www.javatpoint.com/java-hex-to-decimal
            //Converts the split up hex values into decimal.
            for(int i=0;i<rgbValues.length;i++){
                rgbValues[i] = String.format("%d", Integer.parseInt(rgbValues[i], 16));
            }

            //Places the values into an rgb() form
            rgbString = "rgb(" + rgbValues[0] + ", " + rgbValues[1] + ", " + rgbValues[2] + ")";
        }
        catch(Exception e){
            Toast.makeText(this, "Error converting hex to rgb", Toast.LENGTH_LONG).show();
        }

        //Returns the string. If there is some error on converting the hex to rgb, the hex will be returned
        return rgbString;
    }

    public void moveToCamera(View v){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }

    public void moveToColorSchemes(View v){
        Intent i = new Intent(this, ColorSchemesActivity.class);
        startActivity(i);
    }

    public void moveToSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

}
