package com.example.loginapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import com.example.loginapp.database.Metadata;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;


public class AllUserFragment extends Fragment {


    public AllUserFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_user, container, false);
        LoginDatabase db = new LoginDatabase(inflater.getContext());
        Cursor cursor = db.getAllUsers();
        cursor.moveToFirst();
        ListView userList = v.findViewById(R.id.userList);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(inflater.getContext(),
                R.layout.list_item, cursor, new String[]{Metadata._ID, Metadata.NAME, Metadata.MAIL},
                new int[]{R.id.idtv, R.id.nametv, R.id.mailtv});
        SwipeActionAdapter adapter = new SwipeActionAdapter(cursorAdapter);
        adapter.setListView(userList);
        adapter.addBackground(SwipeDirection.DIRECTION_FAR_LEFT, R.layout.row_bg_left_far)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left)
                .addBackground(SwipeDirection.DIRECTION_FAR_RIGHT, R.layout.row_bg_right_far)
                .addBackground(SwipeDirection.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
        adapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int position, SwipeDirection direction) {
                if (direction.isLeft()) return true;
                if (direction.isRight()) return true;
                return false;
            }

            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction) {
                return false;
            }

            @Override
            public void onSwipe(int[] position, SwipeDirection[] direction) {

            }
        });
        userList.setAdapter(adapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.idtv);
                int u_id = Integer.parseInt(textView.getText().toString());
            }
        });
        return v;
    }
}