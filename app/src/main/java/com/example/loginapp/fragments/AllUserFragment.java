package com.example.loginapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.example.loginapp.MainActivity;
import com.example.loginapp.R;
import com.example.loginapp.database.LoginDatabase;
import com.example.loginapp.database.Metadata;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.util.ArrayList;


public class AllUserFragment extends Fragment {

    private DialogFragment fragment;
    private AlertDialog.Builder dialog;
    private Cursor cursor;
    private LoginDatabase db;
    private SimpleCursorAdapter cursorAdapter;
    private SwipeActionAdapter adapter;

    public AllUserFragment() { }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_user, container, false);
        db = new LoginDatabase(inflater.getContext());
        cursor = db.getAllUsers(MainActivity.getUserPhone());
        cursor.moveToFirst();
        ListView userList = v.findViewById(R.id.userList);
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
                    final Cursor cursor1 = (Cursor) adapter.getItem(listItemPosition);
                    fragment = new DialogFragment();
//                    dialog = new AlertDialog.Builder(getContext())
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    db.deleteUser(cursor1.getLong(3)+"");
//                                    dialog.dismiss();
//                                    Toast.makeText(getContext(), "User deleted!", Toast.LENGTH_SHORT).show();
//                                    refresh();
//                                }
//                            })
//                            .setNegativeButton("No", null)
//                            .setTitle("Alert").setMessage("Are you sure you want to delete the user?")
//                            .setCancelable(false);

                    switch (swipeDirection){

                        case DIRECTION_NORMAL_LEFT:
                            fragment.setNumber(cursor1.getLong(3));
                            fragment.show(getChildFragmentManager(), "edit_dialog");
                            //cursor1.close();

                            break;

                        case DIRECTION_FAR_LEFT:
                            fragment.setNumber(cursor1.getLong(3));
                            fragment.show(getChildFragmentManager(), "edit_dialog");
                            //cursor1.close();

                            //break;

//                        case DIRECTION_NORMAL_RIGHT:
//                            dialog.show();
//
//                            break;
//
//                        case DIRECTION_FAR_RIGHT:
//                            dialog.show();
                    }

                }
            }
        });
        userList.setAdapter(adapter);
        userList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        userList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int count = 0;
            Cursor temp;
            ArrayList<String> arrayList = new ArrayList<>();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                ++count;
                arrayList.add(position+"");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.contextualmenu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.contextdel){
                    new AlertDialog.Builder(getContext())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int t = 0; t < count; t++){
                                        temp = (Cursor) adapter.getItem(Integer.parseInt(arrayList.get(t)));
                                        db.deleteUser(temp.getLong(3)+"");
                                    }
                                    temp.close();
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), count+" user/users deleted", Toast.LENGTH_SHORT).show();
                                    mode.finish();
                                    refresh();
                                    count = 0;
                                    arrayList.clear();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mode.finish();
                                }
                            })
                            .setTitle("Alert").setMessage("Click on yes to proceed")
                            .setCancelable(false).show();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) { }
        });
        return v;
    }

    public void refresh(){
        cursorAdapter.changeCursor(db.getAllUsers(MainActivity.getUserPhone()));
        adapter.notifyDataSetChanged();
    }
}