package com.kwegg.commons.news;

import java.util.Date;
import java.util.List;

public class CloudNews extends FastNews {
	
	public CloudNews(long id, long modifiedTime) {
		super(id, modifiedTime);
		// TODO Auto-generated constructor stub
	}
	private int id;
	private String guidValue;
	private String title;
	private String baseUri;
	private long modifiedTime;
	private long publishTime;
	private long updatedTime;
	private String feedLink;
	private String content;
	private String author;
}
