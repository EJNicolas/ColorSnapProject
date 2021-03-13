package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class ViewColorSchemeActivity extends Activity implements View.OnClickListener {

    private Button buttonEditColor, buttonAddColor, buttonSearchColor, buttonSaveColor;
    private EditText tempColor;
    private TextView textViewColorSchemeTitle;

    private int colorSchemeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_color_scheme);

        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        buttonAddColor = (Button) findViewById(R.id.buttonAddColor);
        buttonSearchColor = (Button) findViewById(R.id.buttonSearchColor);
        buttonSaveColor = (Button) findViewById(R.id.buttonSaveColor);
        tempColor = (EditText) findViewById(R.id.editTextColorInput);
        textViewColorSchemeTitle = (TextView) findViewById(R.id.textViewColorSchemeTitle);

        buttonEditColor.setOnClickListener(this);
        buttonAddColor.setOnClickListener(this);
        buttonSearchColor.setOnClickListener(this);
        buttonSaveColor.setOnClickListener(this);

        Intent i = getIntent();
        String colorSchemeTitle = i.getStringExtra("COLOR_SCHEME_NAME");
        textViewColorSchemeTitle.setText(colorSchemeTitle);
        colorSchemeId = (int) i.getLongExtra("ROW_ID",-1);

        Log.d("ViewColorScheme", String.format("%d", colorSchemeId));
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
        else if(buttonSaveColor.isPressed()){
            String input = tempColor.getText().toString();
            Constants.dbColorSchemes.addColor(colorSchemeId, input);
        }
        else if(buttonSearchColor.isPressed()){
            String input = tempColor.getText().toString();
            Uri webpage = Uri.parse("https://coolors.co/" + input);
            Intent i = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(i);
        }

    }
}
