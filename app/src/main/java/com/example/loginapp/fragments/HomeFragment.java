package com.example.loginapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import com.example.loginapp.database.Metadata;


public class HomeFragment extends Fragment {

    private LoginDatabase loginDatabase;
    private Context context;
    private TextView phone;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        loginDatabase = new LoginDatabase(context);
        Cursor cursor = loginDatabase.getUser(MainActivity.getUserPhone());
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView name = v.findViewById(R.id.welcomename);
        TextView mail = v.findViewById(R.id.mailTv);
        phone = v.findViewById(R.id.phoneTv);
        TextView pwd = v.findViewById(R.id.pwdTv);
        TextView dob = v.findViewById(R.id.dobTv);
        FloatingActionButton floatingActionButton = v.findViewById(R.id.floatButton);
        cursor.moveToFirst();
        name.setText(cursor.getString(cursor.getColumnIndex(Metadata.NAME)));
        mail.setText(cursor.getString(cursor.getColumnIndex(Metadata.MAIL)));
        phone.setText(cursor.getString(cursor.getColumnIndex(Metadata.PHONE)));
        pwd.setText(cursor.getString(cursor.getColumnIndex(Metadata.PASSWORD)));
        dob.setText(cursor.getString(cursor.getColumnIndex(Metadata.DOB)));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("Are you sure?").setMessage("You will be unregistered from this application")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loginDatabase.deleteUser(phone.getText().toString());
                                Intent i = new Intent(context, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                Toast.makeText(context, "Good Bye!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
            }
        });
        return v;
    }
}
