package com.kwegg.commons.news;

import java.util.Date;
import java.util.List;

public class CloudNews extends FastNews {

	private String title;
	private String linkText;
	private String baseUri;
	private Date receiveDate;
	private Date publishDate;

	private String comments;
	private String inReplyTo;
	private boolean isFlagged;
	private int rating;


	private Source source;
	private String feedLink;
	private Author author;

	//private List<Attachment> attachments;
	private List<NewsCategory> categories;

	/* This field is only non-zero if the parent is not a feed */
	private long parentId;
}
