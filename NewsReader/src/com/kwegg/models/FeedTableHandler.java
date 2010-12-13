package com.kwegg.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kwegg.common.db.PSLocalMap;
import com.kwegg.common.db.SimpleTableHandler;
import com.kwegg.common.utils.CircularLinkedList;
import com.kwegg.commons.news.FastFeed;

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
    
    private ThreadLocal<PreparedStatement> saveFeedPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param fd
     * @throws Exception
     */
    public void saveFeed(FastFeed fd) throws Exception {
    	String sql = "INSERT INTO "+TABLE_NAME+" (url, name, lastModifiedTime, lastCrawledTime, isEnabled) VALUES (?,?,?,?,?)";
    	int i=1;
    	try {
			PreparedStatement stmtToInsertFeed = PSLocalMap.getPS(connection, saveFeedPsMap, sql);
			stmtToInsertFeed.setString(i++, fd.getURL().toString());
			stmtToInsertFeed.setString(i++, fd.getName());
			stmtToInsertFeed.setLong(i++, fd.getLastModifiedTime());
			stmtToInsertFeed.setLong(i++, fd.getLastCrawledTime());
			stmtToInsertFeed.setInt(i++, fd.isEnabled()?1:0);
			
			if(null==stmtToInsertFeed) 
				throw new Exception("no PS found");
			
			stmtToInsertFeed.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private ThreadLocal<PreparedStatement> updateFeedPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param fd
     * @throws Exception
     */
    public void updateFeed(FastFeed fd) throws Exception {
    	String sql = "UPDATE "+TABLE_NAME+" SET url=?, name=?, lastModifiedTime=?, lastCrawledTime=?, isEnabled=? WHERE id=?";
    	int i=1;
    	try {
			PreparedStatement stmtToUpdateFeed = PSLocalMap.getPS(connection, updateFeedPsMap, sql);
			stmtToUpdateFeed.setString(i++, fd.getURL().toString());
			stmtToUpdateFeed.setString(i++, fd.getName());
			stmtToUpdateFeed.setLong(i++, fd.getLastModifiedTime());
			stmtToUpdateFeed.setLong(i++, fd.getLastCrawledTime());
			stmtToUpdateFeed.setInt(i++, fd.isEnabled()?1:0);
			stmtToUpdateFeed.setInt(i++, fd.getId());
			if(null==stmtToUpdateFeed) 
				throw new Exception("no PS found");
			
			stmtToUpdateFeed.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private ThreadLocal<PreparedStatement> getFeedFromUrlPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param url
     * @return 0 if feed does not exist. Non Zero id if feed exists
     * @throws Exception 
     */
    public int getFeedId(String url) {
    	int id=0;
    	String sql = "SELECT id FROM "+TABLE_NAME+" WHERE url=?";
    	PreparedStatement stmtToGetFeed;
		try {
			stmtToGetFeed = PSLocalMap.getPS(connection, getFeedFromUrlPsMap, sql);
			stmtToGetFeed.setString(1, url);
	    	
	    	if(null==stmtToGetFeed)
	    		return 0;
	    	
	    	ResultSet rs = stmtToGetFeed.executeQuery();
	    	if(rs.next()) {
	    		id = rs.getInt("id");
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return id;
    }
    
    private ThreadLocal<PreparedStatement> getAllFeedsPsMap = new ThreadLocal<PreparedStatement>();
    public CircularLinkedList<FastFeed> getAllFastFeeds() throws Exception {
    	CircularLinkedList<FastFeed> fastFeedsQueue = new CircularLinkedList<FastFeed>();
    	String sql = "SELECT * FROM "+TABLE_NAME;
    	PreparedStatement stmtToGetFastFeeds = PSLocalMap.getPS(connection, getAllFeedsPsMap, sql);
    	if(null==stmtToGetFastFeeds)
    		throw new Exception("no PS found");
    	ResultSet rs = stmtToGetFastFeeds.executeQuery();
    	while(rs.next()) {
    		fastFeedsQueue.add(getFastFeedFromResultSet(rs));
    	}
    	
    	return fastFeedsQueue;
    }
    
    public FastFeed getFastFeedFromResultSet(ResultSet rs) {
    	URL feedUrl;
    	FastFeed fd = null;
		try {
			feedUrl = new URL(rs.getString("url"));
			fd = new FastFeed(feedUrl);
			fd.setId(rs.getInt("id"));
			fd.setName(rs.getString("name"));
			fd.setLastCrawledTime(rs.getLong("lastCrawledTime"));
			fd.setLastModifiedTime(rs.getLong("lastModifiedTime"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fd;
    }
}
