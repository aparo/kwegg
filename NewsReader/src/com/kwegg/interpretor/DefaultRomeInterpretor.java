package com.kwegg.interpretor;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.kwegg.commons.news.FastFeed;
import com.kwegg.commons.news.FastNews;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class DefaultRomeInterpretor implements BaseInterpretor {

	@Override
	public List<FastNews> getAllFastNews(FastFeed feed) throws IOException {
		List<FastNews> newsList = new LinkedList<FastNews>();
		
		SyndFeedInput syndFeedInput = new SyndFeedInput();
		SyndFeed syndFeed = null;
		XmlReader xmlReader = new XmlReader(feed.getURL());
		try {
			syndFeed = syndFeedInput.build(xmlReader);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(syndFeed.getAuthor()+", "+syndFeed.getCopyright()+", "+syndFeed.getDescription());
		System.out.println(syndFeed.getEncoding()+", "+syndFeed.getFeedType()+", "+syndFeed.getLanguage());
		System.out.println(syndFeed.getLink()+", "+syndFeed.getTitle()+", "+syndFeed.getUri());
		System.out.println(syndFeed.getCategories().toArray().toString()+", "+syndFeed.getPublishedDate());
		feed.setLastCrawledTime(System.currentTimeMillis());
		
		/*
		 * generate list of news after last modified time
		 */
		Iterator it = syndFeed.getEntries().iterator();
		while(it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			System.out.println(entry.getTitle()+": "+entry.getLink());
		}
		
		/**
		 * finally set last modified time as from feed
		 */
		feed.setLastModifiedTime(syndFeed.getPublishedDate().getTime());
		
		return newsList;
	}

}
