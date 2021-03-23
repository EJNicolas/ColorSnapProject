package com.example.colorsnap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraActivity extends Activity implements View.OnClickListener {
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    Button buttonEditColor;
    Button cameraBtn;
    ImageView selectedImage;

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
        selectedImage = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        buttonEditColor.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }


    }


    @Override
    public void onClick(View v) {
        if (buttonEditColor.isPressed()) {
            Intent i = new Intent(this, EditColorActivity.class);
            startActivity(i);
        } else if (cameraBtn.isPressed()) {
            Toast.makeText(CameraActivity.this, "Clicked.", Toast.LENGTH_SHORT).show();
            startActivityForResult(cameraIntent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {

            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(captureImage);


        }
    }
}
