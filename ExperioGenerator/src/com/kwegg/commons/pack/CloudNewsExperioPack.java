package com.kwegg.commons.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.googleapis.ajax.schema.ImageResult;
import com.kwegg.common.utils.Utils;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.extractor.BaseExperioExtractor;
import com.kwegg.models.NewsTableHandler;

public class CloudNewsExperioPack {
	private CloudNews news;
	private CloudExperiencePack[] pack;
	private HashMap<String, Integer> numberOfKeywordsMap = new HashMap<String, Integer>();
	
	private final int keywordsPerPack = 3;
	private final int totalKeywords = 3;
	
	public CloudNewsExperioPack(CloudNews news) throws IOException {
		this.news = news;
		BaseExperio[] experios = BaseExperioExtractor.getInstance().extractExperios(news.getTitle(), news.getContent());
		createPack(experios);
	}
	
	/**
	 * first experio is title
	 * @param experios
	 */
	private void createPack(BaseExperio[] experios) {
		pack = new CloudExperiencePack[experios.length - 1];
		for(int i=0; i<experios.length; i++) {
			BaseExperio experio = experios[i];
			for(BaseSubject subject: experio.getSubjects()) {
				for(Object keyword : subject.getKeywords()) {
					String keys = keyword.toString();
					Pattern p = Pattern.compile("'s");
					//keys = keys.replace(Pattern.compile("'s"), "");
					// strip punctuations
					keys = keys.replaceAll("\\W", "");
					System.out.println(keys);
				}
			}
			System.out.println("--");
		}
	}
	
	private class CloudExperiencePack {
		private String phrase;
		private String[] keywords;
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
