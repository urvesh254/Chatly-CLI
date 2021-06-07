package com.ukpatel.chatly;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    // Type of the messages.
    public static final int USER_JOIN = 1;
    public static final int MESSAGE_SEND = 2;
    public static final int MESSAGE_RECEIVE = 3;
    public static final int USER_EXIT = 4;

    private int messageType;
    private String message;
    private String author;
    private String time;

    public Message(String author, int messageType, String message) {
        this.author = author;
        this.messageType = messageType;
        this.message = message;
        this.time = getCurrentTimeStamp();
    }

    public String getAuthor() {
        return this.author;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTime() {
        return this.time;
    }

    private String getCurrentTimeStamp() {
        //Displaying current date and time in 12 hour format with AM/PM
        return new SimpleDateFormat("hh:mm aa").format(new Date());
    }

    @Override
    public String toString() {
        return this.author + " : " + this.message;
    }
}