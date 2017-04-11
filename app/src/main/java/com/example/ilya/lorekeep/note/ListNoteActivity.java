package com.example.ilya.lorekeep.note;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.note.notefragment.MoreNoteInfoFragment;

import java.util.ArrayList;

public class ListNoteActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private String title;
    private String content;
    private int childCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // title = (TextView) findViewById(R.id.title);
        // content = (TextView) findViewById(R.id.content);
        mViewPager = (ViewPager) findViewById(R.id.linkViewPager);

        // title.setText(getIntent().getStringExtra("title"));
        // content.setText(getIntent().getStringExtra("content"));
        childCount = getIntent().getIntExtra("childCount", 10);

        Log.d("in on", "in ViewPager");

        FragmentManager manager = getSupportFragmentManager();
        Manager manager1 = new Manager(manager);
        mViewPager.setAdapter(manager1);
        mViewPager.setCurrentItem(0);
    }

    private class Manager extends FragmentPagerAdapter{

        private ArrayList<MoreNoteInfoFragment> data;

        Manager(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MoreNoteInfoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return childCount;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return "Title " + position;
        }

    }
}
