package com.example.loginapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import com.example.loginapp.database.Metadata;


public class AllUserFragment extends Fragment {


    public AllUserFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_user, container, false);
        LoginDatabase db = new LoginDatabase(inflater.getContext());
        Cursor cursor = db.getAllUsers();
        ListView userList = v.findViewById(R.id.userList);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(inflater.getContext(), R.layout.list_item, cursor,
                new String[]{Metadata._ID, Metadata.NAME, Metadata.MAIL}, new int[]{R.id.idtv, R.id.nametv, R.id.mailtv});
        userList.setAdapter(cursorAdapter);
        return v;
    }
}

