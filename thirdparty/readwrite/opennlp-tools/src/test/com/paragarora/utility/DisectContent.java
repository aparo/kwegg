package com.paragarora.utility;

import java.io.IOException;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import opennlp.tools.lang.english.SentenceDetector;
import opennlp.tools.lang.english.Tokenizer;
import opennlp.tools.lang.english.TreebankParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.sentdetect.SentenceDetectorME;

public class DisectContent {
	
	String paragraph = "The Blackstone Group (NYSE: BX), announced today that it will be investing INR 2250 million (approximately USD 50 million) in Jagran Media Network Private Limited, which will hold majority share of Jagran Prakashan Limited (“JPL”). JPL (NSE: JAGRAN.NS, BSE: JAGRAN.BO) is India’s leading media and communications group, with the group’s flagship brand, Dainik Jagran, being the most widely read newspaper in the world with a total readership of 54.6 million. Jagran Media Network Private Limited will file for necessary approvals for the investment with the Foreign Investment Promotion Board (“FIPB”) today.";
	String[] ignoreWords = {"We","we", "I", "it", "It", "He"};
	SentenceDetectorME sdetector;
	Tokenizer tokenizer;
	
	public DisectContent() {
		paragraph = "We recently brought Blekko CEO Rich Skrenta into our office to talk about why his company’s recent attempt to enter a market where two search engines hold 90% of the market share is not completely insane. The prevailing “Blekko is doomed” argument holds that because Google already does search so well, it’s fruitless to for anyone else to bother trying.";
		try {
			sdetector = new SentenceDetector("/home/parag/work/nlp/opennlp/thirdparty/readwrite/models/sentdetect/EnglishSD.bin.gz");
			//tokenizer = new Tokenizer("models/tokenize/EnglishTok.bin.gz");
			
			String[] sent = sdetector.sentDetect(paragraph);
			
			
			String dataDir = "/home/parag/work/nlp/opennlp/thirdparty/readwrite/models/";
			
			Parser parser = TreebankParser.getParser(dataDir);
			
			for(int i=0; i<sent.length; i++) {
				
				System.out.println(i+":"+sent[i]);
				
				Parse[] parses = TreebankParser.parseLine(sent[i], parser, 1);
				
				showTree(parses[0]);
				
				/*for (int pi=0,pn=parses.length;pi<pn;pi++) {
		            if (showTopK) {
		              System.out.print(pi+" "+parses[pi].getProb()+" ");
		            }
		            parses[pi].show();
		            System.out.println("type:"+parses[pi].getType()+", text:"+parses[pi].toString()+"\n");
		            
		            Parse[] test = parses[pi].getChildren()[0].getChildren();
		            for(int k=0; k<test.length; k++)
		            {
		            	test[k].show();
		            	System.out.println("type:"+test[k].getType()+", text:"+test[k].getText()+"\n");
		            }
		            
		          }*/
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showTree(Parse parse) {
		
		if(parse.getChildCount()>0) {
			for(int i=0; i<parse.getChildCount(); i++) {
				showTree(parse.getChildren()[i]);
				
				//rule1: failed
				/*if(parse.getType().equals("NP")) {
					if(parse.getParent().getType().equals("S"))
						System.out.println("label: "+parse.getLabel()+" type: "+parse.getType()+",spanStart "+parse.getSpan().getStart()+", text: "+parse.toString());
				}*/
				
				//rule2
				//works as of now to extract context of sentence
				if(parse.getType().equals("NP")) {
					boolean valid = false;
					for(Parse p: parse.getChildren()) {
						if(p.getType().equals("NNP")) {
							valid = true;
							break;
						}
					}
					if(valid) {
						System.out.println(parse.toString());
					}
					
				}
				
				//for debugging
				/*try {
					System.out.println("parent: "+parse.getParent().getType()+" type: "+parse.getType()+", text: "+parse.toString()+", parent: "+parse.getParent().toString());
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}*/
			}
		}
		else {
			//System.out.println("type: "+parse.getType()+",spanStart "+parse.getSpan().getStart()+", text: "+parse.toString());
		}
		
	}
	
	public static void main(String[] args) {
		DisectContent con = new DisectContent();
	}

}
