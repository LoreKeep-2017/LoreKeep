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

    private static final int MY_PERMISSION_READ_STORAGE = 1;

    @Override
    @TargetApi(23)
    public Fragment createFragment() {
        int buffer = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("topic fragment", "" + buffer);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("topic fragment", "permission != check self");
            this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_READ_STORAGE);

        }
        return TopicFragment.newInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("topic activity", grantResults[0] + " " + requestCode + " " + PackageManager.PERMISSION_GRANTED);
        switch(requestCode){
            case MY_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("topic fragment", "in case read_storage");

                } else {

                }
        }
    }

}
