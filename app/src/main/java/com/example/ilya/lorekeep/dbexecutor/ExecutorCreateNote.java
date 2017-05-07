package com.example.ilya.lorekeep.dbexecutor;

import android.util.Log;

import com.example.ilya.lorekeep.Ui;
import com.example.ilya.lorekeep.config.HelperFactory;
import com.example.ilya.lorekeep.note.NoteList;
import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.topic.dao.Topic;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorCreateNote {

    private String TAG = "executorCreateNote";
    private Map<Integer, List<Note>> mlNotes = NoteList.getInstance().mlNotes;

    private static ExecutorCreateNote CREATE_NOTE = null;
    private final Executor executor = Executors.newCachedThreadPool();
    private ExecutorCreateNote.Callback callback;
    private Integer topicId;

    public ExecutorCreateNote(){

    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public static ExecutorCreateNote getInstance() {

        if(CREATE_NOTE == null){
            CREATE_NOTE = new ExecutorCreateNote();
        }
        return CREATE_NOTE;
    }

    public interface Callback {
        void onCreate();
    }

    public void setCallback(ExecutorCreateNote.Callback callback) {
        this.callback = callback;
    }

    public void create(final String content, final String comment, final String link) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Note newNote = new Note();
                    Topic topic = HelperFactory.getHelper().getTopicDAO().queryForId(topicId);
                    newNote.setTopic(topic);
                    newNote.setNoteContent(content);
                    newNote.setNoteComment(comment);
                    newNote.setNoteUrl(link);
                    HelperFactory.getHelper().getNoteDao().setNewNote(newNote);

                    List<Note> notes = mlNotes.get(topicId);
                    notes.add(newNote);
                    mlNotes.put(topicId, notes);
                    Log.e(TAG, "run: put in cache!!!" );
                } catch (SQLException e) {
                    Log.e("in create link", e.toString());
                }

                notifyLoaded();
            }
        });
    }

    protected void notifyLoaded() {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onCreate();
                }
                Log.d(TAG, "run: notify UI");
            }
        });
    }
}
