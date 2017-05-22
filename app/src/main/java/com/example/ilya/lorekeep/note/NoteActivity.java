package com.example.ilya.lorekeep.note;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;
import com.example.ilya.lorekeep.dbexecutor.ExecutorGetAllNotes;
import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.note.noteApi.NoteApi;
import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteModel;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class NoteActivity extends AppCompatActivity {

    public static final String GET_NOTES_BY_TOPIC_ID =
            "topic_id_for_note";

    private Map<Integer, List<Note>> mlNotes = NoteList.getInstance().mlNotes;
    private String TAG = "NoteActivity";
    private Integer topicId;
    private RecyclerView recyclerView;
    private TextView mTextView;
    private Button mButtonUrl;
    private ImageView mCreateNote;
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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (bundle != null) {
//            toolbar.setTitle(bundle.getString("topic"));
//        }
//        setSupportActionBar(toolbar);


        topicId = getIntent().getIntExtra(GET_NOTES_BY_TOPIC_ID, -1);

        Toolbar bottomToolbar = (Toolbar) findViewById(R.id.toolbar_bottom_activty_note) ;
        mCreateNote = (ImageView) bottomToolbar.findViewById(R.id.imageview_ceate_note);

        mCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                NewNoteFragment noteDialogFragment = NewNoteFragment.newInstance(topicId);
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_note_create, noteDialogFragment);
                transaction.commit();
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



        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.sharedTitle),
                Context.MODE_PRIVATE);
        final int userId = sharedPref.getInt(getString(R.string.userId), 1);
        String sessionId = sharedPref.getString(getString(R.string.sessionId), "");

        final NoteApi note = RetrofitFactory.retrofitLore().create(NoteApi.class);
        final Call<List<NoteModel>> callSyn = note.getChanges(topicId, "sessionId=" + sessionId);
        NetworkThread.getInstance().execute(callSyn, new NetworkThread.ExecuteCallback<List<NoteModel>>() {
            @Override
            public void onSuccess(List<NoteModel> result, Response<List<NoteModel>> response) {
                try {
                    Log.d("on update", "result size " + result.size());
                    try {
                        Topic topic = HelperFactory.getHelper().getTopicDAO().getTopic(topicId);

                        for (int i = 0; i < result.size(); ++i) {

                            Note mNote = new Note();
                            mNote.setTopic(topic);
                            mNote.setServerNoteId(result.get(i).getNoteId());
                            mNote.setNoteComment(result.get(i).getComment());
                            mNote.setNoteContent(result.get(i).getContent());
                            mNote.setNoteUrl(result.get(i).getUrl());

                            HelperFactory.getHelper().getNoteDao().setNewNote(mNote);

//                            Note isNote = HelperFactory.getHelper().getNoteDao().getNoteByServerTopicId(mNote.getServerNoteId());
//                            if (isNote == null) {
//                                HelperFactory.getHelper().getNoteDao().setNewNote(mNote);
//                            } else {
//                                isNote.setNoteComment(mNote.getNoteComment());
//                                isNote.setNoteContent(mNote.getNoteContent());
//                                isNote.setNoteUrl(mNote.getNoteUrl());
//                                HelperFactory.getHelper().getNoteDao().updateNote(isNote);
//                            }
                        }
                    }catch(SQLException e){
                        Log.d("Error", e.toString());
                    }


                } catch (Exception e) {
                    Log.e("on create", "fail to get query");
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.d("onError", "Error " + ex.toString());
            }
        });

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




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
                            mButtonUrl = (Button) dropdownView.findViewById(R.id.button_open_browser);

                            if (mNote.getNoteUrl() != null) {
                                if(mButtonUrl.getVisibility() != View.VISIBLE)
                                    mButtonUrl.setVisibility(View.VISIBLE);
                                mButtonUrl.setText(mNote.getNoteUrl());
                                mButtonUrl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String urlString=mNote.getNoteUrl();
                                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setPackage("com.android.chrome");
                                        try {
                                            startActivity(intent);
                                        } catch (ActivityNotFoundException ex) {
                                            // Chrome browser presumably not installed so allow user to choose instead
                                            intent.setPackage(null);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }else{
                                if(mButtonUrl.getVisibility() == View.VISIBLE)
                                    mButtonUrl.setVisibility(View.GONE);
                            }

                            mTextView = (TextView) dropdownView.findViewById(R.id.fragment_test);
                            mTextView.setText(mNote.getNoteComment());

                        }
                    } else {
                        dropdownView.setVisibility(View.VISIBLE);
                        mButtonUrl = (Button) dropdownView.findViewById(R.id.button_open_browser);
                        isItemPressed = true;
                        mPriviousItem = mNote;
                        mPriviousScrollView = dropdownView;
                        if (mNote.getNoteUrl() != null) {
                            if(mButtonUrl.getVisibility() != View.VISIBLE)
                                mButtonUrl.setVisibility(View.VISIBLE);
                            mButtonUrl.setText(mNote.getNoteUrl());
                            mButtonUrl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String urlString=mNote.getNoteUrl();
                                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setPackage("com.android.chrome");
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException ex) {
                                        // Chrome browser presumably not installed so allow user to choose instead
                                        intent.setPackage(null);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else{
                            if(mButtonUrl.getVisibility() == View.VISIBLE)
                                mButtonUrl.setVisibility(View.GONE);
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
