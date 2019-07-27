package com.example.loginapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
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

    private TextInputEditText mail_ed;
    private TextInputEditText phn_ed;
    private TextInputEditText fname_edit;
    private TextInputEditText lname_edit;
    private TextInputEditText pwd1_edit;
    private TextInputEditText pwd2_edit;
    private TextInputEditText dob_edit;
    private Button log_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname_edit = findViewById(R.id.fNameEd);
        lname_edit = findViewById(R.id.lNameEd);
        mail_ed = findViewById(R.id.mailEd);
        phn_ed = findViewById(R.id.phoneEd);
        pwd1_edit = findViewById(R.id.pwd1Ed);
        pwd2_edit = findViewById(R.id.pwd2Ed);
        dob_edit = findViewById(R.id.dobEd);
        log_btn = findViewById(R.id.login);
        dob_edit.setOnClickListener(this);
        log_btn.setOnClickListener(this);
    }

    public boolean isValidEmail(String s) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            mail_ed.setError("Invalid email!");
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
            phn_ed.setError("Invalid phone!");
            return false;
        }
    }

    public boolean isValidNamePassAndDOB(String fn, String ln, String pass1, String pass2, String DOB) {

        boolean state = true;

        if (fn.equals("")) {
            fname_edit.setError("First name cannot be empty!");
            state = false;
        }

        if (ln.equals("")) {
            lname_edit.setError("Last name cannot be empty!");
            state = false;
        }

        if (pass1.equals("")) {
            pwd1_edit.setError("Password cannot be empty!");
            state = false;
        }

        if (pass2.equals("")) {
            pwd2_edit.setError("Password cannot be empty!");
            state = false;
        }

        if (DOB.equals("")) {
            dob_edit.setError("DOB cannot be null!");
            state = false;
        }

        if (!pass1.equals(pass2)) {
            pwd2_edit.setError("password not same as above!");
            state = false;
        }

        return state;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login:
                boolean a = isValidEmail(mail_ed.getText().toString());
                boolean b = isValidNumber(phn_ed.getText().toString());
                boolean c = isValidNamePassAndDOB(fname_edit.getText().toString(), lname_edit.getText().toString(), pwd1_edit.getText().toString(), pwd2_edit.getText().toString(), dob_edit.getText().toString());
                Intent i = new Intent(this, MainActivity.class);
                if (a && b && c) {
                    try {
                        LoginDatabase database = new LoginDatabase(this);
                        String name = fname_edit.getText() + " " + lname_edit.getText();
                        database.insertValues(name, mail_ed.getText().toString(), Long.parseLong(phn_ed.getText().toString()), pwd1_edit.getText().toString(), dob_edit.getText().toString());
                        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
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
                        dob_edit.setText(dayOfMonth + "/" + c + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
        }
    }
}