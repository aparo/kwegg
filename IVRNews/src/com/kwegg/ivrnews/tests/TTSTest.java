package com.kwegg.ivrnews.tests;

import java.beans.PropertyVetoException;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


public class TTSTest {
	
	static boolean showPropertyChanges = true;
	static boolean showEvents = true;
	
	/**
     * Example of how to list all the known voices.
     */
    public static void listAllVoices() {
        System.out.println();
        System.out.println("All voices available:");        
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice[] voices = voiceManager.getVoices();
        for (int i = 0; i < voices.length; i++) {
            System.out.println("    " + voices[i].getName()
                               + " (" + voices[i].getDomain() + " domain)");
        }
    }
	
	public static void freetts() {
		/* The VoiceManager manages all the voices for FreeTTS.
         */
        VoiceManager voiceManager = VoiceManager.getInstance();
        String voiceName = "mbrola_us2";
        Voice helloVoice = voiceManager.getVoice(voiceName);
        Voice[] all = voiceManager.getVoices();
        for(Voice a:all) {
        	System.out.println(a.getName()+"-"+a.getDescription());
        }
        helloVoice.setRate(120f);
        helloVoice.setRunTitle("test");
        if (helloVoice == null) {
            System.err.println(
                "Cannot find a voice named "
                + voiceName + ".  Please specify a different voice.");
            System.exit(1);
        }
        
        /* Allocates the resources for the voice.
         */
        helloVoice.allocate();
        /*SingleFileAudioPlayer sap = new SingleFileAudioPlayer("/home/parag/Desktop/player", javax.sound.sampled.AudioFileFormat.Type.WAVE);
        helloVoice.setAudioPlayer(sap);
        helloVoice.speak("Hello and thanks a lot for giving me my voice.");
        sap.drain();
        sap.close();*/
        //helloVoice.setPitch(140f);
        //MultiFileAudioPlayer player = new MultiFileAudioPlayer("/home/parag/Desktop/v", javax.sound.sampled.AudioFileFormat.Type.WAVE);
        //helloVoice.setAudioPlayer(player);
        helloVoice.speak("Hello and thanks a lot for giving me my voice.");
        helloVoice.speak("I think this is a really missionary work happening");
        //player.drain();
        //player.close();
        
        
        System.out.println(helloVoice.getPitch()+", "+helloVoice.getRate());

        /* Clean up and leave.
         */
        helloVoice.deallocate();
	}
	
	public static void main(String[] args) throws IllegalArgumentException, PropertyVetoException, InterruptedException {
		freetts();
	}
}
