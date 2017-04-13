package com.example.ilya.lorekeep.note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.dbexecutor.executorCreateNote;
import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.R;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateNoteActivity extends AppCompatActivity {

    private String title;
    private String link;
    private String content;
    private Button button;
    private String TAG = "CreateNoteActivity";

    private final Executor executor = Executors.newCachedThreadPool();
    private executorCreateNote executorCreateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final EditText titleEdit = (EditText) findViewById(R.id.enterTitle);
        final EditText linkEdit = (EditText) findViewById(R.id.enterLink);
        final EditText contentEdit = (EditText) findViewById(R.id.enterContent);

        button = (Button) findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title = titleEdit.getText().toString();
                link = linkEdit.getText().toString();
                content = contentEdit.getText().toString();

                if (title.isEmpty() || link.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please, fill Title and Link filed", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onClick: title content link"+ title+content+link);
                    executorCreateNote = new executorCreateNote(title, content, link);
                    executor.execute(executorCreateNote);
                    Log.d(TAG, "onClick: createNote");
                    finish();

                }

            }
        });

    }
}
