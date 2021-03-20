package com.example.colorsnap;

import android.app.Activity;
import android.content.Intent;
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

public class ColorSchemesActivity extends Activity implements View.OnClickListener {

    Button buttonAddColorScheme, buttonSearchColorScheme;
    EditText editTextNameInput, editTextSearchName;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_schemes);
        buttonAddColorScheme = (Button) findViewById(R.id.buttonAddColorScheme);
        buttonSearchColorScheme = (Button) findViewById(R.id.buttonSearchColorScheme);
        editTextNameInput = (EditText) findViewById(R.id.editTextColorName);
        editTextSearchName = (EditText) findViewById(R.id.editTextSearchName);
        recyclerView  = (RecyclerView) findViewById(R.id.recyclerViewColorScheme);

        buttonAddColorScheme.setOnClickListener(this);
        buttonSearchColorScheme.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRecyclerViewColorSchemes();
    }

    public void setRecyclerViewColorSchemes(){
        Cursor cursor;
        cursor = Constants.dbColorSchemes.getData();

        int nameColumn = cursor.getColumnIndex(Constants.NAME);

//        might neeed these later
//        int colorColumn1 = cursor.getColumnIndex(Constants.COLOR1);
//        int colorColumn2 = cursor.getColumnIndex(Constants.COLOR2);
//        int colorColumn3 = cursor.getColumnIndex(Constants.COLOR3);
//        int colorColumn4 = cursor.getColumnIndex(Constants.COLOR4);
//        int colorColumn5 = cursor.getColumnIndex(Constants.COLOR5);

        ArrayList<String> mArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String colorName = cursor.getString(nameColumn);
            String s = colorName; //other coloers will be added to the arraylist later
            mArrayList.add(s);
            cursor.moveToNext();
        }

        layoutManager = new LinearLayoutManager(this);
        myAdapter = new MyAdapter(mArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

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
        if(buttonSearchColorScheme.isPressed()){
            if(editTextSearchName.getText().toString().equals("")){
                setRecyclerViewColorSchemes();
            }
            else{
                searchColorScheme(editTextSearchName.getText().toString());
            }
        }
        if(buttonAddColorScheme.isPressed()){
            long id = Constants.dbColorSchemes.createRow(editTextNameInput.getText().toString());
            Intent i = new Intent(this, ViewColorSchemeActivity.class);
            i.putExtra("COLOR_SCHEME_NAME", editTextNameInput.getText().toString());
            i.putExtra("ROW_ID", id);
            startActivity(i);
        }
    }
}
