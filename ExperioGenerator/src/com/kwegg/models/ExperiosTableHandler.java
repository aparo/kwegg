package com.kwegg.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kwegg.common.db.PSLocalMap;
import com.kwegg.common.db.SimpleTableHandler;
import com.kwegg.commons.pack.MacroCloudNewsExperioPack;

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
    private ThreadLocal<PreparedStatement> insertExperioNewsMap = new ThreadLocal<PreparedStatement>();
	public void insertCloudNewsPack(MacroCloudNewsExperioPack pack) throws Exception {
    	String sql = "INSERT INTO "+TABLE_NAME+" (newsId, feedId, json_news, json_extended, pub_time) VALUES (?,?,?,?,?)";
    	int i=1;
    	try {
    		PreparedStatement stmtToInsertPack = PSLocalMap.getPS(connection, insertExperioNewsMap, sql);
    		stmtToInsertPack.setInt(i++, pack.getCloudNews().getId());
    		stmtToInsertPack.setInt(i++, pack.getCloudNews().getFeed_id());
    		stmtToInsertPack.setString(i++, pack.getJson());
    		stmtToInsertPack.setString(i++, pack.getJsonExtended());
    		stmtToInsertPack.setLong(i++, pack.getCloudNews().getPublishTime());
    		if(null==stmtToInsertPack)
    			throw new Exception("no PS found");
			
			stmtToInsertPack.executeUpdate();
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
}
