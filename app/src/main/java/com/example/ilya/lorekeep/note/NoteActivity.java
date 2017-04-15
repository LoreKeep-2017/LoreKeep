package com.example.ilya.lorekeep.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.LruCache;
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
import com.example.ilya.lorekeep.dbexecutor.executorGetAllNotes;
import com.example.ilya.lorekeep.note.dao.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private Intent intent;
    private int initSize = -1;
    private List<Note> noteList = new ArrayList<Note>();
    private String TAG = "NoteActivity";

    //add cache
    private LruCache<Integer, Note> cache = new LruCache(32);
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
                intent = new Intent(NoteActivity.this, CreateNoteActivity.class);
                startActivity(intent);
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
                Note note = noteList.get(position);
                ((ItemViewHolder) holder).bind(note);
            }

            @Override
            public int getItemCount() {
                return noteList.size();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        executorGetAllNotes.getInstance().setCallback(new executorGetAllNotes.Callback() {
            @Override
            public void onLoaded(List<Note> noteList) {
                onNotesLoaded(noteList);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        //load all Notes in new thread

        executorGetAllNotes.getInstance().load();
        Log.d(TAG, "onResume: create new thread and execute!");
    }


    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

    private void onNotesLoaded(List<Note> noteList){
        if (noteList == null){
            //TODO: спросить какого уя в ресайкл вью ниче не добавляется
            //do smth
        } else {
            //TODO: спросить какого уя в ресайкл вью ниче не добавляется
            //do smth
            this.noteList = noteList;
            recyclerView.getAdapter().notifyItemInserted(noteList.size() - 1);
        }

    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView link;
        private final TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.link = (TextView) itemView.findViewById(R.id.link);
        }

        public void bind(Note note) {
            title.setText(note.getNoteTitle());
            link.setText(note.getNoteDescription());
        }

    }

}
