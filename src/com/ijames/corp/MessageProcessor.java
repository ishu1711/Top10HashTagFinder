package com.ijames.corp;

import java.util.StringTokenizer;

/**
 * Extracts hashtags from messages.
 */
public class MessageProcessor implements Runnable {
    private final MessageData messageData;

    /**
     * Constructs a MessageProcessor.
     *
     * @param messageData the MessageData.
     */
    public MessageProcessor(final MessageData messageData) {
        this.messageData = messageData;
    }

    @Override
    public void run() {
        while (true) {
            extractHashtagsFromMessage(messageData.takeMessageFromQueue());
        }
    }

    private void extractHashtagsFromMessage(final String message) {
        String deliminator = " \t\n\r\f,.:;?![]'";
        StringTokenizer tokenizer = new StringTokenizer(message, deliminator);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith("#")) {
                messageData.addHashTag(token);
            }
        }
    }
}
