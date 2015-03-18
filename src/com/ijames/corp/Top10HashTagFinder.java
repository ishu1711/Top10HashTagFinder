package com.ijames.corp;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Top10HashTagFinder {

	/**
	 * @param args
	 */
	private final static Logger log = Logger.getLogger(Top10HashTagFinder.class);
    private final MessageData messageData;
    private final TwitterClient twitterClient;
    private final ExecutorService pool = Executors.newFixedThreadPool(2);
    
    public Top10HashTagFinder(final String trackedTerm) {
        messageData = new MessageData();
        twitterClient = new TwitterClient(trackedTerm, messageData);
    }

    public static void main(String[] args) {
        if (args.length == 1 & validCredentialsSupplied()) {
        	Top10HashTagFinder thtf = new Top10HashTagFinder(args[0]);
        	thtf.startTrackingTerm();
        	thtf.startProcessingMessages();
        	thtf.outputTopTenEveryThirtySeconds();
        } else {
            if (args.length != 1) {
                System.out.println("Invalid number of arguments. Usage: Top10HashTagFinder [keyword]");
            }
            System.exit(-1);
        }
    }

    private static boolean validCredentialsSupplied() {
        try {
            Twitter twitter = TwitterFactory.getSingleton();
            twitter.verifyCredentials();
            return true;
        } catch (TwitterException ex) {
            System.out.println("Please supply a valid twitter4j.properties file in your working directory. " + ex.getMessage());
            return false;
        }
    }

    private void startTrackingTerm() {
        log.info("Starting Twitter client: " + twitterClient.toString() + ".");
        pool.execute(twitterClient);
    }


    private void startProcessingMessages() {
        log.info("Starting message processor.");
        pool.execute(new MessageProcessor(messageData));
    }

    private void outputTopTenEveryThirtySeconds() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                messageData.printTopTenHashTags();
            }
        }, 0, 30000);
    }

}
