package com.example.ilya.lorekeep.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ilya.lorekeep.note.dao.Note;
import com.example.ilya.lorekeep.note.dao.NoteImpl;
import com.example.ilya.lorekeep.topic.dao.Topic;
import com.example.ilya.lorekeep.topic.dao.TopicImpl;
import com.example.ilya.lorekeep.user.dao.User;
import com.example.ilya.lorekeep.user.dao.UserImpl;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "links.db";

    private static final int DATABASE_VERSION = 230;

    private TopicImpl topicNoteDAO = null;
    private NoteImpl noteInfoDao = null;
    private UserImpl userDao = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Topic.class);
            TableUtils.createTable(connectionSource, Note.class);
            Log.d(TAG, "on create call");
        } catch (SQLException e) {
            Log.e(TAG, "error creating database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Topic.class, true);
            TableUtils.dropTable(connectionSource, Note.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error creating database");
        }
    }

    public TopicImpl getTopicDAO() throws SQLException {
        if (topicNoteDAO == null) {
            topicNoteDAO = new TopicImpl(getConnectionSource(), Topic.class);
        }
        return topicNoteDAO;
    }

    public NoteImpl getNoteDao() throws SQLException {
        if (noteInfoDao == null) {
            noteInfoDao = new NoteImpl(getConnectionSource(), Note.class);
        }
        return noteInfoDao;
    }

    public UserImpl getUserDao() throws SQLException{
        if(userDao == null){
            userDao = new UserImpl(getConnectionSource(), User.class);
        }
        return userDao;
    }

    @Override
    public void close() {
        super.close();
        topicNoteDAO = null;
    }

}
