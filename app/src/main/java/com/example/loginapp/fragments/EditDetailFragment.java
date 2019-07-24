package com.example.loginapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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


public class EditDetailFragment extends Fragment implements View.OnClickListener{

    private TextInputEditText dobted;
    private Context context;
    private String name;
    private String mail;
    private long phone;
    private String pwd;
    private String dob;

    public EditDetailFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_detail, container, false);
        context = inflater.getContext();
        Cursor cursor = MainActivity.getCursor();
        cursor.moveToFirst();
        TextInputEditText fnameted = v.findViewById(R.id.firstName);
        TextInputEditText lnameted = v.findViewById(R.id.lastName);
        TextInputEditText mailted = v.findViewById(R.id.mail);
        TextInputEditText phoneted = v.findViewById(R.id.phone);
        TextInputEditText pwdted = v.findViewById(R.id.pwd);
        dobted = v.findViewById(R.id.dob);
        Button button = v.findViewById(R.id.update);
        String[] names = cursor.getString(1).split(" ", 2);
        name = cursor.getString(1);
        mail = cursor.getString(2);
        phone = cursor.getLong(3);
        pwd = cursor.getString(4);
        dob = cursor.getString(5);
        fnameted.setText(names[0]);
        lnameted.setText(names[1]);
        mailted.setText(mail);
        phoneted.setText(phone+"");
        pwdted.setText(pwd);
        dobted.setText(dob);
        dobted.setOnClickListener(this);
        button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.dob:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int monthh = month +1;
                        dobted.setText(dayOfMonth+"/"+monthh+"/"+year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();


                break;

            case R.id.update:
                LoginDatabase database = new LoginDatabase(context);
                database.updateUser(name, mail, phone, pwd, dob);
                Toast.makeText(context, "Details updated!", Toast.LENGTH_SHORT).show();
        }
    }
}
