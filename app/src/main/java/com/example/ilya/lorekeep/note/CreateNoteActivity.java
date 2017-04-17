package com.example.ilya.lorekeep.note;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;


public class CreateNoteActivity extends AppCompatActivity {

    private String title;
    private String link;
    private String content;
    private Button button;
    private String TAG = "CreateNoteActivity";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
                    ExecutorCreateNote.getInstance().create(title, content, link);
                    finish();

                }
            }
        });

    }
}
