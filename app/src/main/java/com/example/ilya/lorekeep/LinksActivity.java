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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.linksLayout, new LinkFragment())
                        .commitAllowingStateLoss();
            }
        });
//        Button button = (Button)findViewById(R.id.addButton);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                 addFragment();
//            }
//        });
        addFragment();
    }

    private void addFragment(){
        for(int i = 0; i < 10; i++) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.linksLayout, new LinkFragment())
                    .commitAllowingStateLoss();
        }
    }

}
