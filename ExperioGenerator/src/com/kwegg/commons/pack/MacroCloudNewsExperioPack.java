package com.kwegg.commons.pack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googleapis.ajax.schema.ImageResult;
import com.googleapis.ajax.services.GoogleSearchQuery;
import com.googleapis.ajax.services.GoogleSearchQueryFactory;
import com.googleapis.ajax.services.ImageSearchQuery;
import com.googleapis.ajax.services.enumeration.ImageSize;
import com.googleapis.ajax.services.enumeration.ResultSetSize;
import com.kwegg.common.utils.HtmlManipulator;
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
	private FastCloudImage[] fastImgResults = {null,null,null,null,null,null,null};
	private ArrayList<PhrasePack> phrasePack = new ArrayList<PhrasePack>();
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
			System.out.println(keywords[keyi]);
			GoogleSearchQueryFactory factory = GoogleSearchQueryFactory.newInstance("applicationKey");
			ImageSearchQuery query = factory.newImageSearchQuery();
			query.withImageSize(ImageSize.MEDIUM);
			int p=0;
			if(keyi==0) {
				sbQuery.append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				query.withResultSetSize(ResultSetSize.LARGE);
				System.out.println("size: "+response.list().size());
				try {
					while(response.list().get(p)!=null) {
						ImageResult resp = response.list().get(0);
						if(!Utils.ifArrayContains(imageGuid, resp.getImageId())) {
							imageGuid.add(resp.getImageId());
							FastCloudImage fastImage = new FastCloudImage(resp.getUrl(), resp.getTitle(), resp.getVisibleUrl());
							fastImgResults[resultsNum++] = fastImage;
							break;
						}
						else {
							p++;
						}
						
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			else if(keyi==1) {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				query.withResultSetSize(ResultSetSize.LARGE);
				System.out.println("size: "+response.list().size());
				try {
					while(response.list().get(p)!=null) {
						ImageResult resp = response.list().get(0);
						if(!Utils.ifArrayContains(imageGuid, resp.getImageId())) {
							imageGuid.add(resp.getImageId());
							FastCloudImage fastImage = new FastCloudImage(resp.getUrl(), resp.getTitle(), resp.getVisibleUrl());
							fastImgResults[resultsNum++] = fastImage;
							break;
						}
						else {
							p++;
						}
						
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			else if(keyi==2) {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				query.withResultSetSize(ResultSetSize.LARGE);
				System.out.println("size: "+response.list().size());
				try {
					while(response.list().get(p)!=null) {
						ImageResult resp = response.list().get(0);
						if(!Utils.ifArrayContains(imageGuid, resp.getImageId())) {
							imageGuid.add(resp.getImageId());
							FastCloudImage fastImage = new FastCloudImage(resp.getUrl(), resp.getTitle(), resp.getVisibleUrl());
							fastImgResults[resultsNum++] = fastImage;
							break;
						}
						else {
							p++;
						}
						
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			else {
				sbQuery.append("+").append(keywords[keyi]);
				GoogleSearchQuery<ImageResult> response = query.withQuery(sbQuery.toString());
				query.withResultSetSize(ResultSetSize.LARGE);
				System.out.println("size: "+response.list().size());
				
				try {
					for(ImageResult res: response.list()) {
						if(res!=null&&!Utils.ifArrayContains(imageGuid, res.getImageId())) {
							imageGuid.add(res.getImageId());
							FastCloudImage fastImage = new FastCloudImage(res.getUrl(), res.getTitle(), res.getVisibleUrl());
							fastImgResults[resultsNum++] = fastImage;
							if(resultsNum>=fastImgResults.length) {
								break;
							}
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
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
	
	/**
	 * for macro, return title, publishDate, author, content, pic-array, audio dir, phrase-Pack (phrase-audio)
	 * @return
	 * @throws JSONException 
	 */
	public String toPublish() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("title", news.getTitle());
		json.put("author", news.getAuthor());
		json.put("publishedDate", news.getPublishTime());
		json.put("content", news.getContent());
		
		JSONArray tags = new JSONArray();
		for(String key: keywords) {
			tags.put(key);
		}
		
		JSONArray packArray = new JSONArray();
		for(PhrasePack p: phrasePack) {
			packArray.put(p.toJson());
		}
		
		JSONArray images = new JSONArray();
		for(FastCloudImage img: fastImgResults) {
			images.put(img.toJson());
		}
		
		json.put("tags", tags);
		json.put("newsPack", packArray);
		json.put("images", images);
		
		return json.toString();
	}
	
	private class FastCloudImage {
		private String url;
		private String title;
		private String source;
		public FastCloudImage(String url, String title, String source) {
			this.url = url;
			this.title = HtmlManipulator.replaceHtmlEntities(title.replaceAll("\\<.*?>",""));;
			this.source = source;
		}
		public JSONObject toJson() throws JSONException {
			JSONObject obj = new JSONObject();
			obj.put("url", url);
			obj.put("title", title);
			obj.put("source", source);
			return obj;
		}
	}
	
	private class PhrasePack {
		private String phrase;
		// audioFile name without extension. .wav/.ogg etc.
		private String audioFile;
		
		public PhrasePack(String phrase, String audioFile) {
			this.phrase = phrase;
			this.audioFile = audioFile;
		}

		public String getPhrase() {
			return phrase;
		}

		public String getAudioFile() {
			return audioFile;
		}
		
		public JSONObject toJson() throws JSONException {
			JSONObject obj = new JSONObject();
			obj.put("phrase", phrase);
			obj.put("audioFile", audioFile);
			return obj;
		}
	}
	
	public static void main(String[] args) {
		CloudNews cn = NewsTableHandler.getInstance().getNewsById(7);
		
		try {
			MacroCloudNewsExperioPack pack = new MacroCloudNewsExperioPack(cn);
			System.out.println(pack.toPublish());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
