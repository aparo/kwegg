package com.kwegg.commons.news;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import com.kwegg.interpretor.BaseInterpretor;
import com.kwegg.interpretor.DefaultRomeInterpretor;
import com.kwegg.models.FeedTableHandler;
import com.kwegg.models.NewsTableHandler;
import com.kwegg.utils.ReaderUtils;

public class FastFeed {
	
	public static final String DEFAULT_SAVE_AS_ENCODING = "utf-8";
	private int id;
	private final URL feedURL;
	private long lastModifiedTime = 0;
	private long lastCrawledTime = 0;
	private boolean isEnabled = true;
	private String name;
	
	public FastFeed(URL feedURL) {
		this.feedURL = ReaderUtils.normalizeURL(feedURL);
	}

	/**
	 * Determine whether this <tt>FeedInfo</tt> object is equivalent to another
	 * one, based on the URL.
	 * 
	 * @param obj
	 *            the other object
	 * 
	 * @return <tt>true</tt> if <tt>obj</tt> is a <tt>FeedInfo</tt> object that
	 *         specifies the same URL, <tt>false</tt> otherwise
	 */
	public boolean equals(final Object obj) {
		boolean eq = false;

		if (obj instanceof FastFeed)
			eq = this.feedURL.equals(((FastFeed) obj).feedURL);

		return eq;
	}
	
	public synchronized void dumpNews() {
		System.out.println("dumping news for feed-id: "+id+", "+name);
		BaseInterpretor bi = new DefaultRomeInterpretor();
		try {
			LinkedList<CloudNews> lcn = (LinkedList<CloudNews>) bi.getAllCloudNews(this);
			Object[] cnArr = lcn.toArray();
			int length = cnArr.length;
			for(int inc=1; inc<=length; inc++) {
				try {
					CloudNews ns = (CloudNews) cnArr[length-inc];
					if(NewsTableHandler.getInstance().getNewsByGuid(ns.getGuidValue())==null)
						NewsTableHandler.getInstance().insertNews(ns);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				FeedTableHandler.getInstance().updateFeed(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Get the main RSS URL for the site.
	 * 
	 * @return the site's main RSS URL, guaranteed to be normalized
	 * @see CurnUtil#normalizeURL
	 */
	public URL getURL() {
		return feedURL;
	}
	
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
	public long getLastCrawledTime() {
		return lastCrawledTime;
	}
	public void setLastCrawledTime(long lastCrawledTime) {
		this.lastCrawledTime = lastCrawledTime;
	}
	
	public void enable() {
		isEnabled = true;
	}
	public void disable() {
		isEnabled = false;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
