package com.kwegg.commons.pack;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googleapis.ajax.schema.ImageResult;
import com.googleapis.ajax.services.GoogleSearchQuery;
import com.googleapis.ajax.services.GoogleSearchQueryFactory;
import com.googleapis.ajax.services.ImageSearchQuery;
import com.kwegg.common.utils.HtmlManipulator;
import com.kwegg.common.utils.Utils;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;
import com.kwegg.commons.news.CloudNews;

@Deprecated
public class CoreBareExperioPack {
	private CloudNews cloudNews;
	private ExperiencePack[] pack=null;
	ArrayList<String> imageGuid = new ArrayList<String>();

	public CoreBareExperioPack(CloudNews news) {
		this.cloudNews = news;
	}

	public void createPack(BaseExperio[] experios) {
		int numExperios = experios.length;
		pack = new ExperiencePack[numExperios];
		int i = 0;
		for (BaseExperio experio : experios) {
			String phrase = experio.getPhrase();
			String googKeyword = "";
			String[] keywords = new String[experio.getSubjects().length];
			int j = 0;
			if(experio.getSubjects().length<1) {
				pack[i++] = new ExperiencePack(phrase, null, null);
				continue;
			}
			for (BaseSubject subj : experio.getSubjects()) {
				keywords[j] = subj.getSubjectPhrase();
				googKeyword = googKeyword + "+" + keywords[j];
				j++;
			}
			googKeyword.replaceAll(" ", "%20");
			ImageResult img = getRelevantImage(googKeyword);
			pack[i++] = new ExperiencePack(phrase, img, keywords);
		}
	}

	public ImageResult getRelevantImage(String keyword) {
		GoogleSearchQueryFactory factory = GoogleSearchQueryFactory.newInstance("applicationKey");

		ImageSearchQuery query = factory.newImageSearchQuery();
		GoogleSearchQuery<ImageResult> response = query.withQuery(keyword);
		for (ImageResult ir : response.list()) {
			if(!Utils.ifArrayContains(imageGuid, ir.getImageId())) {
				imageGuid.add(ir.getImageId());
				return ir;
			}
		}
		return null;
	}

	public CloudNews getCloudNews() {
		return cloudNews;
	}

	public ExperiencePack[] getPack() {
		return pack;
	}

	public ArrayList<String> getImageGuid() {
		return imageGuid;
	}

	private class ExperiencePack {
		private String phrase;
		private ImageResult imageRes;
		private String[] keywords;

		public ExperiencePack(String phrase, ImageResult imageRes,
				String[] keywords) {
			this.phrase = phrase;
			this.imageRes = imageRes;
			this.keywords = keywords;
		}
		
		public JSONObject toJson() throws JSONException {
			JSONObject obj = new JSONObject();
			obj.put("phrase", this.phrase);
			if(this.imageRes!=null) {
				String imgTitle = HtmlManipulator.replaceHtmlEntities(this.imageRes.getTitle().replaceAll("\\<.*?>",""));
				obj.put("imageTitle", imgTitle);
				obj.put("imageUrl", imageRes.getUrl());
			}
			if(keywords!=null&&keywords.length>0) {
				obj.put("keywords", keywords);
			}
			return obj;
		}
	}
	
	/**
	 * must handle null keywords and null imageresults
	 * @return
	 * @throws JSONException 
	 */
	public String toPublish() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("title", cloudNews.getTitle());
		json.put("content", cloudNews.getContent());
		json.put("publishedTime", cloudNews.getPublishTime());
		if(cloudNews.getAuthor()!=null)
			json.put("author", cloudNews.getAuthor());
		if(pack!=null&&pack.length>0) {
			String[] packContent = new String[pack.length];
			JSONArray arr = new JSONArray();
			int j=0;
			for(ExperiencePack p:pack) {
				//packContent[j++] = p.toJson();
				arr.put(p.toJson());
			}
			//json.addProperty("newsPack", Arrays.toString(packContent));
			json.put("newsPack", arr);
		}
		return json.toString();
	}
}
