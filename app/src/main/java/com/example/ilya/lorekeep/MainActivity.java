package com.example.ilya.lorekeep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteActivity;
import com.example.ilya.lorekeep.topic.TopicActivity;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.buttonToLinks);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });

        //auth vk
        //VKSdk.login(MainActivity.this, VKUtil.getCertificateFingerprint(this, this.getPackageName()));
        //VKSdk.initialize(MainActivity.this);



    }

    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

}
