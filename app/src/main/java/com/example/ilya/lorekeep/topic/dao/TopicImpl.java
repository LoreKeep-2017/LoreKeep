package com.example.ilya.lorekeep.topic.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
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


    public Topic getTopic(int topicId) throws SQLException{   /// CHech changes !!!!!!!!!!!
        return this.queryForId(topicId);
//        return this.queryBuilder().where().eq("topicId", topicId).query();
    }

    public void updateTopic(Topic topic) throws SQLException{
        this.update(topic);
    }

    public void setTopic(Topic newTopic) throws SQLException{
        newTopic.setTopicCreationDate(new Date());
        newTopic.setTopicLastUsed(new Date());
        newTopic.setTopicRating(0);
        this.create(newTopic);
    }

    public Topic getTopicByServerTopicId(int serverTopicId) throws SQLException{
        return this.queryBuilder().where().eq("serverTopicId", serverTopicId).queryForFirst(); /// Maybe the problem
    }

    public void updateChanged(int topicId){
        try {
            UpdateBuilder<Topic, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("topicId", topicId);
            updateBuilder.updateColumnValue("changed", false);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void updateCreated(int topicId){
        try {
            UpdateBuilder<Topic, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("topicId", topicId);
            updateBuilder.updateColumnValue("created", false);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void updateDeleted(int topicId){
        try{
            UpdateBuilder<Topic, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("topicId", topicId);
            updateBuilder.updateColumnValue("deleted", true);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public void updateServerTopicId(int topicId, int serverTopicId){
        try{
            UpdateBuilder<Topic, Integer> updateBuilder = this.updateBuilder();
            updateBuilder.where().eq("topicId", topicId);
            updateBuilder.updateColumnValue("serverTopicId", serverTopicId);
            updateBuilder.update();
        } catch(SQLException ex){
            //TODO write exception
        }
    }

    public int getChangedCount(){
        try {
            return (int) this.queryBuilder().where().eq("changed", true).countOf();
        } catch(SQLException ex){
            return -1;
        }
    }

    public int getCreatedCount(){
        try{
            return (int) this.queryBuilder().where().eq("created", true).countOf();
        } catch(SQLException ex){
            return -1;
        }
    }

    public List<Topic> getChangedTopics(){
        try{
            return this.queryBuilder().where().eq("changed", true).query();
        } catch(SQLException ex){
            return null;
        }
    }

    public List<Topic> getCreatedTopics(){
        try{
            return this.queryBuilder().where().eq("created", true).query();
        } catch(SQLException ex){
            return null;
        }
    }

    public int getServerTopicId(int topicId){
        try{
            List<Topic> topic =  this.queryBuilder().selectColumns("serverTopicId").where().eq("topicId", topicId).query();
            return topic.get(0).getServerTopicId();
        } catch(SQLException ex){
            return -1;
        }
    }

    public void deleteTopicById(Integer topicId) throws SQLException{
        this.deleteById(topicId);
    }
}
