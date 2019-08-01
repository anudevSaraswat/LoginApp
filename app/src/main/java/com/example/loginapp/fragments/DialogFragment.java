package com.example.loginapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;


public class DialogFragment extends android.support.v4.app.DialogFragment {

    private int id;

    public DialogFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_my_detail, container);
        TextInputEditText fNameEd = v.findViewById(R.id.fNameEd);
        TextInputEditText lNameEd = v.findViewById(R.id.lNameEd);
        TextInputEditText mailEd = v.findViewById(R.id.mailEd);
        TextInputEditText phoneEd = v.findViewById(R.id.phoneEd);
        TextInputEditText pwdEd = v.findViewById(R.id.pwd1Ed);
        TextInputEditText dobEd = v.findViewById(R.id.dobEd);
        LoginDatabase database = new LoginDatabase(inflater.getContext());
        Cursor cursor = database.getUser1(id);
        cursor.moveToFirst();
        String[] names = cursor.getString(1).split(" ", 2);
        fNameEd.setText(names[0]);
        lNameEd.setText(names[1]);
        mailEd.setText(cursor.getString(2));
        phoneEd.setText(cursor.getString(3));
        pwdEd.setText(cursor.getString(4));
        dobEd.setText(cursor.getString(5));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setId(int i){
        id = i;
    }

}
