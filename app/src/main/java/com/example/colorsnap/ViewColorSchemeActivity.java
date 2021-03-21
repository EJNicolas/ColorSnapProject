package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class ViewColorSchemeActivity extends Activity implements View.OnClickListener {

    private Button buttonEditColor, buttonAddColor, buttonSearchColor, buttonSaveColor;
    private EditText tempColor;
    private TextView textViewColorSchemeTitle;
    private String colorSchemeTitle;
    private String[] colors = new String[5];
    private LinearLayout color1, color2, color3, color4, color5;
    private TextView colorTitle1, colorTitle2, colorTitle3, colorTitle4, colorTitle5;

    private String focusedColor;

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

        color1 = (LinearLayout) findViewById(R.id.linearLayoutColor1);
        color2 = (LinearLayout) findViewById(R.id.linearLayoutColor2);
        color3 = (LinearLayout) findViewById(R.id.linearLayoutColor3);
        color4 = (LinearLayout) findViewById(R.id.linearLayoutColor4);
        color5 = (LinearLayout) findViewById(R.id.linearLayoutColor5);

        colorTitle1 = (TextView) findViewById(R.id.textViewColor1);
        colorTitle2 = (TextView) findViewById(R.id.textViewColor2);
        colorTitle3 = (TextView) findViewById(R.id.textViewColor3);
        colorTitle4 = (TextView) findViewById(R.id.textViewColor4);
        colorTitle5 = (TextView) findViewById(R.id.textViewColor5);

        buttonEditColor.setOnClickListener(this);
        buttonAddColor.setOnClickListener(this);
        buttonSearchColor.setOnClickListener(this);
        buttonSaveColor.setOnClickListener(this);

        Intent i = getIntent();
        colorSchemeId = (int) i.getLongExtra("ROW_ID",-1);
        if(colorSchemeId == -1){
            colorSchemeTitle = i.getStringExtra("SAVED_COLOR_SCHEME_NAME");
        }
        else{
            colorSchemeTitle = i.getStringExtra("COLOR_SCHEME_NAME");
        }
        textViewColorSchemeTitle.setText(colorSchemeTitle);

        setColorsView();
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
            Toast.makeText(this, "Color added",Toast.LENGTH_LONG).show();
            setColorsView();
        }
        else if(buttonSearchColor.isPressed()){
            String input = tempColor.getText().toString();
            Uri webpage = Uri.parse("https://coolors.co/" + input);
            Intent i = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(i);
        }
    }

    private void setColorsView(){
        Cursor cursor;
        cursor = Constants.dbColorSchemes.getData(colorSchemeTitle);
        int colorColumn1 = cursor.getColumnIndex(Constants.COLOR1);
        int colorColumn2 = cursor.getColumnIndex(Constants.COLOR2);
        int colorColumn3 = cursor.getColumnIndex(Constants.COLOR3);
        int colorColumn4 = cursor.getColumnIndex(Constants.COLOR4);
        int colorColumn5 = cursor.getColumnIndex(Constants.COLOR5);

        cursor.moveToFirst();
        colorSchemeId = cursor.getInt(0);
        colors[0] = cursor.getString(colorColumn1);
        colors[1] = cursor.getString(colorColumn2);
        colors[2] = cursor.getString(colorColumn3);
        colors[3] = cursor.getString(colorColumn4);
        colors[4] = cursor.getString(colorColumn5);

        int colorSize = 300;

        if(!colors[0].equals("null")){
            colorTitle1.setText(colors[0]);
            try{
                color1.setPadding(0,0, 0, colorSize);
                color1.setBackgroundColor(Color.parseColor("#" + colors[0].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 1 is not a valid color", Toast.LENGTH_LONG).show();
            }
        }
        if(!colors[1].equals("null")){
            colorTitle2.setText(colors[1]);
            try{
                color2.setPadding(0,0, 0, colorSize);
                color2.setBackgroundColor(Color.parseColor("#" + colors[1].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 2 is not a valid color", Toast.LENGTH_LONG).show();
            }
        }
        if(!colors[2].equals("null")){
            colorTitle3.setText(colors[2]);
            try{
                color3.setPadding(0,0, 0, colorSize);
                color3.setBackgroundColor(Color.parseColor("#" + colors[2].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 3 is not a valid color", Toast.LENGTH_LONG).show();
            }
        }
        if(!colors[3].equals("null")){
            colorTitle4.setText(colors[3]);
            try{
                color4.setPadding(0,0, 0, colorSize);
                color4.setBackgroundColor(Color.parseColor("#" + colors[3].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 4 is not a valid color", Toast.LENGTH_LONG).show();
            }
        }
        if(!colors[4].equals("null")){
            colorTitle5.setText(colors[4]);
            try{
                color5.setPadding(0,0, 0, colorSize);
                color5.setBackgroundColor(Color.parseColor("#" + colors[4].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 5 is not a valid color", Toast.LENGTH_LONG).show();
            }
        }
    }
}
