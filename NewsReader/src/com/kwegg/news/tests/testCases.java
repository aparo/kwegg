package com.kwegg.news.tests;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.news.service.NewsReaderService;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class testCases {
	
	public static void addFeedAndStartService() {
		NewsReaderService service = NewsReaderService.getInstance();
		try {
			service.addFeed("http://feeds.feedburner.com/ommalik");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.onStart();
	}
	
	public static void timeTest() throws IOException {
		SyndFeedInput syndFeedInput = new SyndFeedInput();
		SyndFeed syndFeed = null;
		URL url = new URL("http://feedproxy.google.com/TechCrunch");
		System.out.println("current time: "+System.currentTimeMillis());
		XmlReader xmlReader = new XmlReader(url);
		try {
			syndFeed = syndFeedInput.build(xmlReader);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("feed publish time: "+syndFeed.getPublishedDate().getTime());
		/*
		 * generate list of news after last modified time
		 */
		Iterator it = syndFeed.getEntries().iterator();
		while(it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			System.out.println("news publish time: "+entry.getPublishedDate().getTime()+" for "+entry.getTitle());
		}
	}
	
	public static void main(String[] args) {
		addFeedAndStartService();
	}

}
