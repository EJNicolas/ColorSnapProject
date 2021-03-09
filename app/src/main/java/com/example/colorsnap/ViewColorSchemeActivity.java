package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class ViewColorSchemeActivity extends Activity implements View.OnClickListener {

    Button buttonEditColor, buttonAddColor, buttonSearchColor;
    EditText tempColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_color_scheme);

        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        buttonAddColor = (Button) findViewById(R.id.buttonAddColor);
        buttonSearchColor = (Button) findViewById(R.id.buttonSearchColor);
        tempColor = (EditText) findViewById(R.id.editTextColorInput);

        buttonEditColor.setOnClickListener(this);
        buttonAddColor.setOnClickListener(this);
        buttonSearchColor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(buttonEditColor.isPressed()){
            Intent i = new Intent(this, EditColorActivity.class);
            startActivity(i);
        }
        else if(buttonAddColor.isPressed()){
            Intent i = new Intent(this, CameraActivity.class);
            startActivity(i);
        }
        else if(buttonSearchColor.isPressed()){
            String input = tempColor.getText().toString();
            Uri webpage = Uri.parse("https://coolors.co/" + input);
            Intent i = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(i);
        }

    }
}
