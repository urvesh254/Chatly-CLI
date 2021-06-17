package com.ukpatel.chatly;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JProgressBar;

public class FileSending implements Runnable {

    private ObjectOutputStream writer;
    private File file;
    private Message message;
    private JProgressBar progressBar;
    private int messageType;
    private boolean isServerSending = false;

    // For Client
    public FileSending(Message message, ObjectOutputStream writer, JProgressBar progressBar) {
        this.message = message;
        this.file = message.getFile();
        this.progressBar = progressBar;
        this.writer = writer;
        this.messageType = Message.FILE_SENDING;
    }

    // For Server.
    public FileSending(Message message, File file, ObjectOutputStream writer) {
        this.message = message;
        this.file = file;
        this.writer = writer;
        this.isServerSending = true;
        this.messageType = Message.FILE_RECEIVING;
    }

    @Override
    public void run() {
        Message rMessage;
        int byteRead;
        long totalLen = file.length();
        long sentBytes = 0;
        byte[] data = new byte[Message.BUFFER_SIZE];
        int infoType;

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file))) {

            // Sending File Info.
            infoType = isServerSending ? Message.FILE_INFO_RECEIVE : Message.FILE_INFO_SEND;
            rMessage = new Message(message.getAuthor(), infoType, "" + file.length());
            rMessage.setFile(message.getFile());
            sendMsg(rMessage);

            // Sending file data to server.
            while ((byteRead = reader.read(data)) != -1) {
                sentBytes += byteRead;
                rMessage = new Message(message.getFile(), messageType, byteRead,
                        ArraysUtils.getObjectArray(data, byteRead));

                sendMsg(rMessage);

                if (!isServerSending) {
                    int sent = (int) ((sentBytes * 100) / totalLen);

                    // Setting Progressbar value.
                    progressBar.setValue(sent);
                    progressBar.setString(sent + "%");
                }
            }

            // Nofifing the file sending is done.
            infoType = isServerSending ? Message.FILE_RECEIVED : Message.FILE_SENT;
            rMessage = new Message(this.message.getAuthor(), infoType, "" + file.length());
            rMessage.setFile(message.getFile());
            sendMsg(rMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message msg) throws Exception {
        synchronized (writer) {
            writer.writeObject(msg);
            writer.flush();
        }
    }
}
