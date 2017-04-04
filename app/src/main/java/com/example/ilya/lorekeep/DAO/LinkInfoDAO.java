package com.example.ilya.lorekeep.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class LinkInfoDAO extends BaseDaoImpl<LinkInfo, Integer> {

    protected LinkInfoDAO(ConnectionSource connectionSource, Class<LinkInfo> linkInfo) throws SQLException{
        super(connectionSource, linkInfo);
    }

    public List<LinkInfo> getAllLinks() throws SQLException{
        return this.queryForAll();
    }

    public void setNewLink(LinkInfo link) throws SQLException{
        this.create(link);
    }
}
