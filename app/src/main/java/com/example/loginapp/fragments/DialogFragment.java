package com.example.loginapp.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DialogFragment extends android.support.v4.app.DialogFragment {

    private long PHONE;
    private TextInputEditText fNameEd;
    private TextInputEditText lNameEd;
    private TextInputEditText mailEd;
    private TextInputEditText phoneEd;
    private TextInputEditText pwdEd;
    private TextInputEditText dobEd;


    public DialogFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_my_detail, container);
        fNameEd = view.findViewById(R.id.fNameEd);
        lNameEd = view.findViewById(R.id.lNameEd);
        mailEd = view.findViewById(R.id.mailEd);
        phoneEd = view.findViewById(R.id.phoneEd);
        pwdEd = view.findViewById(R.id.pwd1Ed);
        dobEd = view.findViewById(R.id.dobEd);
        Button button = view.findViewById(R.id.update);
        final LoginDatabase database = new LoginDatabase(getContext());
        Cursor cursor = database.getUser(PHONE+"");
        if (cursor.moveToFirst()) {
            String[] names = cursor.getString(1).split(" ", 2);
            fNameEd.setText(names[0]);
            lNameEd.setText(names[1]);
            mailEd.setText(cursor.getString(2));
            phoneEd.setText(cursor.getString(3));
            pwdEd.setText(cursor.getString(4));
            dobEd.setText(cursor.getString(5));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        Cursor cursor1 = database.getUser(phone);
                        if (cursor1.moveToFirst()){
                            new AlertDialog.Builder(getContext()).setTitle("Alert!")
                                    .setMessage("User with same number exists. Choose a different one!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            cursor1.close();
                        }
                        else {
                            database.updateMyDetail(name, mail, phone, PHONE + "", pwd, dob);
                            getDialog().dismiss();
                            Toast.makeText(getContext(), "Details updated!", Toast.LENGTH_SHORT).show();
                            AllUserFragment users = (AllUserFragment) DialogFragment.this.getParentFragment();
                            users.refresh();
                        }
                    }
                }
            });
        }
        return view;
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
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setNumber(long number){
        PHONE = number;
    }

}
