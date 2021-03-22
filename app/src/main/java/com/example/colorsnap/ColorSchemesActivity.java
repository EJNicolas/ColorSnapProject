package com.example.colorsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Activity for displaying, creating, deleting, and searching color schemes
public class ColorSchemesActivity extends Activity implements View.OnClickListener {

    //Create variables
    Button buttonAddColorScheme, buttonSearchColorScheme;
    EditText editTextNameInput, editTextSearchName;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

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
        setContentView(R.layout.activity_color_schemes);
        //Initialize variables
        buttonAddColorScheme = (Button) findViewById(R.id.buttonAddColorScheme);
        buttonSearchColorScheme = (Button) findViewById(R.id.buttonSearchColorScheme);
        editTextNameInput = (EditText) findViewById(R.id.editTextColorName);
        editTextSearchName = (EditText) findViewById(R.id.editTextSearchName);
        recyclerView  = (RecyclerView) findViewById(R.id.recyclerViewColorScheme);
        //Set listeners
        buttonAddColorScheme.setOnClickListener(this);
        buttonSearchColorScheme.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        //Set recyclerview to see color scheme names. Done on resume to show changes done by searching and deleting.
        super.onResume();
        setRecyclerViewColorSchemes();
    }

    public void setRecyclerViewColorSchemes(){
        //Create cursor to get all the rows from the database
        Cursor cursor;
        cursor = Constants.dbColorSchemes.getData();

        //Get all the data from the names column
        int nameColumn = cursor.getColumnIndex(Constants.NAME);

//        Might be used later to display the color scheme in the activity
//        int colorColumn1 = cursor.getColumnIndex(Constants.COLOR1);
//        int colorColumn2 = cursor.getColumnIndex(Constants.COLOR2);
//        int colorColumn3 = cursor.getColumnIndex(Constants.COLOR3);
//        int colorColumn4 = cursor.getColumnIndex(Constants.COLOR4);
//        int colorColumn5 = cursor.getColumnIndex(Constants.COLOR5);

        //Create ArrayList to put all names in.
        ArrayList<String> mArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String colorName = cursor.getString(nameColumn);
            String s = colorName; //other coloers will be added to the arraylist later
            mArrayList.add(s);
            cursor.moveToNext();
        }

        //Initialize needed variables for the recyclerview and give the adapter the array list
        layoutManager = new LinearLayoutManager(this);
        myAdapter = new MyAdapter(mArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    //Searches for a color scheme name in the database. Uses a query instead of getting all the data
    public void searchColorScheme(String name){
        Cursor cursor;
        cursor = Constants.dbColorSchemes.getData(name);
        int nameColumn = cursor.getColumnIndex(Constants.NAME);
        ArrayList<String> mArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String colorName = cursor.getString(nameColumn);
            String s = colorName; //other colors might be added to the arraylist later
            mArrayList.add(s);
            cursor.moveToNext();
        }
        myAdapter = new MyAdapter(mArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        //If statements for search query. Takes the current text from the edit text and does a search query. If the edit text has nothing in it, all rows will be displayed again
        if(buttonSearchColorScheme.isPressed()){
            if(editTextSearchName.getText().toString().equals("")){
                setRecyclerViewColorSchemes();
            }
            else{
                searchColorScheme(editTextSearchName.getText().toString());
            }
        }
        //Creates a new row in the data base and starts the activity to view the colors in a scheme. Names the row with the name provided on the edit text. Passes the color scheme's name and id for saving in the next activity
        if(buttonAddColorScheme.isPressed()){
            long id = Constants.dbColorSchemes.createRow(editTextNameInput.getText().toString());
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            i.putExtra("COLOR_SCHEME_NAME", editTextNameInput.getText().toString());
            i.putExtra("ROW_ID", id);
            startActivity(i);
        }
    }
}
