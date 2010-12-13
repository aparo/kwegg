package com.kwegg.commons.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.googleapis.ajax.schema.ImageResult;
import com.kwegg.common.utils.Utils;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.extractor.BaseExperioExtractor;
import com.kwegg.models.NewsTableHandler;
@Deprecated
public class CloudNewsExperioPack {
	private CloudNews news;
	private CloudExperiencePack[] pack;
	private HashMap<String, Integer> numberOfKeywordsMap = new HashMap<String, Integer>();
	//array sorted in increasing order
	private String[] keywods;
	
	private final int keywordsPerPack = 3;
	private final int totalKeywords = 3;
	
	public CloudNewsExperioPack(CloudNews news) throws IOException {
		this.news = news;
		BaseExperio[] experios = BaseExperioExtractor.getInstance().extractExperios(news.getTitle(), news.getContent());
		createPack(experios);
		keywods = new String[totalKeywords];
	}
	
	/**
	 * first experio is title
	 * @param experios
	 */
	private void createPack(BaseExperio[] experios) {
		
		pack = new CloudExperiencePack[experios.length - 1];
		for(int i=0; i<experios.length; i++) {
			String[] keywords = {null, null, null};
			int weight = 1;
			if(i==0)
				weight = 2;
			BaseExperio experio = experios[i];
			System.out.println(experio.getPhrase());
			for(BaseSubject subject: experio.getSubjects()) {
				for(Object keyword : subject.getKeywords()) {
				// TODO: create regex first to eliminate 's etc.
					String key = keyword.toString();
					key = key.replaceAll("[’'`]s", "");
					key = key.replaceAll("\\W", "");
					int numKeys = weight;
					if(numberOfKeywordsMap.containsKey(key))
						numKeys = numKeys+numberOfKeywordsMap.get(key);
					numberOfKeywordsMap.put(key, numKeys+weight);
					
					keywordsArrange(keywords, key, numKeys);
					
					/*if(keywords[0]==null)
						keywords[0] = key;
					else if(key.equals(keywords[0])) {
						// do nothing
					}
					else if(numberOfKeywordsMap.get(keywords[0]) < numKeys) {
						keywords[2] = keywords[1];
						keywords[1] = keywords[0];
						keywords[0] = key;
					}
					else if(key.equals(keywords[1])) {
						// do nothing
					}
					else if(keywords[1]==null)
						keywords[1] = key;
					else if(numberOfKeywordsMap.get(keywords[1]) < numKeys) {
						keywords[2] = keywords[1];
						keywords[1] = key;
					}
					else if(key.equals(keywords[2])) {
						// do nothing
					}
					else if(keywords[2]==null)
						keywords[2] = key;
					else if(numberOfKeywordsMap.get(keywords[2])<numKeys)
						keywords[2] = key;
					*/
					System.out.println(key + ":"+(numKeys));
				}
			}

			for(String k: keywords) {
				System.out.print(k +"--");
			}
			System.out.println("--");
			System.out.println("\n\n");
		}
		
		for(Entry entry: numberOfKeywordsMap.entrySet()) {
			System.out.println(entry.getKey()+" - "+entry.getValue());
		}
	}
	
	private void keywordsArrange(String[] keywords, String key, int numKeys) {
		int length = keywords.length;
		for(int i=0; i<length;i++) {
			if(keywords[i]==null){
				keywords[i] = key;
				return;
			}

			if(keywords[i].equals(key)) {
				return;
			}
			// corner cases: keys equal and an equal key below it
			if(numberOfKeywordsMap.get(keywords[i]) < numKeys) {
				for(int j=length-1;j>i;j--) {
					if(!key.equals(keywords[j-1]))
						keywords[j] = keywords[j-1];
				}
				keywords[i] = key;
				return;
			}
		}
	}
	
	private class CloudExperiencePack {
		private String phrase;
		private String[] keywords = new String[keywordsPerPack];
		private ImageResult imgResult;
		public CloudExperiencePack(String phrase, String[] keywords, ImageResult imgResult) {
			this.phrase = phrase;
			this.keywords = keywords;
			this.imgResult = imgResult;
		}
	}
	
	public static void main(String[] args) {
		CloudNews cn = NewsTableHandler.getInstance().getNewsById(10);
		String content = cn.getContent();
		
		try {
			CloudNewsExperioPack pack = new CloudNewsExperioPack(cn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
