package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loginapp.database.LoginDatabase;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText phoneEd;
    private TextInputEditText pwdEd;
    private LoginDatabase database;
    private Intent i;
    private SharedPreferences pref;
    private CheckBox checkBox;
    public static String USER_PHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = new Intent(this, Main2Activity.class);
        pref = getSharedPreferences("preference", MODE_PRIVATE);
        phoneEd = findViewById(R.id.phoneEdit);
        checkBox = findViewById(R.id.cb);
        pwdEd = findViewById(R.id.passEdit);
        int key = pref.getInt("loggedIn", 0);
        if(key == 1){
            USER_PHONE = pref.getString("userPhone1", "");
            startActivity(i);
        }
        else if (!pref.getString("userPhone2", "").equals("")){
            phoneEd.setText(pref.getString("userPhone2", ""));
            pwdEd.setText(pref.getString("userPassword", ""));
            checkBox.setChecked(true);
        }
        Button loginbtn = findViewById(R.id.loginbtn);
        TextView signuptv = findViewById(R.id.signuptv);
        loginbtn.setOnClickListener(this);
        signuptv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database = new LoginDatabase(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.loginbtn:
                String phone = phoneEd.getText().toString();
                String pwd = pwdEd.getText().toString();
                if (check(phone, pwd)){
                    Cursor cursor = database.getUser(phone);
                    if (!cursor.moveToFirst())
                        Toast.makeText(this, "Invalid user!", Toast.LENGTH_SHORT).show();
                    else {
                        cursor.moveToFirst();
                        String pwd1 = cursor.getString(4);
                        if (pwd1.equals(pwd)) {
                            USER_PHONE = phone;
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putInt("loggedIn", 1);
                            edit.putString("userPhone1", USER_PHONE);
                            if (checkBox.isChecked()){
                                edit.putString("userPhone2", USER_PHONE);
                                edit.putString("userPassword", pwd);
                            }
                            else {
                                edit.putString("userPhone2", "");
                                edit.putString("userPassword", "");
                            }
                            edit.apply();
                            startActivity(i);
                        }
                        else
                            Toast.makeText(this, "Incorrect password or phone number!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.signuptv:
                i = new Intent(this, SignUpActivity.class);
                startActivity(i);

                break;


        }
    }

    public static String getUserPhone(){return USER_PHONE;}

    public static void setPhone(String phone){
        USER_PHONE = phone;
    }

    private boolean check(String phone, String pwd){
        boolean status = true;

        if (phone.equals("")){
            status = false;
            phoneEd.setError("Field empty!");
        }

        if (pwd.equals("")){
            status = false;
            pwdEd.setError("Field empty!");
        }

        return status;
    }

    private void insertThousand(){
        LoginDatabase db = new LoginDatabase(this);
        String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random(1000000000);
        String[] numbers = new String[1000];
        String[] name = new String[1000];
        String[] mail = new String[1000];

        //generating 1000 random numbers
        for (int i = 0; i < 1000; i++)
            numbers[i] = random.nextInt(1000000000)+"1";

        //generating 1000 random strings for name
        for (int i = 0; i < 1000; i++){
            StringBuilder builder = new StringBuilder();
            for(int j = 0; j < 5; j++)
                builder.append(ALPHABETS.charAt((int)(Math.random() * ALPHABETS.length())));
            name[i] = builder.toString();
        }

        //generating 1000 random for email
        for (int i = 0; i < 1000; i++){
            StringBuilder builder = new StringBuilder();
            for(int j = 0; j < 5; j++)
                builder.append(ALPHABETS.charAt((int)(Math.random() * ALPHABETS.length())));
            mail[i] = builder.toString() + "@gm.com";
        }

        //inserting 1000 records
        for (int i = 0; i < 1000; i++){
            db.insertValues(name[i] + " " + name[i],
                    mail[i], numbers[i], "hello",
                    random.nextInt(32) + "/" + random.nextInt(13) + "/" + random.nextInt(2000));
        }

    }
}