package com.example.ilya.lorekeep.topic;

import android.support.v4.app.Fragment;

import com.example.ilya.lorekeep.SingleFragmentActivity;

public class NewTopicActivity extends SingleFragmentActivity{
    @Override
    public Fragment createFragment() {
        return NewTopicFragment.newInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
