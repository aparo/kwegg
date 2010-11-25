package com.kwegg.commons.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.googleapis.ajax.schema.ImageResult;
import com.googleapis.ajax.services.GoogleSearchQuery;
import com.googleapis.ajax.services.GoogleSearchQueryFactory;
import com.googleapis.ajax.services.ImageSearchQuery;
import com.googleapis.ajax.services.enumeration.ImageSize;
import com.kwegg.common.utils.Utils;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.extractor.BaseExperioExtractor;
import com.kwegg.models.NewsTableHandler;

public class MacroCloudNewsExperioPack {
	private CloudNews news;
	private HashMap<String, Integer> numberOfKeywordsMap = new HashMap<String, Integer>();
	//array sorted in increasing order
	private String[] keywords = {null,null,null,null};
	private ImageResult[] imgResults = {null,null,null,null,null,null,null};
	ArrayList<String> imageGuid = new ArrayList<String>();
	
	public MacroCloudNewsExperioPack(CloudNews news) throws IOException {
		this.news = news;
		BaseExperio[] experios = BaseExperioExtractor.getInstance().extractExperios(news.getTitle(), news.getContent());
		createPack(experios);	
	}
	
	/**
	 * first experio is title
	 * @param experios
	 */
	private void createPack(BaseExperio[] experios) {
		for(int i=0; i<experios.length; i++) {
			int weight = 1;
			if(i==0)
				weight = 2;
			BaseExperio experio = experios[i];
			System.out.println(experio.getPhrase());
			for(BaseSubject subject: experio.getSubjects()) {
				for(Object keyword : subject.getKeywords()) {
					String key = keyword.toString();
					key = key.replaceAll("[’'`]s", "");
					key = key.replaceAll("\\W", "");
					int numKeys = weight;
					if(numberOfKeywordsMap.containsKey(key))
						numKeys = numKeys+numberOfKeywordsMap.get(key);
					numberOfKeywordsMap.put(key, numKeys+weight);
				}
			}
		}
		
		for(Entry entry: numberOfKeywordsMap.entrySet()) {
			keywordsArrange(keywords, (String)entry.getKey(), (Integer)entry.getValue());
		}
		StringBuilder sbQuery = new StringBuilder("");
		int resultsNum=0;
		
		for(int keyi = 0; keyi<keywords.length; keyi++) {
			GoogleSearchQueryFactory factory = GoogleSearchQueryFactory.newInstance("applicationKey");
			ImageSearchQuery query = factory.newImageSearchQuery();
			query.withImageSize(ImageSize.MEDIUM);
			if(keyi==0) {
				sbQuery.append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				int p=0;
				if(response.list().get(0)!=null)
					imgResults[resultsNum++] = response.list().get(0);
				
				/*if(!Utils.ifArrayContains(imageGuid, ir.getImageId())) {
					imageGuid.add(ir.getImageId());
					return ir;
				}*/
			}
			else if(keyi==1) {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				if(response.list().get(0)!=null)
					imgResults[resultsNum++] = response.list().get(0);
			}
			else if(keyi==2) {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				if(response.list().get(0)!=null)
					imgResults[resultsNum++] = response.list().get(0);
			}
			else {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				for(ImageResult res: response.list()) {
					if(res!=null)
						imgResults[resultsNum++] = res;
				}
			}
		}
		for(int i=0; i<imgResults.length; i++) {
			System.out.println(imgResults[i].getUnescapedUrl() + " - " + imgResults[i].getTitle() + " - " + imgResults[i].getOriginalContextUrl());
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
	
	public static void main(String[] args) {
		CloudNews cn = NewsTableHandler.getInstance().getNewsById(14);
		
		try {
			MacroCloudNewsExperioPack pack = new MacroCloudNewsExperioPack(cn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
