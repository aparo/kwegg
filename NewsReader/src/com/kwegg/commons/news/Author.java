package com.kwegg.commons.news;

/**
 * each feed or news may have an author
 * @author parag
 *
 */
public class Author {
	
	private String name;
	private String uri;
	private String email;
	private long id;
	
	public Author(long id) {
		this.id = id;
	}
}
