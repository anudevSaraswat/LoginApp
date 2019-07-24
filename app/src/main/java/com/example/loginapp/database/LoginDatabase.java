package com.example.loginapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public LoginDatabase(Context context) {
        super(context, Metadata.DATABASE_NAME, null, Metadata.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Metadata.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void insertValues(String name, String mail, long phn, String pwd, String dob){
        ContentValues values = new ContentValues();
        values.put(Metadata.NAME, name);
        values.put(Metadata.MAIL, mail);
        values.put(Metadata.PHONE, phn);
        values.put(Metadata.PASSWORD, pwd);
        values.put(Metadata.DOB, dob);
        db = this.getReadableDatabase();
        db.insert(Metadata.TABLE_NAME, null, values);
    }

    public void updateUser(String name, String mail, long phn, String pwd, String dob){
        ContentValues values = new ContentValues();
        values.put(Metadata.NAME, name);
        values.put(Metadata.MAIL, mail);
        values.put(Metadata.PHONE, phn);
        values.put(Metadata.PASSWORD, pwd);
        values.put(Metadata.DOB, dob);
        db = this.getWritableDatabase();
        db.update(Metadata.TABLE_NAME, values, Metadata.PHONE+"=?", new String[]{Long.toString(phn)});
    }

    public Cursor getAllUsers(){
        db = this.getReadableDatabase();
        return db.query(Metadata.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getUser(long number){
        db = this.getReadableDatabase();
        return db.query(Metadata.TABLE_NAME, null, Metadata.PHONE+"="+number, null, null, null, null);
    }

    public void closeDB(){
        db.close();
    }

}

