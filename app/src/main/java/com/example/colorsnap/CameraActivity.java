package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class CameraActivity extends Activity implements View.OnClickListener {

    Button buttonEditColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
