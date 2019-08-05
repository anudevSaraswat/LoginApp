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
import android.widget.Button;
import android.widget.Toast;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;


public class DialogFragment extends android.support.v4.app.DialogFragment {

    private long phone;
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
        Cursor cursor = database.getUser(phone+"");
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
                    database.updateMyDetail(fNameEd.getText().toString() + " " + lNameEd.getText().toString(),
                            mailEd.getText().toString(), phoneEd.getText().toString(), phone + "", pwdEd.getText().toString(), dobEd.getText().toString());
                    getDialog().dismiss();
                    Toast.makeText(getContext(), "Details updated!", Toast.LENGTH_SHORT).show();
                    AllUserFragment users = (AllUserFragment) DialogFragment.this.getParentFragment();
                    users.refresh();
                }
            });
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void setNumber(long number){
        phone = number;
    }

}
