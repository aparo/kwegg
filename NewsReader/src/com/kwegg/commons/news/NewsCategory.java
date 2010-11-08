package com.kwegg.commons.news;

public class NewsCategory {
	
	private int id;
	private String name;
	
	public NewsCategory(String name) {
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
