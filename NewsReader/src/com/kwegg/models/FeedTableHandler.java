package com.kwegg.models;

import java.sql.Connection;

import com.kwegg.common.db.SimpleTableHandler;

public class FeedTableHandler {
	
	private static FeedTableHandler instance;
	private SimpleTableHandler tableHandler;
    protected Connection connection;
    private String TABLE_NAME = "FEED";
    
    private FeedTableHandler() {
    	tableHandler = new SimpleTableHandler();
    	connection = tableHandler.getConnection();
    }
    
    public static FeedTableHandler getInstance() {
    	if(instance==null) {
    		instance = new FeedTableHandler();
    	}
    	return instance;
    }

}
