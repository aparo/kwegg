package com.kwegg.commons.experio;

public class BaseSubject {
	private String phrase;
	private Object[] keywords;
	
	public BaseSubject(String phrase, Object[] keywords) {
		this.phrase = phrase;
		this.keywords = keywords;
	}
	
	public final String getSubjectPhrase(){ return phrase;}
	public final Object[] getKeywords() {return keywords;};
}
