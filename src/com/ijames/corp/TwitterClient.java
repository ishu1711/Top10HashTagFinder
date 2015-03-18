package com.ijames.corp;

import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Utilizes the Twitter Streaming API to collect messages.
 */
public class TwitterClient extends AbstractClient {

    private final static Logger log = Logger.getLogger(TwitterClient.class);

    /**
     * Constructs a Twitter Client using the supplied MessageData object and tracked term.
     *
     * @param trackedTerm the term to track on Twitter.
     * @param messageData the data structure for the Twitter data.
     */
    public TwitterClient(final String trackedTerm, final MessageData messageData) {
        super(trackedTerm, messageData);
    }

    @Override
    public void run() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new TwitterListener(this.getMessageData()));
        twitterStream.filter(getFilterQuery());
        log.info("Start listening to the Twitter stream.");
    }

    private FilterQuery getFilterQuery() {
        FilterQuery filterQuery = new FilterQuery();
        String keywords[] = {this.getTrackedTerm()};
        filterQuery.track(keywords);
        return filterQuery;
    }

    private class TwitterListener implements StatusListener {
        private final MessageData messageData;

        public TwitterListener(MessageData messageData) {
            this.messageData = messageData;
        }

        @Override
        public void onStatus(final Status status) {
            log.debug("Received onStatus: " + status.getText());
            messageData.addMessage(status.getText());
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            log.info("Received a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            log.info("Received track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            log.info("Received scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            log.info("Received stall warning:" + warning);
        }

        @Override
        public void onException(Exception ex) {
            log.error("Received Exception: ", ex);
        }
    }

}
