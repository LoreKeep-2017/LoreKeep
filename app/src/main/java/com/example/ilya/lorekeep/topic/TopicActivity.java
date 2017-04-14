package com.example.ilya.lorekeep.topic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.SingleFragmentActivity;

public class TopicActivity extends SingleFragmentActivity{

    @Override
    public Fragment createFragment() {
        return TopicFragment.newInstance();
    }

}
