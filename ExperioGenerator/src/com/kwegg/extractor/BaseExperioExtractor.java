package com.kwegg.extractor;

import java.io.IOException;
import java.util.ArrayList;

import opennlp.tools.lang.english.SentenceDetector;
import opennlp.tools.lang.english.TreebankParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.sentdetect.SentenceDetectorME;

import com.kwegg.common.utils.HtmlManipulator;
import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.experio.BaseSubject;

public class BaseExperioExtractor {
	private final SentenceDetectorME sdetector;
	private final Parser parser;
	private final String modelsDataDir = "/home/parag/work/kwegg/thirdparty/readwrite/models/";
	private static BaseExperioExtractor instance;
	/**
	 * should be singleton
	 * @throws IOException
	 */
	private BaseExperioExtractor() throws IOException {
		sdetector = new SentenceDetector(modelsDataDir+"sentdetect/EnglishSD.bin.gz");
		parser = TreebankParser.getParser(modelsDataDir);
	}
	
	public static BaseExperioExtractor getInstance() throws IOException {
		if(null==instance) {
			instance = new BaseExperioExtractor();
		}
		
		return instance;
	}
	
	public final BaseExperio[] extractExperios(String cont) {
		cont.replaceAll("\n", "").replaceAll("\t", "");
		String htmlEscaped = HtmlManipulator.textizeHtml(cont);
		String[] phrases = sdetector.sentDetect(htmlEscaped);
		BaseExperio[] be = new BaseExperio[phrases.length];
		int bei = 0;
		for(int i=0; i<phrases.length; i++) {
			ArrayList<BaseSubject> subjs = new ArrayList<BaseSubject>();
			Parse[] parses = TreebankParser.parseLine(new String(phrases[i]), parser, 1);
			//parses[0].show();
			handleParse(parses[0], subjs);
			/**
			 * arraylist to array
			 * bloody hell, do something after trying logic works
			 */
			BaseSubject[] bss = new BaseSubject[subjs.size()];
			for(int ind=0; ind<bss.length; ind++) {
				bss[ind] = subjs.get(ind);
			}
			
			BaseExperio exp = new BaseExperio(phrases[i], bss);
			be[bei++] = exp;
		};
		return be;
	}
	
	/*
	 * first experio is of title
	 */
	public final BaseExperio[] extractExperios(String newsTitle, String newsContent) {
		String cont = new String(newsContent);
		String title = new String(newsTitle);
		cont.replaceAll("\n", "").replaceAll("\t", "");
		title.replaceAll("\n", "").replaceAll("\t", "");
		String htmlEscaped = HtmlManipulator.textizeHtml(cont);
		String htmlEscapedTitle = HtmlManipulator.textizeHtml(title);
		String[] phrases = sdetector.sentDetect(htmlEscaped);
		BaseExperio[] be = new BaseExperio[phrases.length + 1];
		int bei = 0;
		for(int i=0; i<phrases.length+1; i++) {
			ArrayList<BaseSubject> subjs = new ArrayList<BaseSubject>();
			String phrase;
			Parse[] parses;
			if(i>=1)
				phrase = phrases[i-1];
			else
				phrase = title;
			parses = TreebankParser.parseLine(new String(phrase), parser, 1);
			//parses[0].show();
			handleParse(parses[0], subjs);
			/**
			 * arraylist to array
			 * bloody hell, do something after trying logic works
			 */
			BaseSubject[] bss = new BaseSubject[subjs.size()];
			for(int ind=0; ind<bss.length; ind++) {
				bss[ind] = subjs.get(ind);
			}
			
			BaseExperio exp = new BaseExperio(phrase, bss);
			be[bei++] = exp;
		};
		return be;
	}
	
	public final void extractCloudNewsExperioPack(String title, String content) {
		
	}
	
	public void handleParse(Parse phrases, ArrayList<BaseSubject> subjs) {
		boolean validSubject = false;
		String phrase = phrases.toString();
		ArrayList<String> keywords = new ArrayList<String>();
		if(phrases.getType().equals("NP")) {
			for(Parse pa:phrases.getChildren()) {
				if(pa.getType().contains("NNP")) {
					validSubject = true;
					keywords.add(pa.toString());
				}
				// use his if we need to break CC
				/*
				if(pa.getType().equals("CC")) {
					String[] splitted = phrase.split(pa.toString(), 2);
					BaseSubject subject = new BaseSubject(splitted[0], keywords.toArray());
					subjs.add(subject);
					phrase = splitted[1];
					keywords.clear();
				}*/
			}
		}
		if(validSubject) {
			BaseSubject subject = new BaseSubject(phrase, keywords.toArray());
			subjs.add(subject);
		}
		else {
			for(Parse ps:phrases.getChildren()) {
				handleParse(ps, subjs);
			}
		}
	}
	
	public static void main(String[] args) {
		String phrase = "BringIt did so by inserting its platform for tournament-style mini-games inside RockYou’s Zoo World game on Facebook. BringIt helped RockYou improve how much money it makes from each user and how long its users stay engaged with RockYou’s game. BringIt is also announcing it has raised $1.5 million in funding.";
		phrase = phrase+"The BringIt platform lets users play their friends and bet the game’s virtual currency, or Zoo World Wildlife Points, without having to leave the game to do so. They can view leaderboards, issue challenges, and play in tournaments. For Zoo World, BringIt’s Zoo Blast mini-game gets users to stay an average of six minutes and 31 seconds more for each time they log into the game. The mini-game is skinned, meaning it has the same look as the rest of the RockYou game.";
		phrase = phrase+"The eCPM is in excess of $120 for users who click-through into the BringIt mini-game. That is, BringIt generates $120 in revenue for RockYou for every 1,000 people who play its mini-game. That compares to eCPMs of around $100 or so for special ads known as offers.";
		phrase = phrase+"BringIt is helping speed the rate at which players spend their money inside the virtual economy of the game, by sinking, or removing the currency from the game at a faster rate by getting players to spend their virtual money. That leads to users spending more real money on virtual currency, said Woody Levin, chief executive of BringIt in San Francisco.";
		phrase = phrase+"Levin (pictured right) said 6 percent of users return to the Zoo Blast mini game every day, and 55 percent of users have played the game before.";

		//phrase = "Gather lot of related market data for analysis Lets come back to analysis now assuming that above mentioned sort of advice will always be given an utmost importance. First part of every plan starts with market or industry analysis. Even if your idea or plan seems overwhelming enough for you to skip this thing, you should be very strict to yourself gathering negative data for you too to make you strong enough to fight against them when the right time comes. Read lot of related blog (I prefer Google Reader ) as others’ experience is always going to help you a lot. I would rather advice you to make an account over delicious and preserve all your bookmarks there so that you can share with me or even yourself later ";
		//String phrase = "Everybody knows that Google and Facebook are killing it in online display advertising.  Google recently announced that it is on track to generate <a href='http://techcrunch.com/2010/10/20/google-display-yaho/'>$2.5 billion</a> a year in display advertising revenues, and Facebook is doing about <a href='http://techcrunch.com/2010/09/01/facebook-will-hit-2-billion-2010-revenue-says-mob-of-unofficial-facebook-spokespersons/'>$2 billion</a>, mostly from display. ";
		try {
			BaseExperio[] exps = BaseExperioExtractor.getInstance().extractExperios(phrase);
			for(BaseExperio exp: exps) {
				System.out.println(exp.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
 