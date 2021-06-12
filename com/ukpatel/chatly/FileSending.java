package com.ukpatel.chatly;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JProgressBar;

public class FileSending implements Runnable {

    private ObjectOutputStream writer;
    private final File file;
    private Message message;
    private JProgressBar progressBar;
    private final int messageType;

    // JProgressBar, OutputStream, File
    public FileSending(Message message, ObjectOutputStream writer, JProgressBar progressBar, boolean isServerSending) {
        this.message = message;
        this.file = message.getFile();
        this.progressBar = progressBar;
        this.writer = writer;
        this.messageType = isServerSending ? Message.FILE_RECEIVING : Message.FILE_SENDING;

        // Sending File Info.
        Message msg = new Message(message.getAuthor(), Message.FILE_INFO_SEND, "");
        msg.setFile(file);
        try {
            writer.writeObject(msg);
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
            Message message;
            int byteRead;
            long totalLen = file.length();
            long sentBytes = 0;
            byte[] data = new byte[Message.BUFFER_SIZE];

            while ((byteRead = reader.read(data)) != -1) {
                sentBytes += byteRead;
                message = new Message(file, messageType, byteRead, data);
                writer.writeObject(message);
                writer.flush();

                int sent = (int) ((sentBytes * 100) / totalLen);

                // Setting Progressbar value.
                progressBar.setValue(sent);
                progressBar.setString(sent + "%");
            }
            Message msg = new Message(this.message.getAuthor(), Message.FILE_SENT, "");
            msg.setFile(file);
            writer.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
