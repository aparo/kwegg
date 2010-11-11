package com.kwegg.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kwegg.common.db.PSLocalMap;
import com.kwegg.common.db.SimpleTableHandler;

public class CategoryTableHandler {
	private static CategoryTableHandler instance;
	private SimpleTableHandler tableHandler;
    protected Connection connection;
    private String TABLE_NAME = "CATEGORY";
    
    private CategoryTableHandler() {
    	tableHandler = new SimpleTableHandler();
    	connection = tableHandler.getConnection();
    }
    
    public static CategoryTableHandler getInstance() {
    	if(instance==null) {
    		instance = new CategoryTableHandler();
    	}
    	return instance;
    }
    
    private ThreadLocal<PreparedStatement> getIdByNamePsMap = new ThreadLocal<PreparedStatement>();
    private ThreadLocal<PreparedStatement> insertPsMap = new ThreadLocal<PreparedStatement>();
    private ThreadLocal<PreparedStatement> getLastIdPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param name
     * @return id if it exists. If it does not exist, add name to db and then return the id
     * @throws Exception 
     */
    public int getIdByName(String name) throws Exception {
    	int id=0;
    	String sql = "SELECT id FROM "+TABLE_NAME+" WHERE name=?";
    	try {
			PreparedStatement stmtToGetIdByName = PSLocalMap.getPS(connection, getIdByNamePsMap, sql);
			stmtToGetIdByName.setString(1, name);
			if(null==stmtToGetIdByName) 
				throw new Exception("no PS found");
			
			ResultSet rs = stmtToGetIdByName.executeQuery();
			if(!rs.next()) {
				String insert_sql = "INSERT INTO "+TABLE_NAME+" (name) VALUES (?)"; 
				PreparedStatement stmtToInsertName = PSLocalMap.getPS(connection, insertPsMap, insert_sql);
				stmtToInsertName.setString(1, name);
				if(null==stmtToInsertName) 
					throw new Exception("no PS found");
				
				stmtToInsertName.executeUpdate();
				
				String get_sql = "SELECT last_insert_id() AS id FROM "+TABLE_NAME;
				PreparedStatement stmtToGetLastId = PSLocalMap.getPS(connection, getLastIdPsMap, get_sql);
				if(null==stmtToGetLastId) 
					throw new Exception("no PS found");
				else
					stmtToGetLastId.clearParameters();
				ResultSet rs1= stmtToGetLastId.executeQuery();
				rs1.next();
				id = rs1.getInt("id");
			}
			else {
				id = rs.getInt("id");
			}
		} 
    	catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return id;
    }

}
