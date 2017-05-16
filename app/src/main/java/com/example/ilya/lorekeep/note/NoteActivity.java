package com.example.ilya.lorekeep.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;
import com.example.ilya.lorekeep.dbexecutor.ExecutorGetAllNotes;
import com.example.ilya.lorekeep.note.dao.Note;

import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    public static final String GET_NOTES_BY_TOPIC_ID =
            "topic_id_for_note";

    private Map<Integer, List<Note>> mlNotes = NoteList.getInstance().mlNotes;
    private String TAG = "NoteActivity";
    private Integer topicId;
    private RecyclerView recyclerView;
    private TextView mTextView;
    private Button mButtonUrl;
    private boolean isItemPressed = false;
    private Note mPriviousItem = null;
    private LinearLayout mPriviousScrollView;

    public static Intent newIntent(Context packageContext, Integer topicId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(GET_NOTES_BY_TOPIC_ID, topicId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (bundle != null) {
            toolbar.setTitle(bundle.getString("topic"));
        }
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
                if (mlNotes.get(topicId) == null)
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
    public void onDestroy() {
        HelperFactory.releaseHelper();
        super.onDestroy();
    }

    private void onNotesLoaded() {
        if (mlNotes.get(topicId) == null) {
            //TODO: smth
        } else {
            Log.e(TAG, "onNotesLoaded: callback, cache size: " + mlNotes.get(topicId).size());
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void onNoteCreate() {
        Log.e(TAG, "onNoteCreate: callback, cache size: " + mlNotes.get(topicId).size());
        recyclerView.getAdapter().notifyItemInserted(mlNotes.get(topicId).size());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private Note mNote = new Note();
        private final TextView noteInList;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            noteInList = (TextView) itemView.findViewById((R.id.note_in_list));

            final LinearLayout dropdownView = (LinearLayout) itemView.findViewById(R.id.loading_description_note);
            final ScrollView scrollView = (ScrollView) itemView.findViewById(R.id.scroll_view);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Disallow the touch request for parent scroll on touch of child view
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            noteInList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (isItemPressed) {
                        if (mPriviousItem.getNoteId() == mNote.getNoteId()) {
                            dropdownView.setVisibility(View.GONE);
                            isItemPressed = false;
                        } else {
                            mPriviousScrollView.setVisibility(View.GONE);
                            dropdownView.setVisibility(View.VISIBLE);
                            isItemPressed = true;
                            mPriviousItem = mNote;
                            mPriviousScrollView = dropdownView;

                            if (mNote.getNoteUrl() != null) {
                                mButtonUrl = (Button) dropdownView.findViewById(R.id.button_open_browser);
                                mButtonUrl.setText(mNote.getNoteUrl());
                            }

                            mTextView = (TextView) dropdownView.findViewById(R.id.fragment_test);
                            mTextView.setText(mNote.getNoteComment());

                        }
                    } else {
                        dropdownView.setVisibility(View.VISIBLE);
                        isItemPressed = true;
                        mPriviousItem = mNote;
                        mPriviousScrollView = dropdownView;
                        if (mNote.getNoteUrl() != null) {
                            mButtonUrl = (Button) dropdownView.findViewById(R.id.button_open_browser);
                            mButtonUrl.setText(mNote.getNoteUrl());
                        }

                        mTextView = (TextView) dropdownView.findViewById(R.id.fragment_test);
                        mTextView.setText(mNote.getNoteComment());
                    }
                }
            });
        }

        public void bind(Note note) {

            if (note.getNoteComment() != null)
                noteInList.setText(note.getNoteContent());
            else
                noteInList.setText(note.getNoteComment());

            mNote = note;
        }
    }
}
