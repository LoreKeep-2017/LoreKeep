package com.example.ilya.lorekeep;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.ilya.lorekeep.auth.FragmentAdapter;

public class LoginActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewPager = (ViewPager) findViewById(R.id.vpPager);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    public void setPage(int position){
        viewPager.setCurrentItem(position);
    }

}
