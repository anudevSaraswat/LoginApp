package com.example.loginapp.database;

public class Metadata {

    static public String DATABASE_NAME = "user";

    static public int DB_VERSION = 1;

    static public String TABLE_NAME = "registered_users";

    static public String _ID = "_id";

    static public String NAME = "NAME";

    static public String MAIL = "MAIL";

    static public String PHONE = "PHONE";

    static public String PASSWORD = "PASSWORD";

    static public String DOB = "DOB";

    static public String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME + " VARCHAR(40) NOT NULL," +
            MAIL + " VARCHAR(30) NOT NULL," +
            PHONE + " INTEGER UNIQUE NOT NULL," +
            PASSWORD + " VARCHAR NOT NULL," +
            DOB + " DATE NOT NULL)";

}
