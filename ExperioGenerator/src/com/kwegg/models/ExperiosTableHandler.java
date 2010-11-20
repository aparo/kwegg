package com.kwegg.models;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.kwegg.common.db.PSLocalMap;
import com.kwegg.common.db.SimpleTableHandler;
import com.kwegg.commons.pack.CloudNewsExperioPack;

public class ExperiosTableHandler {
	
	private static ExperiosTableHandler instance;
	private SimpleTableHandler tableHandler;
    protected Connection connection;
    private String TABLE_NAME = "EXPERIOS";
    
    private ExperiosTableHandler() {
    	tableHandler = new SimpleTableHandler();
    	connection = tableHandler.getConnection();
    }
    
    public static ExperiosTableHandler getInstance() {
    	if(instance==null) {
    		instance = new ExperiosTableHandler();
    	}
    	return instance;
    }
    private ThreadLocal<PreparedStatement> insertCloudNewsMap = new ThreadLocal<PreparedStatement>();
    public void insertCloudNewsPack(CloudNewsExperioPack pack) {
    	String sql = "INSERT INTO "+TABLE_NAME+" (newsId, json, update_time, feedId) VALUES (?,?,?,?)";
    	int i=1;
    	try {
    		PreparedStatement stmtToInsertPack = new PSLocalMap().getPS(connection, insertCloudNewsMap, sql);
    		stmtToInsertPack.setInt(i++, pack.getCloudNews().getId());
    		stmtToInsertPack.setString(i++, pack.toPublish());
    		stmtToInsertPack.setLong(i++, System.currentTimeMillis());
    		stmtToInsertPack.setInt(i++, pack.getCloudNews().getFeed_id());
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
