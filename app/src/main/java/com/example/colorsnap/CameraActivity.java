package com.example.colorsnap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//Activity for displaying, creating,and operating the camera to take a picture
public class CameraActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    //Create variables
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    Button buttonEditColor;
    ImageView selectedImage;
    LinearLayout colorDisplay;
    Bitmap imageBitmap;
    String hexColor;
    String colorSchemeName;

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
        setContentView(R.layout.activity_camera);

        //Initialize variables
        selectedImage = findViewById(R.id.imageView);
        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        colorDisplay = (LinearLayout) findViewById(R.id.linearLayoutColorPick);

        //Set listeners
        buttonEditColor.setOnClickListener(this);
        selectedImage.setOnTouchListener(this);

        //gets the color scheme's name to save to if available
        Intent i = getIntent();
        if(!(i==null)){
            colorSchemeName = i.getStringExtra("COLOR_SCHEME_NAME");
        }

        //Grants permission to access the camera if the correct request code is called
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        else{
            startActivityForResult(cameraIntent, 100);
        }

    }


    @Override
    public void onClick(View v) {

       //If statement for when the edit color button is press to start the edit color activity. It passes the color the user picks. If its being saved to an existing color scheme, pass its name too.
        if (buttonEditColor.isPressed()) {
            Intent i = new Intent(this, EditColorActivity.class);
            i.putExtra("EDIT_COLOR", hexColor);
            if(!(colorSchemeName==null)){
                i.putExtra("COLOR_SCHEME_NAME", colorSchemeName);
            }
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      // if the request code is correct, capture the image from the camera
        if (requestCode == 100) {
            if(resultCode==RESULT_OK){
                Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                //selectedImage.setImageBitmap(captureImage);
                //Strategy of scaling Bitmap from https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
                selectedImage.setImageBitmap(Bitmap.createScaledBitmap(captureImage, 800, 1100, false));

                //Getting scaled bitmap to pick colors from
                BitmapDrawable drawable = (BitmapDrawable) selectedImage.getDrawable();
                imageBitmap = drawable.getBitmap();
            }
            else{
                Toast.makeText(this, "Camera cancelled. Returning to main menu", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //when the user presses their finger down on the screen, get their finger's position relative to the image view and get the pixel color from that coordinate. Set this color to the color display
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int x = (int) event.getX();
            int y = (int) event.getY();

            if(x < imageBitmap.getWidth() && y < imageBitmap.getHeight()){
                int rawColor = imageBitmap.getPixel(x,y);
                hexColor = String.format("%06X", (0xFFFFFF & rawColor));
                colorDisplay.setBackgroundColor(rawColor);
            }
            return true;
        }
        else
            return false;
    }
}
