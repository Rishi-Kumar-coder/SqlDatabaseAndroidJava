package com.example.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDbHandler extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "contact" ,COLUMN_ID = "id",COLUMN_NAME="name",COLUMN_PHONE="phone", DATABASE_NAME="databse";
    private static final Integer TABLE_VERSION = 1;

    public MyDbHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ TABLE_NAME+" ( "+COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
        +COLUMN_NAME+" TEXT , "+COLUMN_PHONE +" TEXT)");



    }

    void addContact(contactModal modal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME,modal.getName());
        values.put(COLUMN_PHONE,modal.getPhone());

        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    void deleteContact(contactModal contactModal){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_ID+ " == "+contactModal.getId(),null);
        db.close();
    }

    ArrayList<contactModal> fetchData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME,null);
        ArrayList<contactModal> data = new ArrayList<>();
        while (cursor.moveToNext()){
            contactModal modal = new contactModal();

            modal.setId(cursor.getInt(0));
            modal.setName(cursor.getString(1));
            modal.setPhone(cursor.getString(2));
            data.add(modal);

        }

        return data;
    }

    void updateContact(contactModal Old,contactModal New){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_ID,New.getId());
        newValues.put(COLUMN_NAME,New.getName());
        newValues.put(COLUMN_PHONE,New.getPhone());
        db.update(TABLE_NAME,newValues,COLUMN_ID + " == " + Old.getId(),null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
