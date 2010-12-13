package com.kwegg.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kwegg.common.db.PSLocalMap;
import com.kwegg.common.db.SimpleTableHandler;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.news.FastFeed;

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
    
    private ThreadLocal<PreparedStatement> saveNewsPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param n
     * @throws Exception
     */
    public void insertNews(CloudNews n) throws Exception {
    	String sql = "INSERT INTO "+TABLE_NAME+" (feedId, guidValue, title, author, updatedTime, publishedTime, content, baseUri) VALUES(?,?,?,?,?,?,?,?)";
    	int i=1;
    	try {
			PreparedStatement stmtToInsertNews = PSLocalMap.getPS(connection, saveNewsPsMap, sql);
			stmtToInsertNews.setInt(i++, n.getFeed_id());
			stmtToInsertNews.setString(i++, n.getGuidValue());
			stmtToInsertNews.setString(i++, n.getTitle());
			stmtToInsertNews.setString(i++, n.getAuthor());
			stmtToInsertNews.setLong(i++, n.getUpdatedTime());
			stmtToInsertNews.setLong(i++, n.getPublishTime());
			stmtToInsertNews.setString(i++, n.getContent());
			stmtToInsertNews.setString(i++, n.getBaseUri());
			
			if(null==stmtToInsertNews) 
				throw new Exception("no PS found");
			
			stmtToInsertNews.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private ThreadLocal<PreparedStatement> getAllNewsForFeedPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param fd
     * @param numFeeds
     * @return feeds sorted by time
     */
    public ArrayList<CloudNews> getAllNewsForFeed(FastFeed fd, int from, int to) {
    	String sql = "SELECT * FROM "+TABLE_NAME+" WHERE feedId=? ORDER BY id DESC LIMIT ?,?";
    	int i=1;
    	ArrayList<CloudNews> acn = new ArrayList<CloudNews>();
    	try {
			PreparedStatement stmtToGetNews = PSLocalMap.getPS(connection, getAllNewsForFeedPsMap, sql);
			stmtToGetNews.setInt(i++, fd.getId());
			stmtToGetNews.setInt(i++, from);
			stmtToGetNews.setInt(i++, to);
			ResultSet rs = stmtToGetNews.executeQuery();
			while(rs.next()) {
				CloudNews cn = getCloudNewsFromRS(rs);
				acn.add(cn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return acn;
    }
    private ThreadLocal<PreparedStatement> getAllNotConvertedNewsForFeedPsMap = new ThreadLocal<PreparedStatement>();
    public ArrayList<CloudNews> getAllNotConvertedNewsForFeed(FastFeed fd) {
    	String sql = "SELECT * FROM "+TABLE_NAME+" WHERE feedId=? AND  isConverted=?";
    	int i=1;
    	ArrayList<CloudNews> acn = new ArrayList<CloudNews>();
    	try {
			PreparedStatement stmtToGetNews = PSLocalMap.getPS(connection, getAllNotConvertedNewsForFeedPsMap, sql);
			stmtToGetNews.setInt(i++, fd.getId());
			stmtToGetNews.setInt(i++, 0);
			ResultSet rs = stmtToGetNews.executeQuery();
			while(rs.next()) {
				CloudNews cn = getCloudNewsFromRS(rs);
				acn.add(cn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return acn;
    }
    
    private ThreadLocal<PreparedStatement> getNewsByIdPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param fd
     * @param numFeeds
     * @return feeds sorted by time
     */
    public CloudNews getNewsById(int id) {
    	String sql = "SELECT * FROM "+TABLE_NAME+" WHERE id=?";
    	int i=1;
    	CloudNews cn = null;
    	try {
			PreparedStatement stmtToGetNews = PSLocalMap.getPS(connection, getNewsByIdPsMap, sql);
			stmtToGetNews.setInt(i++, id);
			ResultSet rs = stmtToGetNews.executeQuery();
			while(rs.next()) {
				cn = getCloudNewsFromRS(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return cn;
    }
    
    private ThreadLocal<PreparedStatement> getNewsByGuidPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * 
     * @param fd
     * @param numFeeds
     * @return feeds sorted by time
     */
    public CloudNews getNewsByGuid(String guidValue) {
    	String sql = "SELECT * FROM "+TABLE_NAME+" WHERE guidValue=?";
    	int i=1;
    	CloudNews cn = null;
    	try {
			PreparedStatement stmtToGetNews = PSLocalMap.getPS(connection, getNewsByGuidPsMap, sql);
			stmtToGetNews.setString(i++, guidValue);
			ResultSet rs = stmtToGetNews.executeQuery();
			while(rs.next()) {
				cn = getCloudNewsFromRS(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return cn;
    }
    
    private ThreadLocal<PreparedStatement> updateConvertedNewsPsMap = new ThreadLocal<PreparedStatement>();
    /**
     * update single news when it gets converted to experio
     * @param ns
     * @throws Exception 
     */
    public void updateNewsToConverted(CloudNews ns) throws Exception {
    	String sql = "UPDATE "+TABLE_NAME+" SET isConverted=? WHERE id=?";
    	int i=1;
    	try {
			PreparedStatement stmtToUpdateNews = PSLocalMap.getPS(connection, updateConvertedNewsPsMap, sql);
			stmtToUpdateNews.setInt(i++, 1);
			stmtToUpdateNews.setInt(i++, ns.getId());
			if(null==stmtToUpdateNews)
				throw new Exception("no PS found");
			stmtToUpdateNews.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private CloudNews getCloudNewsFromRS(ResultSet rs) throws SQLException {
    	int id = rs.getInt("id");
    	CloudNews cn = new CloudNews();
    	cn.setId(id);
    	cn.setFeed_id(rs.getInt("feedId"));
    	cn.setAuthor(rs.getString("author"));
    	cn.setBaseUri(rs.getString("baseUri"));
    	cn.setContent(rs.getString("content"));
    	cn.setFeedLink(rs.getString("feedLink"));
    	cn.setGuidValue(rs.getString("guidValue"));
    	cn.setPublishTime(rs.getLong("publishedTime"));
    	cn.setTitle(rs.getString("title"));
    	cn.setUpdatedTime(rs.getLong("updatedTime"));
    	
    	return cn;
    }
}
