package com.example.ilya.lorekeep.topic.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class TopicImpl extends BaseDaoImpl<Topic, Integer> {

    public TopicImpl(ConnectionSource connectionSource, Class<Topic> topicClass) throws SQLException{
        super(connectionSource, topicClass);
    }

    public List<Topic> getAllTopics() throws SQLException{
        return this.queryForAll();
    }

    public void setTopic() throws SQLException{
        this.create(new Topic());
    }
}
