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

    public void setTopic(Topic paramTopic) throws SQLException{
        Topic topic = new Topic();
        topic.setTopicTitle(paramTopic.getTopicTitle());
        topic.setImage(paramTopic.getImage());
        this.create(topic);
    }

    public void deleteTopicById(Integer topicId) throws SQLException{
        this.deleteById(topicId);
    }
}
