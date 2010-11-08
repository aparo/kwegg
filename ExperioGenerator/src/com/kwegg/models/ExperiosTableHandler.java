package com.kwegg.models;

import java.sql.Connection;

import com.kwegg.common.db.SimpleTableHandler;

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
}
