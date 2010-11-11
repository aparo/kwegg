package com.kwegg.commons.news;

public class CloudNews {
	
	private int id=0;
	private String guidValue="";
	private String title="";
	private String baseUri="";
	private long publishTime=0l;
	private long updatedTime=0l;
	private String feedLink="";
	private String content="";
	private String description="";
	private String author="";
	private int feed_id=0;
	
	public CloudNews() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGuidValue() {
		return guidValue;
	}
	public void setGuidValue(String guidValue) {
		this.guidValue = guidValue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public long getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}
	public long getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getFeedLink() {
		return feedLink;
	}
	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getFeed_id() {
		return feed_id;
	}
	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}
	
}
