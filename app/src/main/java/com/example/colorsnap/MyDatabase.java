package com.example.colorsnap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c){
        context = c;
        helper = new MyHelper(context);
    }

    public long createRow(String name){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.COLOR1, "null");
        contentValues.put(Constants.COLOR2, "null");
        contentValues.put(Constants.COLOR3, "null");
        contentValues.put(Constants.COLOR4, "null");
        contentValues.put(Constants.COLOR5, "null");
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    public int addColor(int id, String colorCode){
        SQLiteDatabase db = helper.getWritableDatabase();
        String columnName = findEmptyColumn(id);
        ContentValues cv = new ContentValues();
        cv.put(columnName, colorCode);
        int count = db.update(Constants.TABLE_NAME, cv, Constants.UID + "=?", new String[]{String.format("%d", id)});
        return count;
    }

    public int editColor(int id, String columnName, String newColor){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnName, newColor);
        int count = db.update(Constants.TABLE_NAME, cv, Constants.UID + "=?", new String[]{String.format("%d", id)});
        return count;
    }

    public String findEmptyColumn(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.COLOR1, Constants.COLOR2, Constants.COLOR3, Constants.COLOR4, Constants.COLOR5};
        String selection = Constants.UID + "='" +id+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
        int colorColumn1 = cursor.getColumnIndex(Constants.COLOR1);
        int colorColumn2 = cursor.getColumnIndex(Constants.COLOR2);
        int colorColumn3 = cursor.getColumnIndex(Constants.COLOR3);
        int colorColumn4 = cursor.getColumnIndex(Constants.COLOR4);
        int colorColumn5 = cursor.getColumnIndex(Constants.COLOR5);
        String outcome = Constants.COLOR1;

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(cursor.getString(colorColumn1).equals("null")){
                outcome =  Constants.COLOR1;
            }
            else if(cursor.getString(colorColumn2).equals("null")){
                outcome =  Constants.COLOR2;
            }
            else if(cursor.getString(colorColumn3).equals("null")){
                outcome =  Constants.COLOR3;
            }
            else if(cursor.getString(colorColumn4).equals("null")){
                outcome = Constants.COLOR4;
            }
            else {
                outcome = Constants.COLOR5;
            }
            cursor.moveToNext();
        }

        return outcome;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.COLOR1, Constants.COLOR2, Constants.COLOR3, Constants.COLOR4, Constants.COLOR5};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getData(String searchName)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.COLOR1, Constants.COLOR2, Constants.COLOR3, Constants.COLOR4, Constants.COLOR5};
        String selection = Constants.NAME + "='" +searchName+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }

    public int deleteRow(String searchName){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {searchName};
        int count = db.delete(Constants.TABLE_NAME, Constants.NAME + "=?", whereArgs);
        return count;
    }

}
