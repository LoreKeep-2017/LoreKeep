package com.example.ilya.lorekeep.topic.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TopicImpl extends BaseDaoImpl<Topic, Integer> {

    public TopicImpl(ConnectionSource connectionSource, Class<Topic> topicClass) throws SQLException{
        super(connectionSource, topicClass);
    }

    public List<Topic> getAllTopics() throws SQLException{
        return this.queryForAll();
    }

    public void setTopic(Topic newTopic) throws SQLException{
        newTopic.setTopicCreationDate(new Date());
        newTopic.setTopicLastUsed(new Date());
        newTopic.setTopicRating(0);
        this.create(newTopic);
    }

    public void updateChanged(int topicId){
        try {
            UpdateBuilder<Topic, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("topicId", topicId);
            updateBuilder.updateColumnValue("changed", false);
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void deleteTopicById(Integer topicId) throws SQLException{
        this.deleteById(topicId);
    }
}
