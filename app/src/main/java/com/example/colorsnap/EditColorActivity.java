package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class EditColorActivity extends Activity implements View.OnClickListener {

    Button buttonSaveColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
