package com.example.ilya.lorekeep.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilya.lorekeep.R;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.config.NetworkThread;
import com.example.ilya.lorekeep.config.RetrofitFactory;
import com.example.ilya.lorekeep.dbexecutor.ExecutorCreateNote;
import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.note.noteApi.NoteApi;
import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteAnswer;
import com.example.ilya.lorekeep.note.noteApi.noteModels.NoteModel;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Response;


public class NewNoteFragment extends AppCompatActivity {

    private String title;
    private String link;
    private String content;

    private String TAG = "CreateNoteFragment";
    private Integer topicId;
    private Integer noteId;
    private Boolean isUpdate;

    private EditText titleEdit;
    private EditText linkEdit;
    private EditText contentEdit;
    private TextView mCreateNote;
    private ImageView mTakeImage;
    private ImageView photoLibaray;
    private ImageView trashNote;
    private Note mNote = new Note();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_note_create);

        Intent intent = getIntent();
        topicId = intent.getIntExtra("topicId", 0);
        noteId = intent.getIntExtra("noteId", 0);
        isUpdate = intent.getBooleanExtra("update", false);


        this.titleEdit = (EditText) findViewById(R.id.title_note);
        this.linkEdit = (EditText) findViewById(R.id.url_note);
        this.contentEdit = (EditText) findViewById(R.id.content_note);
        // Остальной код

        photoLibaray = (ImageView) findViewById(R.id.photo_library_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_note);
        mCreateNote = (TextView) toolbar.findViewById(R.id.toolbar_note_create);
        if(isUpdate){
            mCreateNote.setText("Update");
        }

        mCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.toolbar_note_create:
                            title = titleEdit.getText().toString();
                            link = linkEdit.getText().toString();
                            content = contentEdit.getText().toString();

                            if (title.isEmpty() || content.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please, fill all filed", Toast.LENGTH_SHORT).show();
                            }

                            SharedPreferences sharedPreferences = getApplicationContext()
                                    .getSharedPreferences(getString(R.string.sharedTitle), Context.MODE_PRIVATE);
                            String sessionId = sharedPreferences.getString(getString(R.string.sessionId), "");
                            if(!isUpdate){

                                //////////////////// Send new topic to server /////////////////////////////

                                try {

                                    final NoteModel newNote = new NoteModel();
                                    newNote.setTopic(topicId);
                                    newNote.setComment(title);
                                    newNote.setContent(content);
                                    newNote.setUrl(link);
//                                    newNote.setServerTopicId(HelperFactory.getHelper().getTopicDAO().getServerTopicId(topicId));


                                    final NoteApi note = RetrofitFactory.retrofitLore().create(NoteApi.class);
                                    final Call<NoteAnswer> call = note.createNote(newNote, "sessionId=" + sessionId);
                                    NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<NoteAnswer>() {
                                        @Override
                                        public void onSuccess(NoteAnswer result, Response<NoteAnswer> response) {
                                            try {
                                                Log.d(TAG, "onClick: title content link" + title + content + link);
                                                ExecutorCreateNote.getInstance().setTopicId(topicId);
                                                ExecutorCreateNote.getInstance().create(title, content, link, result.getMessage());

                                            } catch (Exception ex) {
//                                 TODO write exception
                                            }
                                            finish();
                                        }

                                        @Override
                                        public void onError(Exception ex) {
                                            Log.d("onError", "Error " + ex.toString());
                                            finish();
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.d(TAG, "onClick: " + e.toString());
                                }
                            }else{

                                try {

                                    Note noteUpdate = HelperFactory.getHelper().getNoteDao().getNoteByNoteId(noteId);
                                if(title != null)
                                    noteUpdate.setNoteComment(title);
                                if(link != null)
                                    noteUpdate.setNoteUrl(link);
                                if(content != null)
                                    noteUpdate.setNoteContent(content);

                                    NoteModel noteModel = new NoteModel();
                                    noteModel.setComment(noteUpdate.getNoteComment());
                                    noteModel.setUrl(noteUpdate.getNoteUrl());
                                    noteModel.setContent(noteUpdate.getNoteContent());
                                    noteModel.setServerTopicId(noteUpdate.getServerTopicId());
                                    noteModel.setServerNoteId(noteUpdate.getServerNoteId());

                                    final NoteApi note = RetrofitFactory.retrofitLore().create(NoteApi.class);
                                    final Call<NoteAnswer> call = note.updateNote(noteModel, "sessionId=" + sessionId);
                                    NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<NoteAnswer>() {
                                        @Override
                                        public void onSuccess(NoteAnswer result, Response<NoteAnswer> response) {
                                            try {
                                                Log.d(TAG, "onClick: title content link" + title + content + link);
                                                ExecutorCreateNote.getInstance().setTopicId(topicId);
                                                ExecutorCreateNote.getInstance().create(title, content, link, result.getMessage());

                                            } catch (Exception ex) {
//                                 TODO write exception
                                            }
                                            finish();
                                        }

                                        @Override
                                        public void onError(Exception ex) {
                                            Log.d("onError", "Error " + ex.toString());
                                            finish();
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.d(TAG, "onClick: " + e.toString());
                                }
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

        this.setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        Toolbar bottomToolbar = (Toolbar) findViewById(R.id.toolbar_note_bottom);
        mTakeImage = (ImageView) bottomToolbar.findViewById(R.id.button_draw);
        mTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        trashNote = (ImageView) bottomToolbar.findViewById(R.id.trash_for_note);
        if(!isUpdate){
            trashNote.setVisibility(View.GONE);
        }
        trashNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences(getString(R.string.sharedTitle), Context.MODE_PRIVATE);
                    String sessionId = sharedPreferences.getString(getString(R.string.sessionId), "");
                    int userId = sharedPreferences.getInt(getString(R.string.userId), 0);

//                    HelperFactory.getHelper().getNoteDao().updateDeleted(topicId);
                    final NoteApi note = RetrofitFactory.retrofitLore().create(NoteApi.class);
                    int serverNoteId = HelperFactory.getHelper().getNoteDao().getServerNoteId(noteId);
                    final Call<NoteModel> call = note.deleteNote(serverNoteId, userId, "sessionId=" + sessionId);
                    NetworkThread.getInstance().execute(call, new NetworkThread.ExecuteCallback<NoteModel>() {
                        @Override
                        public void onSuccess(NoteModel result, Response<NoteModel> response) {
                            try {
//                                Log.d("on Delete", "message: " + response.body());
                                HelperFactory.getHelper().getNoteDao().deleteNoteById(noteId);
                            } catch (SQLException ex) {
//                                 TODO write exception
                            }
                            finish();
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("onError", "Error " + ex.toString());
                            finish();
                        }
                    });
                } catch (SQLException ex){
                    // TODO write exception
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Log.d("New Topic Fragment", targetUri.toString());
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    BitmapDrawable bdrawable = new BitmapDrawable(getApplicationContext().getResources(), bitmap);
                    photoLibaray.setBackground(bdrawable);
                } catch (FileNotFoundException e) {
                }
                mNote.setImage(targetUri.toString());
            }
        }
    }

}
