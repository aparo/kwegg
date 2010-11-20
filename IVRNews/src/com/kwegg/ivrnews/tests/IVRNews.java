package com.kwegg.ivrnews.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.news.FastFeed;
import com.kwegg.models.FeedTableHandler;
import com.kwegg.models.NewsTableHandler;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class IVRNews {
	
	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	Voice voice;
	private ArrayList<CloudNews> allNews;
	private FastFeed[] feedList;
	
	public IVRNews() throws Exception {
		constructVoice();
		feedList = new FastFeed[0];
		feedList = FeedTableHandler.getInstance().getAllFastFeeds().toArray(feedList);
	}
	
	private void constructVoice() {
		/* The VoiceManager manages all the voices for FreeTTS.
         */
        VoiceManager voiceManager = VoiceManager.getInstance();
        String voiceName = "mbrola_us1";
        Voice[] vs = voiceManager.getVoices();
        for(Voice v:vs) {
        	System.out.println(v.getName());
        }
        voice = voiceManager.getVoice(voiceName);

        if (voice == null) {
            System.err.println(
                "Cannot find a voice named "
                + voiceName + ".  Please specify a different voice.");
            System.exit(1);
        }
        
        /* Allocates the resources for the voice.
         */
        voice.allocate();
	}
	
	private void destruct() {

        /* Clean up and leave.
         */
        voice.deallocate();
	}
	
	public void menu() {
		boolean keepRunning = true;
		String option="1";
		int optionInt;
		
		while(keepRunning) {
			System.out.println();
			listenFeeds();
			try {
				System.out.println("press: ");
				option = keyboard.readLine();
				optionInt = Integer.parseInt(option);
				
				listenHeadLines(feedList[optionInt]);
				voice.speak("Press # for main menu");
				
				System.out.println("press: ");
				option = keyboard.readLine();
				if(option.equals("#"))
					continue;
				optionInt = Integer.parseInt(option);
				listenNews(allNews.get(optionInt));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void listenFeeds() {
		int num = feedList.length;
		for(int i=0; i<num; i++) {
			FastFeed feed = feedList[i];
			voice.speak("Press "+i+" for. "+feed.getName());
		}
	}
	
	public void listenHeadLines(FastFeed feed) {
		allNews = NewsTableHandler.getInstance().getAllNewsForFeed(feed, 0, 10);
		int i=0;
		voice.speak("Press one of the following options to listen to news.");
		for(CloudNews news: allNews) {
			voice.speak(i+" for. "+news.getTitle());
			i++;
		}
		
	}
	
	public void listenNews(CloudNews news) {
		String content = news.getContent();
		
		content = content.replaceAll("\\<.*?>","");
		voice.speak(content);
	}
	
	public static void main(String[] args) throws Exception {
		IVRNews ivrn = new IVRNews();
		try {
			ivrn.menu();
		}
		finally {
			ivrn.destruct();
		}
	}
 
}
