package com.example.ilya.lorekeep.DAO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "GroupLink")
public class GroupLink {

    @DatabaseField(generatedId = true)
    private int mId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String GroupTitle;

    public GroupLink(){
        GroupTitle = "Java";
    }

}
