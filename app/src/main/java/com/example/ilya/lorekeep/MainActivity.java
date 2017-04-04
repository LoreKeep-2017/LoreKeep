package com.example.ilya.lorekeep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ilya.lorekeep.DAO.GroupLink;
import com.example.ilya.lorekeep.DAO.HelperFactory;
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
                Intent intent = new Intent(MainActivity.this, LinksActivity.class);
                startActivity(intent);
            }
        });

        //auth vk
        VKSdk.login(MainActivity.this, VKUtil.getCertificateFingerprint(this, this.getPackageName()));
        VKSdk.initialize(MainActivity.this);

        HelperFactory.setHelper(getApplicationContext());

        try {
            List<GroupLink> links = HelperFactory.getHelper().getGroupLinkDAO().getAllGroups();
            Log.d("on create", "query lenght: " + links.size());
            HelperFactory.getHelper().getGroupLinkDAO().setLinkGroup();
            links = HelperFactory.getHelper().getGroupLinkDAO().getAllGroups();
            Log.d("on create", "query lenght: " + links.size());
        } catch (SQLException e){
            Log.e("on create", "fail to get query");
        }

    }

    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

}
