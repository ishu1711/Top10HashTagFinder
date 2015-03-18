package com.ijames.corp;

import org.apache.log4j.Logger;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A blocking message queue and the hashtags extracted.
 */
public class MessageData {
    private final static Logger log = Logger.getLogger(MessageData.class);
    private final Multiset<String> hashTags = TreeMultiset.create();
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
    private volatile long messageCount;

    /**
     * Add a message to the queue to be processed.
     *
     * @param message the message.
     */
    public void addMessage(final String message) {
        messageQueue.add(message);
        messageCount++;
        log.debug("Current Queue size: " + messageQueue.size());
    }

    /**
     * Get the total number of messages submitted for processing.
     *
     * @return the number of messages.
     */
    public long getMessageCount() {
        return messageCount;
    }

    /**
     * Removes and returns the head message in the queue, waiting if necessary until an element becomes available.
     *
     * @return the message.
     */
    public String takeMessageFromQueue() {
        String message = "";

        try {
            message = messageQueue.take();
        } catch (InterruptedException ex) {
            log.error("InterruptedException thrown: " + ex);
            Thread.currentThread().interrupt();
        }

        return message;
    }

    /**
     * Adds a hashtag to the collection.
     *
     * @param hashtag the hashtag.
     */
    public void addHashTag(final String hashtag) {
        hashTags.add(hashtag);
    }

    /**
    * Prints the top ten hashtags to standard out
    */
    public void printTopTenHashTags() {
        System.out.println("Top 10 Hashtags" + getTopHashtags(10) +
            ". Total Tweets Processed: " + getMessageCount());
    }

    /**
     * Get the top hashtags.
     *
     * @return the top hashtags and occurrence of each.
     */
    public Map<String, Integer> getTopHashtags(int maxNumberOfHashTags) {
        Set<String> sortedSet = Multisets.copyHighestCountFirst(hashTags).elementSet();
        Iterator<String> iterator = sortedSet.iterator();
        Map<String, Integer> topTerms = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < maxNumberOfHashTags; i++) {
            if (iterator.hasNext()) {
                String term = iterator.next();
                topTerms.put(term, hashTags.count(term));
            } else {
                break;
            }
        }

        return topTerms;
    }

}
