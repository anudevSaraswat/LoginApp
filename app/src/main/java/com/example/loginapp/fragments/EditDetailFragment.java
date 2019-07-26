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

    private TextInputEditText fnameted;
    private TextInputEditText lnameted;
    private TextInputEditText dobted;
    private TextInputEditText mailted;
    private TextInputEditText phoneted;
    private TextInputEditText pwdted;
    private LoginDatabase database;
    private Context context;
    private String PHONE;

    public EditDetailFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_detail, container, false);
        context = inflater.getContext();
        database = new LoginDatabase(context);
        fnameted = v.findViewById(R.id.firstName);
        lnameted = v.findViewById(R.id.lastName);
        mailted = v.findViewById(R.id.mail);
        phoneted = v.findViewById(R.id.phone);
        pwdted = v.findViewById(R.id.pwd);
        dobted = v.findViewById(R.id.dob);
        Button button = v.findViewById(R.id.update);
        Cursor cursor = MainActivity.getCursor();
        String[] names = cursor.getString(1).split(" ", 2);
        fnameted.setText(names[0]);
        lnameted.setText(names[1]);
        mailted.setText(cursor.getString(2));
        phoneted.setText(cursor.getString(3));
        PHONE = cursor.getString(3);
        pwdted.setText(cursor.getString(4));
        dobted.setText(cursor.getString(5));
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
                database.updateUser(fnameted.getText().toString()+" "+lnameted.getText().toString(), mailted.getText().toString(), phoneted.getText().toString(),
                        PHONE, pwdted.getText().toString(), dobted.getText().toString());
                Toast.makeText(context, "Details updated!", Toast.LENGTH_SHORT).show();
                MainActivity.setCursor(database.getUser(Long.parseLong(phoneted.getText().toString())));
        }
    }
}