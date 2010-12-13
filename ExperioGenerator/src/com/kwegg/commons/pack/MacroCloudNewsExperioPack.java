package com.kwegg.commons.pack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.MultiFileAudioPlayer;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.extractor.BaseExperioExtractor;
import com.kwegg.models.ExperiosTableHandler;
import com.kwegg.models.NewsTableHandler;

public class MacroCloudNewsExperioPack {
	private CloudNews news;
	private HashMap<String, Integer> numberOfKeywordsMap = new HashMap<String, Integer>();
	//array sorted in increasing order
	private String[] keywords = {null,null,null,null};
	private ArrayList<String> phrases = new ArrayList<String>();
	ArrayList<String> imageGuid = new ArrayList<String>();
	private String audioFilePath = "/home/parag/work/kwegg/design/reader/1/audio/";
	String audioUrl = "{BASE_AUDIOURL}/";
	private Voice newsVoice;
	
	public MacroCloudNewsExperioPack(CloudNews news, Voice newsVoice) throws IOException {
		this.news = news;
		this.newsVoice = newsVoice;
		BaseExperio[] experios = BaseExperioExtractor.getInstance().extractExperios(news.getTitle(), news.getContent());
		createPack(experios);
	}
	
	/**
	 * first experio is title
	 * @param experios
	 */
	private void createPack(BaseExperio[] experios) {
		audioFilePath = audioFilePath+news.getId();
		File dir = new File(audioFilePath);
		if(!dir.exists())
			dir.mkdir();
		audioFilePath = dir.getAbsolutePath();
		audioUrl = audioUrl + news.getId();
		MultiFileAudioPlayer player = new MultiFileAudioPlayer(audioFilePath+"/v", javax.sound.sampled.AudioFileFormat.Type.WAVE);
		newsVoice.setAudioPlayer(player);
		for(int i=0; i<experios.length; i++) {
			String phrase = experios[i].getPhrase();
			phrases.add(phrase);
			newsVoice.speak(phrase);
			
			int weight = 1;
			if(i==0)
				weight = 2;
			BaseExperio experio = experios[i];
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
		
		for(Entry<String, Integer> entry: numberOfKeywordsMap.entrySet()) {
			keywordsArrange(keywords, entry.getKey(), entry.getValue());
		}
		
		player.drain();
		player.close();
		try {
			newsVoice.setAudioPlayer(newsVoice.getDefaultAudioPlayer());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void dumpExperio() throws Exception {
		ExperiosTableHandler.getInstance().insertCloudNewsPack(this);
	}
	
	public String getJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("title", news.getTitle());
		json.put("author", news.getAuthor());
		json.put("pubTime", news.getPublishTime());
		json.put("content", news.getContent());
		return json.toString();
	}
	
	public String getJsonExtended() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray tags = new JSONArray();
		for(String key: keywords) {
			tags.put(key);
		}
		JSONArray packArray = new JSONArray();
		for(String p: phrases) {
			packArray.put(p);
		}
		json.put("tags", tags);
		json.put("phrases", packArray);
		json.put("audioUrl", audioUrl);
		return json.toString();
	}
	
	public CloudNews getCloudNews() {
		return news;
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
		for(String p: phrases) {
			packArray.put(p);
		}
		
		json.put("tags", tags);
		json.put("phrases", packArray);
		json.put("audioUrl", audioUrl);
		return json.toString();
	}
	
	public static void main(String[] args) {
		CloudNews cn = NewsTableHandler.getInstance().getNewsById(12);
		VoiceManager voiceManager = VoiceManager.getInstance();
        String voiceName = "mbrola_us2";
        Voice newsVoice;
        newsVoice = voiceManager.getVoice(voiceName);
        newsVoice.setRate(140f);
        newsVoice.setVolume(100f);
        newsVoice.allocate();
        
        if (newsVoice == null) {
            System.err.println(
                "Cannot find a voice named "
                + voiceName + ".  Please specify a different voice.");
            System.exit(1);
        }
		try {
			MacroCloudNewsExperioPack pack = new MacroCloudNewsExperioPack(cn, newsVoice);
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
