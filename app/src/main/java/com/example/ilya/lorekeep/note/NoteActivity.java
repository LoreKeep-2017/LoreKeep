package com.example.ilya.lorekeep.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;
import com.example.ilya.lorekeep.dbexecutor.ExecutorGetAllNotes;
import com.example.ilya.lorekeep.note.dao.Note;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private Intent intent;
    private List<Note> notes = NoteList.getInstance().notes;
    private String TAG = "NoteActivity";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Postgre SQL");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                FragmentManager manager = getSupportFragmentManager();
                NoteDialogFragment noteDialogFragment = new NoteDialogFragment();
                noteDialogFragment.show(manager, "noteDialogFragment");
            }
        });

        HelperFactory.setHelper(getApplicationContext());



        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(
                        getLayoutInflater().inflate(R.layout.note_item, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                Note note = noteList.get(position);
                Note note = notes.get(position);
                ((ItemViewHolder) holder).bind(note);
            }

            @Override
            public int getItemCount() {
                return notes.size();
//                return noteList.size();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExecutorGetAllNotes.getInstance().setCallback(new ExecutorGetAllNotes.Callback() {
            @Override
            public void onLoaded() {
                onNotesLoaded();
            }
        });

        ExecutorCreateNote.getInstance().setCallback(new ExecutorCreateNote.Callback() {
            @Override
            public void onCreate() {
                onNoteCreate();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ExecutorGetAllNotes.getInstance().load();
        Log.d(TAG, "onResume: create new thread and execute!");
    }


    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

    private void onNotesLoaded(){
        if (notes == null){
            //TODO: smth
        } else {
            Log.e(TAG, "onNotesLoaded: callback, cache size: "+notes.size() );
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    public void onNoteCreate(){
        Log.e(TAG, "onNoteCreate: callback, cache size: "+notes.size() );
        recyclerView.getAdapter().notifyItemInserted(notes.size());

    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView link;
        private final TextView content;
        private final TextView comment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.link = (TextView) itemView.findViewById(R.id.link);
            this.comment = (TextView) itemView.findViewById(R.id.comment);
        }

        public void bind(Note note) {
            content.setText(note.getNoteContent());
            link.setText(note.getNoteUrl());
            comment.setText(note.getNoteComment());
        }

    }

}
