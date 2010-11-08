package com.kwegg.news.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.kwegg.commons.news.FastFeed;
import com.kwegg.interpretor.BaseInterpretor;
import com.kwegg.interpretor.DefaultRomeInterpretor;

public class testCases {
	
	public static void testSyndFeed() {
		URL feedURL;
		try {
			feedURL = new URL("http://rss.news.yahoo.com/rss/tech");
			FastFeed fd = new FastFeed(feedURL);
			BaseInterpretor bi = new DefaultRomeInterpretor();
			bi.getAllFastNews(fd);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		testSyndFeed();
	}

}
