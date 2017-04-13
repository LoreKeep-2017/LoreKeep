package com.example.ilya.lorekeep.topic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.SingleFragmentActivity;

public class TopicActivity extends SingleFragmentActivity{

    @Override
    public Fragment createFragment() {
        return TopicFragment.newInstance();
    }

}
