package com.example.loginapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.loginapp.fragments.AboutFragment;
import com.example.loginapp.fragments.AllUserFragment;
import com.example.loginapp.fragments.EditDetailFragment;
import com.example.loginapp.fragments.HomeFragment;

public class Main2Activity extends AppCompatActivity implements ListView.OnItemClickListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String actionBarText;
    private ActionBar bar;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        manager = getSupportFragmentManager();
        listView = findViewById(R.id.drawerList);
        listView.setOnItemClickListener(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.listItems));
        listView.setAdapter(adapter);
        if (savedInstanceState == null){
            actionBarText = "LoginApp";
            manager.beginTransaction().replace(R.id.drawerContainer, new HomeFragment(), "visibleFrag").addToBackStack(null).commit();
        }
        else{
            actionBarText = savedInstanceState.getString("actionBarText");
            bar.setTitle(actionBarText);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                bar.setTitle("LoginApp");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                bar.setTitle(actionBarText);
            }

        };
        drawerLayout.addDrawerListener(drawerToggle);
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (drawerLayout.isDrawerOpen(listView))
                    drawerLayout.closeDrawer(listView);
                Fragment fragment = manager.findFragmentByTag("visibleFrag");
                if (fragment instanceof AllUserFragment)
                    actionBarText = "Registered users";

                if (fragment instanceof EditDetailFragment)
                    actionBarText = "Edit details";

                if (fragment instanceof AboutFragment)
                    actionBarText = "About";

                if (fragment instanceof HomeFragment)
                    actionBarText = "LoginApp";

                if (fragment == null){
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.drawerContainer, new HomeFragment(), "visibleFrag");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    Toast.makeText(Main2Activity.this, "already on first page!", Toast.LENGTH_SHORT).show();
                    actionBarText = "LoginApp";
                }
                bar.setTitle(actionBarText);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Fragment fragment = null;
        transaction = manager.beginTransaction();
        int h = 0;

        switch (position){
            case 0:
                fragment = new AllUserFragment();
                h = 1;

                break;

            case 1:
                fragment = new EditDetailFragment();
                h = 1;

                break;

            case 2:
                fragment = new AboutFragment();
                h = 1;

                break;

            case 3:
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

        }

        if (h == 1) {
            transaction.replace(R.id.drawerContainer, fragment, "visibleFrag");
            transaction.addToBackStack(null);
            drawerLayout.closeDrawer(listView);
            transaction.commit();
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("actionBarText", actionBarText);
    }
}