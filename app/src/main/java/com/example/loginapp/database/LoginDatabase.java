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


    public void insertValues(String name, String mail, String phone, String pwd, String dob){
        ContentValues values = getCV(name, mail, phone, pwd, dob);
        db = this.getReadableDatabase();
        db.insert(Metadata.TABLE_NAME, null, values);
    }


    public void updateMyDetail(String name, String mail, String newPhone, String oldPhone, String pwd, String dob){
        ContentValues values = getCV(name, mail, newPhone, pwd, dob);
        db = this.getWritableDatabase();
        db.update(Metadata.TABLE_NAME, values, Metadata.PHONE+"=?", new String[]{oldPhone});
    }


//    public void updateUser(String id, String name, String mail, String phone, String pwd, String dob){
//        ContentValues values = getCV(name, mail, phone, pwd, dob);
//        db = this.getWritableDatabase();
//        db.update(Metadata.TABLE_NAME, values, Metadata._ID+"=?", new String[]{id});
//    }


    public Cursor getAllUsers(String number){
        db = this.getReadableDatabase();
        return db.query(Metadata.TABLE_NAME, null, Metadata.PHONE+"!=?", new String[]{number}, null, null, null);
    }


    public Cursor getUser(String number){
        db = this.getReadableDatabase();
        return db.query(Metadata.TABLE_NAME, null, Metadata.PHONE+"=?", new String[]{number}, null, null, null);
    }


    public void deleteUser(String number){
        db = this.getWritableDatabase();
        db.delete(Metadata.TABLE_NAME, Metadata.PHONE+"=?", new String[]{number});
    }


//    public Cursor getUser1(int id){
//        db = this.getReadableDatabase();
//        return db.query(Metadata.TABLE_NAME, null, Metadata._ID+"="+id, null, null, null, null);
//    }


    private ContentValues getCV(String name, String mail, String phn, String pwd, String dob){
        ContentValues values = new ContentValues();
        values.put(Metadata.NAME, name);
        values.put(Metadata.MAIL, mail);
        values.put(Metadata.PHONE, phn);
        values.put(Metadata.PASSWORD, pwd);
        values.put(Metadata.DOB, dob);
        return values;
    }
}