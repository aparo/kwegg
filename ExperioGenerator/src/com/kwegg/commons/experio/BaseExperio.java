package com.kwegg.commons.experio;


public class BaseExperio {
	private String phrase;
	/**
	 * subjects should be in order
	 */
	private BaseSubject[] subjects;
	
	public BaseExperio(String phrase, BaseSubject[] subjects) {
		this.phrase = phrase;
		this.subjects = subjects;
	}
	
	public String getPhrase() {return phrase;};
	public BaseSubject[] getSubjects(){return subjects;};
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("{").append("phrase:").append(getPhrase()).append(", subjects:").append("{");
		for(BaseSubject bs: getSubjects()) {
			sb.append(bs.getSubjectPhrase());
			sb.append(" -- ");
		}
		sb.append("}}");
		return sb.toString();
	}
}
