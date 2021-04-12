package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

//Activity to display colors within a color scheme.
public class ViewColorSchemeActivity extends Activity implements View.OnClickListener {

    //Create variables
    private Button buttonEditColor, buttonAddColor, buttonSearchColor;
    private TextView textViewColorSchemeTitle, textViewSelectedColor;
    private String colorSchemeTitle;
    private String[] colors = new String[5];
    private LinearLayout color1, color2, color3, color4, color5;
    private TextView colorTitle1, colorTitle2, colorTitle3, colorTitle4, colorTitle5;

    private String selectedColor, selectedColumn;
    private int colorSchemeId;

    private final int REQUEST_COLOR_CODE = 0;

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
        setContentView(R.layout.activity_view_color_scheme);
        //Initialize variables
        buttonEditColor = (Button) findViewById(R.id.buttonEditColor);
        buttonAddColor = (Button) findViewById(R.id.buttonAddColor);
        buttonSearchColor = (Button) findViewById(R.id.buttonSearchColor);
        textViewColorSchemeTitle = (TextView) findViewById(R.id.textViewColorSchemeTitle);
        textViewSelectedColor = (TextView) findViewById(R.id.textViewSelectedColor);

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
        //Set listeners
        buttonEditColor.setOnClickListener(this);
        buttonAddColor.setOnClickListener(this);
        buttonSearchColor.setOnClickListener(this);
        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);

        //Get data that tells this activity what color scheme it is (name and id)
        //There are two possibilities, this could be an already made color scheme or a new one.
        Intent i = getIntent();
        colorSchemeId = (int) i.getLongExtra("ROW_ID",-1);
        if(colorSchemeId == -1){
            colorSchemeTitle = i.getStringExtra("SAVED_COLOR_SCHEME_NAME");
        }
        else{
            colorSchemeTitle = i.getStringExtra("COLOR_SCHEME_NAME");
        }

        textViewColorSchemeTitle.setText(colorSchemeTitle);

        //Method to set and display colors on this activity
        setColorsView();
        selectedColor = colorTitle1.getText().toString();
        selectedColumn = Constants.COLOR1;
        textViewSelectedColor.setText(selectedColor);

        //Add the new color that was taken from the camera
        String newColor = i.getStringExtra("NEW_COLOR");
        if(!(newColor==null)){
            Constants.dbColorSchemes.addColor(colorSchemeId, newColor);
            Toast.makeText(this, "Color added",Toast.LENGTH_LONG).show();
            setColorsView();
        }
    }

    @Override
    public void onClick(View v) {
        //Go to the edit color activity, putting the selected color and the name of that color's column
        if(buttonEditColor.isPressed()){
            Intent i = new Intent(this, EditColorActivity.class);
            i.putExtra("EDIT_COLOR", selectedColor);
            i.putExtra("COLOR_COLUMN", selectedColumn);
            startActivityForResult(i, REQUEST_COLOR_CODE);
        }
        //Move to the camera activity
        else if(buttonAddColor.isPressed()){
            Intent i = new Intent(this, CameraActivity.class);
            i.putExtra("COLOR_SCHEME_NAME", colorSchemeTitle);
            startActivity(i);
        }
        //Takes the string from the edit text and places it in the database as a color. This is temporary as we do not have the camera and color picker working

        //Takes user to a website which tells them more information about the color they selected
        else if(buttonSearchColor.isPressed()){
            Uri webpage = Uri.parse("https://coolors.co/" + selectedColor);
            Intent i = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(i);
        }
        //Selects the first color
        else if(color1.isPressed()){
            selectedColumn = Constants.COLOR1;
            selectedColor = colors[0];
            textViewSelectedColor.setText(colorTitle1.getText().toString());
        }
        //Selects the second color
        else if(color2.isPressed()){
            selectedColumn = Constants.COLOR2;
            selectedColor = colors[1];
            textViewSelectedColor.setText(colorTitle2.getText().toString());
        }
        //Selects the third color
        else if(color3.isPressed()){
            selectedColumn = Constants.COLOR3;
            selectedColor = colors[2];
            textViewSelectedColor.setText(colorTitle3.getText().toString());
        }
        //selects the fourth color
        else if(color4.isPressed()){
            selectedColumn = Constants.COLOR4;
            selectedColor = colors[3];
            textViewSelectedColor.setText(colorTitle4.getText().toString());
        }
        //selects the fifth color
        else if(color5.isPressed()){
            selectedColumn = Constants.COLOR5;
            selectedColor = colors[4];
            textViewSelectedColor.setText(colorTitle5.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Method to change a color to the edited color from the EditColorActivity
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_COLOR_CODE){
            if(resultCode == RESULT_OK){
                if(data.hasExtra("EDITED_COLOR")){
                    String editedColor = data.getExtras().getString("EDITED_COLOR");
                    String colorColumn = data.getExtras().getString("COLOR_COLUMN");
                    Constants.dbColorSchemes.editColor(colorSchemeId, colorColumn ,editedColor);
                    setColorsView();
                }
            }
        }
    }

    private void setColorsView(){
        //Get all of the data from a specific color scheme's title
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

        //Look at the user's preferences and see if they prefer seeing colors displayed in rgb or hexadecimal
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        boolean usingRGB = false;
        if(!(sharedPrefs==null)){
            usingRGB = sharedPrefs.getBoolean("usingRGB", false);
        }

        //Checks if the color isnt null (hasnt been filled yet). If it isnt null, display the color's name and if possible set a linear layout's background to that color.
        //This process is repeated for any other colors that arent null
        if(!colors[0].equals("null")){

            if(usingRGB)
                colorTitle1.setText(convertHexToRGB(colors[0]));
            else
                colorTitle1.setText("#" + colors[0]);

            try{
                color1.setPadding(0,0, 0, colorSize);
                color1.setBackgroundColor(Color.parseColor("#" + colors[0].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 1 is not a valid color", Toast.LENGTH_SHORT).show();
            }
        }
        if(!colors[1].equals("null")){
            if(usingRGB)
                colorTitle2.setText(convertHexToRGB(colors[1]));
            else
                colorTitle2.setText("#" + colors[1]);

            try{
                color2.setPadding(0,0, 0, colorSize);
                color2.setBackgroundColor(Color.parseColor("#" + colors[1].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 2 is not a valid color", Toast.LENGTH_SHORT).show();
            }
        }
        if(!colors[2].equals("null")){
            if(usingRGB)
                colorTitle3.setText(convertHexToRGB(colors[2]));
            else
                colorTitle3.setText("#" + colors[2]);

            try{
                color3.setPadding(0,0, 0, colorSize);
                color3.setBackgroundColor(Color.parseColor("#" + colors[2].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 3 is not a valid color", Toast.LENGTH_SHORT).show();
            }
        }
        if(!colors[3].equals("null")){
            if(usingRGB)
                colorTitle4.setText(convertHexToRGB(colors[3]));
            else
                colorTitle4.setText("#" + colors[3]);

            try{
                color4.setPadding(0,0, 0, colorSize);
                color4.setBackgroundColor(Color.parseColor("#" + colors[3].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 4 is not a valid color", Toast.LENGTH_SHORT).show();
            }
        }
        if(!colors[4].equals("null")){
            if(usingRGB)
                colorTitle5.setText(convertHexToRGB(colors[4]));
            else
                colorTitle5.setText("#" + colors[4]);

            try{
                color5.setPadding(0,0, 0, colorSize);
                color5.setBackgroundColor(Color.parseColor("#" + colors[4].toString()));
            }
            catch(Exception e){
                Toast.makeText(this, "Color 5 is not a valid color", Toast.LENGTH_SHORT).show();
            }
        }
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

}
