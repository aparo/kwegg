package com.kwegg.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PSLocalMap
{
	/**
	 * 
	 * @param connection
	 * @param psMap
	 * @param query
	 * @return
	 * @throws SQLException 
	 */
    public static PreparedStatement getPS(Connection connection, ThreadLocal<PreparedStatement> psMap, String query) throws SQLException
    {
        PreparedStatement ps = psMap.get();
        if (ps == null)
        {
        	ps = connection.prepareStatement(query);
            psMap.set(ps);
        }
        return ps;
    }
    
    /**
     * 
     * @param connection
     * @param psMap
     * @param query
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException 
     */
    public static PreparedStatement getPS(Connection connection, ThreadLocal<PreparedStatement> psMap, String query, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        PreparedStatement ps = psMap.get();
        if (ps == null)
        {
        	ps = connection.prepareStatement(query, resultSetType, resultSetConcurrency);
            psMap.set(ps);
        }
        return ps;
    }
}
