package com.cocorporation.hueadvanced.messages;

/**
 * Created by Corentin on 5/27/2015.
 */
public interface MessageReceiver {
    public abstract void receiveMessage(MessagesType messagesType, Object message);
}
