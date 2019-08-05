package com.example.loginapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditMyDetailFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText fNameEd;
    private TextInputEditText lNameEd;
    private TextInputEditText dobEd;
    private TextInputEditText mailEd;
    private TextInputEditText phoneEd;
    private TextInputEditText pwdEd;
    private LoginDatabase database;
    private Context context;
    private String PHONE;


    public EditMyDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_my_detail, container, false);
        context = inflater.getContext();
        database = new LoginDatabase(context);
        fNameEd = v.findViewById(R.id.fNameEd);
        lNameEd = v.findViewById(R.id.lNameEd);
        mailEd = v.findViewById(R.id.mailEd);
        phoneEd = v.findViewById(R.id.phoneEd);
        pwdEd = v.findViewById(R.id.pwd1Ed);
        dobEd = v.findViewById(R.id.dobEd);
        Button button = v.findViewById(R.id.update);
        Cursor cursor = database.getUser(MainActivity.getUserPhone()+"");
        cursor.moveToFirst();
        String[] names = cursor.getString(1).split(" ", 2);
        fNameEd.setText(names[0]);
        lNameEd.setText(names[1]);
        mailEd.setText(cursor.getString(2));
        phoneEd.setText(cursor.getString(3));
        PHONE = cursor.getString(3);
        pwdEd.setText(cursor.getString(4));
        dobEd.setText(cursor.getString(5));
        dobEd.setOnClickListener(this);
        button.setOnClickListener(this);
        return v;
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

    public boolean isValidNamePassAndDOB(String fn, String ln, String pass, String DOB) {

        boolean state = true;

        if (fn.equals("")) {
            fNameEd.setError("First name cannot be empty!");
            state = false;
        }

        if (ln.equals("")) {
            lNameEd.setError("Last name cannot be empty!");
            state = false;
        }

        if (pass.equals("")) {
            pwdEd.setError("Password cannot be empty!");
            state = false;
        }

        if (DOB.equals("")) {
            dobEd.setError("DOB cannot be null!");
            state = false;
        }


        return state;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.dobEd:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int monthh = month +1;
                        dobEd.setText(dayOfMonth+"/"+monthh+"/"+year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();


                break;

            case R.id.update:
                try {
                    String fname = fNameEd.getText().toString();
                    String lname = lNameEd.getText().toString();
                    String name = fname + " " + lname;
                    String mail = mailEd.getText().toString();
                    String phone = phoneEd.getText().toString();
                    String pwd = pwdEd.getText().toString();
                    String dob = dobEd.getText().toString();
                    boolean a = isValidEmail(mail);
                    boolean b = isValidNumber(phone);
                    boolean c = isValidNamePassAndDOB(fname, lname, pwd, dob);
                    if (a && b && c){
                        Cursor cursor = database.getUser(phone);
                        if (cursor.moveToFirst()){
                            new AlertDialog.Builder(context).setTitle("Alert!")
                                    .setMessage("User with same number exists. Choose a different one!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            cursor.close();
                        }
                        else {
                            database.updateMyDetail(name, mail, phone, PHONE, pwd, dob);
                            Toast.makeText(context, "Details updated!", Toast.LENGTH_SHORT).show();
                            MainActivity.setPhone(phone);
                        }
                    }
                } catch (Exception e){
                    Log.e("anudev-->>", e.getMessage());
                }
        }
    }
}