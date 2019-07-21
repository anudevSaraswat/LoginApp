package com.example.loginapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.example.loginapp.database.Metadata;


public class HomeFragment extends Fragment {


    public HomeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Cursor cursor = MainActivity.getCursor();
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = v.findViewById(R.id.welcomename);
        cursor.moveToFirst();
        textView.setText(cursor.getString(cursor.getColumnIndex(Metadata.NAME)));
        return v;
    }

}
