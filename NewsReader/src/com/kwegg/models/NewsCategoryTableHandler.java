package com.kwegg.models;

import java.sql.Connection;

import com.kwegg.common.db.SimpleTableHandler;

public class NewsCategoryTableHandler {
	private static NewsCategoryTableHandler instance;
	private SimpleTableHandler tableHandler;
    protected Connection connection;
    private String TABLE_NAME = "NEWS_CATEGORY";
    
    private NewsCategoryTableHandler() {
    	tableHandler = new SimpleTableHandler();
    	connection = tableHandler.getConnection();
    }
    
    public static NewsCategoryTableHandler getInstance() {
    	if(instance==null) {
    		instance = new NewsCategoryTableHandler();
    	}
    	return instance;
    }
    
    /**
     * 
     * @param name
     * @return id if it exists. If it does not exist, add name to db and then return the id
     */
    public int getIdByName(String name) {
    	String sql = "SELECT * FROM "+TABLE_NAME+" WHERE name="+name;
    	
    	return 0;
    }

}
