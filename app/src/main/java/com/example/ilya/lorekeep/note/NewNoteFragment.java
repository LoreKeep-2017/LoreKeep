package com.example.ilya.lorekeep.note;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;


public class NewNoteFragment extends Fragment {

    private String title;
    private String link;
    private String content;

    private String TAG = "CreateNoteFragment";
    private Integer topicId;

    private EditText titleEdit;
    private EditText linkEdit;
    private EditText contentEdit;
    private TextView mCreateNote;

    public static NewNoteFragment newInstance(int topicId) {
        NewNoteFragment f = new NewNoteFragment();

        Bundle args = new Bundle();
        args.putInt("topicId", topicId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_create, container,
                false);

        topicId = getArguments().getInt("topicId");

        this.titleEdit = (EditText) v.findViewById(R.id.title_note);
        this.linkEdit = (EditText) v.findViewById(R.id.url_note);
        this.contentEdit = (EditText) v.findViewById(R.id.content_note);
        // Остальной код


        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar_new_note);
        mCreateNote = (TextView) toolbar.findViewById(R.id.toolbar_note_create);

        mCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.toolbar_note_create:
                            // do something
                            title = titleEdit.getText().toString();
                            link = linkEdit.getText().toString();
                            content = contentEdit.getText().toString();

                            if (title.isEmpty() || content.isEmpty()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Please, fill all filed", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onClick: title content link" + title + content + link);
                                ExecutorCreateNote.getInstance().setTopicId(topicId);
                                ExecutorCreateNote.getInstance().create(title, content, link);


                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();


                                NewNoteFragment noteDialogFragment = (NewNoteFragment) manager.findFragmentById(R.id.fragment_note_create);

                                transaction.remove(noteDialogFragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                                transaction.commit();
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.d("Error", e.toString());
                }
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return v;
    }

}
