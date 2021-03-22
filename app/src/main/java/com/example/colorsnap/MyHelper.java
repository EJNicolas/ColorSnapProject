package com.example.colorsnap;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
//Class to help build the database. Mainly reused/repurposed from Unit 6 PlantDataBaseRecyclerView project
public class MyHelper extends SQLiteOpenHelper {

    //Create variables
    private Context context;

    //Initializes the variable which will create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.COLOR1 + " TEXT, " +
                    Constants.COLOR2 + " TEXT, " +
                    Constants.COLOR3 + " TEXT, " +
                    Constants.COLOR4 + " TEXT, " +
                    Constants.COLOR5 + " TEXT);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
