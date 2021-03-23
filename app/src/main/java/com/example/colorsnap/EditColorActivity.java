package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;



public class EditColorActivity extends Activity implements View.OnClickListener, SensorEventListener {
    private LinearLayout colorDisplay;
    private Sensor sensorOrientation;
    private SensorManager sensorManager = null;
    private int currentColor =  Color.parseColor("#FF6347");

    Button buttonSaveColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_color);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        buttonSaveColor = (Button) findViewById(R.id.buttonSaveColor);
        colorDisplay = (LinearLayout)findViewById(R.id.colorChange);

        buttonSaveColor.setOnClickListener(this);
    }


    public void setSaturation(float x, float y, float z) {

int color = currentColor;

        float[ ] hsv= new float [3];
        Color.colorToHSV(color, hsv);

        float slope = (float) (1/ 6.28318);
       float output = (float) (slope * (z + 3.14159));
        hsv[1] = output;
        float slopeX = (float) (1/ 6.28318);
        float outputX = (float) (slopeX * (x + 3.14159));
        hsv[2] = outputX;
        int newColor = Color.HSVToColor(hsv);
        currentColor = newColor;
        colorDisplay.setBackgroundColor(newColor);
        Log.d("zTag", String.valueOf((z)));
        Log.d("xTag", String.valueOf((x)));
        Log.d("yTag", String.valueOf((y)));




    }


    @Override
    protected void onResume() {
        //register sensor listener to this class on resume
        super.onResume();
        sensorManager.registerListener(this,sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
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
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //display sensor readings
        int type = event.sensor.getType();
        if(type == Sensor.TYPE_ORIENTATION) {
            //get values for x and y orientation
            //orientationX = event.values[0];
           // orientationY = event.values[1];
        }
        try{
            //display the sensor's information. When there is more than one value in the sensor's reading, make a new line on the text view and display that information.
            setSaturation(event.values[0], event.values[1],event.values[2]);

        }
        catch(Exception e){
            Log.e("SensorDetailsActivity","Error on SensorChanged");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
