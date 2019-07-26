package com.example.loginapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loginapp.database.LoginDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText phone;
    private TextInputEditText password;
    private static Cursor cursor;
    private LoginDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new LoginDatabase(this);
        phone = findViewById(R.id.phoneEdit);
        password = findViewById(R.id.passEdit);
        Button loginbtn = findViewById(R.id.loginbtn);
        TextView signuptv = findViewById(R.id.signuptv);
        loginbtn.setOnClickListener(this);
        signuptv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){

            case R.id.loginbtn:
                if (check()){
                    cursor = database.getUser(Long.parseLong(phone.getText().toString()));
                    if (!cursor.moveToFirst())
                        Toast.makeText(this, "Invalid user!", Toast.LENGTH_SHORT).show();
                    else {
                        cursor.moveToFirst();
                        String pwd1 = cursor.getString(4);
                        String pwd2 = password.getText().toString();
                        if (pwd1.equals(pwd2)) {
                            i = new Intent(this, Main2Activity.class);
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

            case R.id.moredetailstv:
                FragmentManager manager = getSupportFragmentManager();
                //manager.beginTransaction().replace(R.id.detailscontainer,)
        }
    }

    public static Cursor getCursor(){
        return cursor;
    }

    public static void setCursor(Cursor c){
        cursor = c;
    }

    private boolean check(){
        boolean status = true;

        if (phone.getText().toString().equals("")){
            status = false;
            phone.setError("Field empty!");
        }

        if (password.getText().toString().equals("")){
            status = false;
            password.setError("Field empty!");
        }

        return status;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.closeDB();
    }
}