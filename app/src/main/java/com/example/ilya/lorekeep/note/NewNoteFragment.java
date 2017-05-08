package com.example.ilya.lorekeep.note;

import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;


public class NewNoteFragment extends DialogFragment implements View.OnClickListener{

    private String title;
    private String link;
    private String content;
    private Button button;
    private String TAG = "CreateNoteFragment";
    private Integer topicId;

    private EditText titleEdit;
    private EditText linkEdit;
    private EditText contentEdit;

    public static NewNoteFragment newInstance(int topicId) {
        NewNoteFragment f = new NewNoteFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("topicId", topicId);
        f.setArguments(args);

        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_note_create, null);
        builder.setView(view);


        topicId = getArguments().getInt("topicId");

        this.titleEdit = (EditText) view.findViewById(R.id.enterTitle);
        this.linkEdit = (EditText) view.findViewById(R.id.enterLink);
        this.contentEdit = (EditText) view.findViewById(R.id.enterContent);
        // Остальной код
        button = (Button) view.findViewById(R.id.create);

        button.setOnClickListener(this);

        return builder.create();
    }

    // 3. capture the clicks and respond depending on which view
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create:
                // do something
                title = this.titleEdit.getText().toString();
                link = this.linkEdit.getText().toString();
                content = this.contentEdit.getText().toString();

                if (title.isEmpty() || link.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please, fill all filed", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onClick: title content link"+ title+content+link);
                    ExecutorCreateNote.getInstance().setTopicId(topicId);
                    ExecutorCreateNote.getInstance().create(title, content, link);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }
}
