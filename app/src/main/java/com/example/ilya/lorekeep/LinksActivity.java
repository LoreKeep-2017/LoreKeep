package com.example.ilya.lorekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilya.lorekeep.DAO.HelperFactory;
import com.example.ilya.lorekeep.DAO.LinkInfo;
import com.example.ilya.lorekeep.LinkFragment.LinkFragment;

import java.sql.SQLException;
import java.util.List;

public class LinksActivity extends AppCompatActivity {

    private Intent intent;
    private int initSize = -1;
    private List<LinkInfo> linkList;

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
                intent = new Intent(LinksActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        HelperFactory.setHelper(getApplicationContext());

    }

    @Override
    public void onResume(){
        super.onResume();
        RecyclerView recycle = (RecyclerView)findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new RecycleAdapter();
        recycle.setAdapter(adapter);
        try {
            linkList = HelperFactory.getHelper().getLinkInfoDao().getAllLinks();
        } catch (SQLException e){
            Log.e("on resume", "error getting links");
        }
        if(initSize == -1){
            initSize = linkList.size();
        }
        if(initSize != linkList.size()) {
            Toast.makeText(getApplicationContext(), "New link succesfully added", Toast.LENGTH_SHORT).show();
        }
    }

    private class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View v) {
                super(v);

            }
        }

        private int i = 0;

        public RecycleAdapter() {

        }

        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(final ViewGroup outside, int viewType) {

            LinkFragment link = new LinkFragment();
            Log.d("viewType number", "number is " + viewType);
            View layout = link.CreateView(outside, linkList.get(i));
            layout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Intent intent = new Intent(LinksActivity.this, InfoActivity.class);
                    intent.putExtra("title", "OLOLOLOL Title OLololol0");
                    intent.putExtra("content", "OLOLOLLOLO Content OLOLOLOLOL");
                    intent.putExtra("childCount", outside.getChildCount());
                    startActivity(intent);
                }
            });
            ++i;
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){

        }

        @Override
        public int getItemCount(){
            return linkList.size();
        }
    }

    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

}
