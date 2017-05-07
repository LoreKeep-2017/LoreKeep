package com.example.ilya.lorekeep.user.dao;


import com.example.ilya.lorekeep.topic.dao.Topic;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class UserImpl extends BaseDaoImpl<User, Integer>{

    public UserImpl(ConnectionSource connectionSource, Class<User> userClass) throws SQLException {
        super(connectionSource, userClass);
    }

    public void setUser(User newUser) throws SQLException{
        newUser.setUsername("username");
        newUser.setPassword("password");
        newUser.setEmail("email@email.com");
        newUser.setPhonenumber("3898493483948");
        this.create(newUser);
    }



}
