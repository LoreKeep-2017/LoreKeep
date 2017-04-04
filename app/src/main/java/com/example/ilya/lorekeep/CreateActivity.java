package com.example.ilya.lorekeep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.DAO.HelperFactory;
import com.example.ilya.lorekeep.DAO.LinkInfo;

import java.sql.SQLException;

public class CreateActivity extends AppCompatActivity {

    private String title;
    private String link;
    private String content;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final EditText titleEdit = (EditText) findViewById(R.id.enterTitle);
        final EditText linkEdit = (EditText) findViewById(R.id.enterLink);
        final EditText contentEdit = (EditText) findViewById(R.id.enterContent);

        button = (Button)findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title = titleEdit.getText().toString();
                link = linkEdit.getText().toString();
                content = contentEdit.getText().toString();

                if(title.isEmpty() || link.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please, fill Title and Link filed", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        LinkInfo newLink = new LinkInfo();
                        newLink.setLinkTitle(title);
                        newLink.setLinkDecription(content);
                        newLink.setLink(link);
                        HelperFactory.getHelper().getLinkInfoDao().setNewLink(newLink);
                    } catch(SQLException e){
                        Log.e("in create link", "error craeting link");
                    }
                    finish();

                }

            }
        });

    }
}
