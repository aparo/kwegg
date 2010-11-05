package com.kwegg.commons.news;

import java.net.URL;

public class FeedInfo {
	public static final String DEFAULT_SAVE_AS_ENCODING = "utf-8";
	
	private int daysToCache = 0;
    private final URL siteURL;
    private String forcedEncoding = null;
    
    public FeedInfo(URL siteURL) {
    	this.siteURL = siteURL;
    }
}
