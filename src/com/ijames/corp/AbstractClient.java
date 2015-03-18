package com.ijames.corp;

/**
 * An Abstract client for retrieving messages that contain hashtags. Can be extended for target social network.
 */
public abstract class AbstractClient implements Runnable {
    private final String trackedTerm;
    private final MessageData messageData;

    public AbstractClient(final String trackedTerm, final MessageData messageData) {
        this.trackedTerm = trackedTerm;
        this.messageData = messageData;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public String getTrackedTerm() {
        return trackedTerm;
    }

    @Override
    public String toString() {
        return "AbstractClient{" +
                "trackedTerm='" + trackedTerm + '\'' +
                ", class=" + this.getClass() +
                '}';
    }
}
