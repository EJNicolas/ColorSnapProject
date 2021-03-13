package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class ColorSchemesActivity extends Activity implements View.OnClickListener {

    Button buttonViewColorScheme, buttonAddColorScheme;
    EditText editTextNameInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_schemes);
        buttonViewColorScheme = (Button) findViewById(R.id.buttonViewColorScheme);
        buttonAddColorScheme = (Button) findViewById(R.id.buttonAddColorScheme);
        editTextNameInput = (EditText) findViewById(R.id.editTextColorName);

        buttonViewColorScheme.setOnClickListener(this);
        buttonAddColorScheme.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(buttonViewColorScheme.isPressed()){
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            startActivity(i);
        }
        else if(buttonAddColorScheme.isPressed()){
            long id = Constants.dbColorSchemes.createRow(editTextNameInput.getText().toString());
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            i.putExtra("COLOR_SCHEME_NAME", editTextNameInput.getText().toString());
            i.putExtra("ROW_ID", id);
            startActivity(i);
        }
    }
}
