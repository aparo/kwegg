package com.kwegg.commons.news;

import java.net.URL;

import com.kwegg.utils.ReaderUtils;
import com.kwegg.utils.ReaderUtils.FeedFormat;

public class FastFeed {
	
	public static final String DEFAULT_SAVE_AS_ENCODING = "utf-8";
	private final URL feedURL;
	private long lastModifiedTime;
	private long lastCrawledTime;
	private FeedFormat format;
	
	public FastFeed(URL feedURL) {
		this.feedURL = ReaderUtils.normalizeURL(feedURL);
	}
	
	/**
	 * Get the hash code for this feed
	 * 
	 * @return the hash code
	 */
	public int hashCode() {
		return getURL().hashCode();
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
	
	public FeedFormat getFeedFormat() {
		return format;
	}
	public void setFeedFormat(FeedFormat format) {
		this.format = format;
	}
	
	
	
}
