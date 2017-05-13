package com.example.ilya.lorekeep.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;
import com.example.ilya.lorekeep.dbexecutor.ExecutorGetAllNotes;
import com.example.ilya.lorekeep.note.dao.Note;
import com.j256.ormlite.stmt.query.Not;

import java.util.List;
import java.util.Map;

import static com.example.ilya.lorekeep.R.styleable.RecyclerView;
import static com.example.ilya.lorekeep.R.styleable.Toolbar;

public class NoteActivity extends AppCompatActivity {

    static final int DROP_DESCRIPTION = 1;
    static final String DROP_DESCRIPTION_DICK = "dick";

    public static final String GET_NOTES_BY_TOPIC_ID =
            "topic_id_for_note";

    private Intent intent;
//    private List<Note> notes = NoteList.getInstance().notes;
private Map<Integer, List<Note>> mlNotes = NoteList.getInstance().mlNotes;
    private String TAG = "NoteActivity";
    private Integer topicId;
    private boolean isPressed = false;
    private String lastContent;
    private RecyclerView recyclerView;

    public static Intent newIntent(Context packageContext, Integer topicId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(GET_NOTES_BY_TOPIC_ID, topicId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Postgre SQL");
        setSupportActionBar(toolbar);

        topicId = getIntent().getIntExtra(GET_NOTES_BY_TOPIC_ID, -1);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                NewNoteFragment noteDialogFragment = NewNoteFragment.newInstance(topicId);
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
                Note note = mlNotes.get(topicId).get(position);
                ((ItemViewHolder) holder).bind(note);
            }

            @Override
            public int getItemCount() {
                if(mlNotes.get(topicId) == null)
                    return 0;
                else
                    return mlNotes.get(topicId).size();
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
        ExecutorGetAllNotes.getInstance().setTopicId(topicId);
        ExecutorGetAllNotes.getInstance().load();
        Log.d(TAG, "onResume: create new thread and execute!");
    }


    @Override
    public void onDestroy(){
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

    private void onNotesLoaded(){
        if (mlNotes.get(topicId) == null){
            //TODO: smth
        } else {
            Log.e(TAG, "onNotesLoaded: callback, cache size: "+mlNotes.get(topicId).size() );
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void onNoteCreate(){
        Log.e(TAG, "onNoteCreate: callback, cache size: "+mlNotes.get(topicId).size() );
        recyclerView.getAdapter().notifyItemInserted(mlNotes.get(topicId).size());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

//        private final TextView link;
//        private final TextView content;
//        private final TextView comment;

        private Note mNote = new Note();
        private final TextView noteInList;

        public ItemViewHolder(View itemView) {
            super(itemView);
            noteInList = (TextView) itemView.findViewById((R.id.note_in_list));
//            this.content = (TextView) itemView.findViewById(R.id.content);
//            this.link = (TextView) itemView.findViewById(R.id.link);
//            this.comment = (TextView) itemView.findViewById(R.id.comment);

            final View  b = findViewById(R.id.loading_description_note);


            noteInList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    b.setVisibility(View.INVISIBLE);
                    FrameLayout eventPopup = (FrameLayout) findViewById(R.id.loading_description_note);

                    if(isPressed){
                        getSupportFragmentManager().beginTransaction().
                                remove(getSupportFragmentManager().findFragmentById(R.id.loading_description_note)).commit();
                        isPressed = false;
                        if(!lastContent.equals(mNote.getNoteContent())){

                            if (eventPopup.getVisibility() == View.VISIBLE)
                                eventPopup.setVisibility(View.GONE);
                            else
                                eventPopup.setVisibility(View.VISIBLE);

                            FragmentManager manager =  getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            Fragment dropdownFragment = DropdownFragment.newInstance(mNote);
//                    dropdownFragment.setTargetFragment(NoteActivity.class, DROP_DESCRIPTION);
                            transaction.add(R.id.loading_description_note, dropdownFragment, DROP_DESCRIPTION_DICK);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            isPressed = true;
                    }

                        lastContent = mNote.getNoteContent();
                    }else{
                        lastContent = mNote.getNoteContent();

                        if (eventPopup.getVisibility() == View.VISIBLE)
                            eventPopup.setVisibility(View.GONE);
                        else
                            eventPopup.setVisibility(View.VISIBLE);
                        FragmentManager manager =  getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Fragment dropdownFragment = DropdownFragment.newInstance(mNote);
//                    dropdownFragment.setTargetFragment(NoteActivity.class, DROP_DESCRIPTION);
                        transaction.add(R.id.loading_description_note, dropdownFragment, DROP_DESCRIPTION_DICK);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }

        public void bind(Note note) {

            if(note.getNoteComment() != null)
                noteInList.setText(note.getNoteComment());
            else
                noteInList.setText(note.getNoteContent());

            mNote = note;

        }
    }
}
