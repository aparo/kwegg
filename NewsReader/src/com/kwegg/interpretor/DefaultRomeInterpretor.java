package com.kwegg.interpretor;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.news.FastFeed;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class DefaultRomeInterpretor implements BaseInterpretor {

	@Override
	public List<CloudNews> getAllCloudNews(FastFeed feed) throws IOException {
		List<CloudNews> newsList = new LinkedList<CloudNews>();
		
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
		
		if(syndFeed.getPublishedDate().getTime() <= feed.getLastModifiedTime()) {
			return newsList;
		}
			
		System.out.println("Crawling started for "+feed.getName()+" at "+System.currentTimeMillis());
		
		/*
		 * generate list of news after last modified time
		 */
		Iterator it = syndFeed.getEntries().iterator();
		while(it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			CloudNews news = new CloudNews();
			if(entry.getAuthor()!=null)
				news.setAuthor(entry.getAuthor());
			if(entry.getUri()!=null)
				news.setBaseUri(entry.getUri());
			if(entry.getDescription()!=null)
				news.setDescription(entry.getDescription().getValue());
			if(entry.getContents()!=null) {
				if(entry.getContents().size() > 0) {
					SyndContent syndContent = (SyndContent)entry.getContents().get(0);
					news.setContent(syndContent.getValue());
				}
				else {
					news.setContent(entry.getDescription().getValue());
				}
			}
			
			news.setFeed_id(feed.getId());
			news.setFeedLink(feed.getURL().toString());
			if(entry.getUri()!=null)
				news.setGuidValue(entry.getUri());
			if(entry.getPublishedDate()!=null)
				news.setPublishTime(entry.getPublishedDate().getTime());
			if(entry.getTitle()!=null)
				news.setTitle(entry.getTitle());
			if(entry.getUpdatedDate()!=null)
				news.setUpdatedTime(entry.getUpdatedDate().getTime());
			newsList.add(news);
			
			feed.setLastCrawledTime(System.currentTimeMillis());
			feed.setLastModifiedTime(syndFeed.getPublishedDate().getTime());
		}
		
		/**
		 * finally set last modified time as from feed
		 */
		feed.setLastModifiedTime(syndFeed.getPublishedDate().getTime());
		
		return newsList;
	}

}
