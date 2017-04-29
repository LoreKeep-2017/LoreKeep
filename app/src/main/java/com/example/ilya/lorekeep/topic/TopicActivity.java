package com.example.ilya.lorekeep.topic;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.SingleFragmentActivity;

public class TopicActivity extends SingleFragmentActivity {

    @Override
    @TargetApi(23)
    public Fragment createFragment() {
        return TopicFragment.newInstance();
    }

}
