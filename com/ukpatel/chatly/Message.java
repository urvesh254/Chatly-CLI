package com.ukpatel.chatly;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int BUFFER_SIZE = 8192;

    // Types of the messages.
    public static final int USER_JOIN = 1;
    public static final int MESSAGE_SEND = 2;
    public static final int FILE_SEND = 3;
    public static final int MESSAGE_RECEIVE = 4;
    public static final int FILE_RECEIVE = 5;
    public static final int USER_EXIT = 6;

    private int messageType;
    private String message;
    private String author;
    private String time;

    private File file;
    private byte[] data;
    private int byteRead;

    // String Message
    public Message(String author, int messageType, String message) {
        this.author = author;
        this.messageType = messageType;
        this.message = message;
        this.time = getCurrentTimeStamp();
    }

    // File Send or Receive.
    public Message(File file, int messageType, int byteRead, byte[] data) {
        this.file = file;
        this.messageType = messageType;
        this.data = data;
        this.byteRead = byteRead;
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

    public File getFile() {
        return this.file;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getByteRead() {
        return this.byteRead;
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