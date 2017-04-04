package com.example.ilya.lorekeep.DAO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "LinkInfo")
public class LinkInfo {

    @DatabaseField(generatedId = true)
    private int mId;

    @DatabaseField(canBeNull = false)
    private int GroupId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String LinkTitle;

    @DatabaseField(dataType = DataType.STRING)
    private String LinkDescription;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String Link;

    public LinkInfo(){}

    public void setGroupId(){
        GroupId = 1;
    }

    public void setLinkTitle(String title){
        LinkTitle = title;
    }

    public void setLinkDecription(String decription){
        LinkDescription = decription;
    }

    public void setLink(String link){
        Link = link;
    }

    public String getLinkTitle(){
        return LinkTitle;
    }

    public String getLinkDescription(){
        return LinkDescription;
    }

    public String getLink(){
        return Link;
    }
}
