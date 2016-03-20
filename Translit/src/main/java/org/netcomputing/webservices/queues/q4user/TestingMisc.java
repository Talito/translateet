package org.netcomputing.webservices.queues.q4user;

public class TestingMisc {

	//TODO: -> testing
	public static void main(String...args) throws InterruptedException {
		String lang1 = "sampleText";
		String lang2 = "lipsum";
		String lang3 = "courier";
		String lang4 = "arial";
		
		String transStr = "Example";
		
		new Thread(new Runnable() {
			public void run() {
				Translator tr = new Translator(lang1, lang2);
				tr.translate();
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				Translator tr = new Translator(lang1, lang4);
				tr.translate();
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				Language lang = Language.getLanguage(lang1);
				lang.requestTranslation(lang2, transStr);
				lang.requestTranslation(lang4, transStr);
			}
		}).start();
		
		DBQueue us = new DBQueue();
	}
	
}
