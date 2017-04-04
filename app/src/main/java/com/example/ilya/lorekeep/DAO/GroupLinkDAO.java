package com.example.ilya.lorekeep.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class GroupLinkDAO extends BaseDaoImpl<GroupLink, Integer> {

    protected GroupLinkDAO(ConnectionSource connectionSource, Class<GroupLink> groupClass) throws SQLException{
        super(connectionSource, groupClass);
    }

    public List<GroupLink> getAllGroups() throws SQLException{
        return this.queryForAll();
    }

    public void setLinkGroup() throws SQLException{
        this.create(new GroupLink());
    }
}
