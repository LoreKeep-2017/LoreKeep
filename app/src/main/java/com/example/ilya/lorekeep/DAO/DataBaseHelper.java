package com.example.ilya.lorekeep.DAO;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String TAG = DataBaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "links.db";

    private static final int DATABASE_VERSION = 3;

    private GroupLinkDAO groupLinkDAO = null;
    private LinkInfoDAO linkInfoDao = null;

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try{
            TableUtils.createTable(connectionSource, GroupLink.class);
            TableUtils.createTable(connectionSource, LinkInfo.class);
            Log.d(TAG, "on create call");
        } catch (SQLException e){
            Log.e(TAG, "error creating database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){
        try{
            TableUtils.dropTable(connectionSource, GroupLink.class, true);
            TableUtils.dropTable(connectionSource, LinkInfo.class, true);
            onCreate(db,connectionSource);
        } catch (SQLException e){
            Log.e(TAG, "error creating database");
        }
    }

    public GroupLinkDAO getGroupLinkDAO() throws SQLException{
        if(groupLinkDAO == null){
            groupLinkDAO = new GroupLinkDAO(getConnectionSource(), GroupLink.class);
        }
        return groupLinkDAO;
    }

    public LinkInfoDAO getLinkInfoDao() throws SQLException{
        if(linkInfoDao == null){
            linkInfoDao = new LinkInfoDAO(getConnectionSource(), LinkInfo.class);
        }
        return linkInfoDao;
    }

    @Override
    public void close(){
        super.close();
        groupLinkDAO = null;
    }



}
