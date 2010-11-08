package com.kwegg.models;

import java.sql.Connection;

import com.kwegg.common.db.SimpleTableHandler;

public class NewsTableHandler {
	
	private static NewsTableHandler instance;
	private SimpleTableHandler tableHandler;
    protected Connection connection;
    private String TABLE_NAME = "NEWS";
    
    private NewsTableHandler() {
    	tableHandler = new SimpleTableHandler();
    	connection = tableHandler.getConnection();
    }
    
    public static NewsTableHandler getInstance() {
    	if(instance==null) {
    		instance = new NewsTableHandler();
    	}
    	return instance;
    }

}
