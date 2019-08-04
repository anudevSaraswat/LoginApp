package com.example.loginapp.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import com.example.loginapp.database.Metadata;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;


public class AllUserFragment extends Fragment {

    private DialogFragment fragment;
    private AlertDialog.Builder dialog;
    private Cursor cursor;
    private LoginDatabase db;
    private SimpleCursorAdapter cursorAdapter;
    private SwipeActionAdapter adapter;

    public AllUserFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_user, container, false);
        db = new LoginDatabase(inflater.getContext());
        cursor = db.getAllUsers(MainActivity.getUserPhone());
        cursor.moveToFirst();
        final ListView userList = v.findViewById(R.id.userList);
        dialog = new AlertDialog.Builder(inflater.getContext())
        .setPositiveButton("Yes", null).setNegativeButton("No", null)
        .setTitle("Delete this user?").setCancelable(false);
        cursorAdapter = new SimpleCursorAdapter(inflater.getContext(),
                R.layout.list_item, cursor, new String[]{Metadata.NAME, Metadata.PHONE},
                new int[]{R.id.nameTv, R.id.phoneTv});
        adapter = new SwipeActionAdapter(cursorAdapter);
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
                for (int i = 0; i < position.length; i++){
                    int listItemPosition = position[i];
                    SwipeDirection swipeDirection = direction[i];
                    Cursor cursor1;
                    fragment = new DialogFragment();

                    switch (swipeDirection){

                        case DIRECTION_NORMAL_LEFT:
                            cursor1 = (Cursor) adapter.getItem(listItemPosition);
                            fragment.setNumber(cursor1.getLong(3));
                            fragment.show(getChildFragmentManager(), "edit_dialog");

                            break;

                        case DIRECTION_FAR_LEFT:
                            cursor1 = (Cursor) adapter.getItem(listItemPosition);
                            fragment.setNumber(cursor1.getLong(3));
                            fragment.show(getChildFragmentManager(), "edit_dialog");

                            break;

                        case DIRECTION_NORMAL_RIGHT:
                            dialog.show();

                            break;

                        case DIRECTION_FAR_RIGHT:
                            dialog.show();
                    }

                }
            }
        });
        userList.setAdapter(adapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView = view.findViewById(R.id.idtv);
                //int u_id = Integer.parseInt(textView.getText().toString());
            }
        });
        return v;
    }

    public SimpleCursorAdapter getAdapter(){
        return cursorAdapter;
    }
}