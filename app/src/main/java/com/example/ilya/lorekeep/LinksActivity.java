package com.example.ilya.lorekeep;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.ilya.lorekeep.LinkFragment.LinkFragment;

public class LinksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Postgre SQL");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button button = (Button)findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                 addFragment();
            }
        });
    }

    private void addFragment(){
        getSupportFragmentManager()
                .beginTransaction()
//        LinkFragment link = (LinkFragment)manager.findFragmentById(R.id.linkFragment);
                .replace(R.id.linkFragment, new LinkFragment())
//        transaction.addToBackStack(null);
                .commitAllowingStateLoss();
        LinkFragment link = (LinkFragment)getSupportFragmentManager().findFragmentById(R.id.linkFragment);
        if(link == null){
            int a =0 ;
            a++;
        }
    }

}
