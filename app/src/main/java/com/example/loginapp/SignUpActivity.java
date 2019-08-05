package com.example.loginapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.loginapp.database.LoginDatabase;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText mailEd;
    private TextInputEditText phoneEd;
    private TextInputEditText fNameEd;
    private TextInputEditText lNameEd;
    private TextInputEditText pwd1Ed;
    private TextInputEditText pwd2Ed;
    private TextInputEditText dobEd;
    private Button logButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        fNameEd = findViewById(R.id.fNameEd);
        lNameEd = findViewById(R.id.lNameEd);
        mailEd = findViewById(R.id.mailEd);
        phoneEd = findViewById(R.id.phoneEd);
        pwd1Ed = findViewById(R.id.pwd1Ed);
        pwd2Ed = findViewById(R.id.pwd2Ed);
        dobEd = findViewById(R.id.dobEd);
        logButton = findViewById(R.id.login);
        dobEd.setOnClickListener(this);
        logButton.setOnClickListener(this);
    }

    public boolean isValidEmail(String s) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            mailEd.setError("Invalid email!");
            return false;
        } else
            return true;
    }

    public boolean isValidNumber(String a) {
        Pattern pattern = Patterns.PHONE;
        Matcher matcher = pattern.matcher(a);
        if (matcher.matches() && a.length() == 10)
            return true;
        else {
            phoneEd.setError("Invalid phone!");
            return false;
        }
    }

    public boolean isValidNamePassAndDOB(String fn, String ln, String pass1, String pass2, String DOB) {

        boolean state = true;

        if (fn.equals("")) {
            fNameEd.setError("First name cannot be empty!");
            state = false;
        }

        if (ln.equals("")) {
            lNameEd.setError("Last name cannot be empty!");
            state = false;
        }

        if (pass1.equals("")) {
            pwd1Ed.setError("Password cannot be empty!");
            state = false;
        }

        if (pass2.equals("")) {
            pwd2Ed.setError("Password cannot be empty!");
            state = false;
        }

        if (DOB.equals("")) {
            dobEd.setError("DOB cannot be null!");
            state = false;
        }

        if (!pass1.equals(pass2)) {
            pwd2Ed.setError("password not same as above!");
            state = false;
        }

        return state;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login:
                String fname = fNameEd.getText().toString();
                String lname = lNameEd.getText().toString();
                String name = fname + " " + lname;
                String mail = mailEd.getText().toString();
                String phone = phoneEd.getText().toString();
                String pwd1 = pwd1Ed.getText().toString();
                String pwd2 = pwd2Ed.getText().toString();
                String dob = dobEd.getText().toString();
                boolean a = isValidEmail(mail);
                boolean b = isValidNumber(phone);
                boolean c = isValidNamePassAndDOB(fname, lname, pwd1, pwd2, dob);
                Intent i = new Intent(this, MainActivity.class);
                if (a && b && c) {
                    try {
                        LoginDatabase database = new LoginDatabase(this);
                        Cursor cursor = database.getUser(phone);
                        if (cursor.moveToFirst()){
                            new AlertDialog.Builder(this).setTitle("ALERT").
                                    setMessage("User with same number already exists!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                            cursor.close();
                        }
                        else {
                            database.insertValues(name, mail, phone, pwd1, dob);
                            Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Log.e("anudev-->>", e.getMessage());
                    }
                }

                break;

            case R.id.dobEd:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int c = month + 1;
                        dobEd.setText(dayOfMonth + "/" + c + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Alert")
                .setMessage("All the entered details will be lost, are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}