package com.ukpatel.chatly;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JProgressBar;

public class FileSending extends Thread {

    private ObjectOutputStream writer;
    private File file;
    private JProgressBar progressBar;

    // JProgressBar, OutputStream, File
    public FileSending(File file, ObjectOutputStream writer, JProgressBar progressBar) {
        try {
            this.file = file;
            this.progressBar = progressBar;
            this.writer = writer;
        } catch (Exception e) {
            System.out.println("in FileSending");
            e.printStackTrace();
        }
        this.start();
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
                message = new Message(file, Message.FILE_SEND, byteRead, data);
                writer.writeObject(message);
                writer.flush();

                int sent = (int) ((sentBytes * 100) / totalLen);

                // Setting Progressbar value.
                progressBar.setValue(sent);
                progressBar.setString(sent + "%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
