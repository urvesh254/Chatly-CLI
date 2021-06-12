package com.ukpatel.layouts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.ukpatel.chatly.Message;

public class MessagePanel extends JPanel {

    private static final String FILE_ICON = "assets/file-48.png";
    private static final String DOWNLOAD_ICON = "assets/download-40.png";

    public static final int USER_INFO = 0;
    public static final int USER_SEND = 1;
    public static final int USER_RECEIVE = 2;

    private JLabel fileDownloadLabel;
    private Message message;

    public MessagePanel(Message message, int messageType) {
        this.message = message;
        this.setLayout(new BorderLayout());

        if (messageType == MessagePanel.USER_SEND) {
            if (message.getMessageType() == Message.FILE_SEND)
                this.add(getFileSendPanel(), BorderLayout.LINE_END);
            else
                this.add(getSendPanel(), BorderLayout.LINE_END);
        } else if (messageType == MessagePanel.USER_RECEIVE) {
            if (message.getMessageType() == Message.FILE_RECEIVE)
                this.add(getFileReceivePanel(), BorderLayout.LINE_START);
            else
                this.add(getReceivePanel(), BorderLayout.LINE_START);
        } else {
            this.add(getInfoPanel(), BorderLayout.CENTER);
        }
    }

    private JPanel getInfoPanel() {
        JPanel infoPanel = new JPanel();
        JLabel label = new JLabel(message.getMessage());
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        label.setOpaque(true);
        label.setBackground(Color.CYAN);
        infoPanel.add(label);
        return infoPanel;
    }

    private JPanel getSendPanel() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        JLabel msgLabel = new JLabel(getFormattedMessage(message.getMessage(), 280));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(new Color(37, 211, 102));
        msgLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        msgLabel.setBorder(new EmptyBorder(2, 0, 0, 2));
        sendPanel.add(msgLabel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(37, 211, 102));
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        sendPanel.add(timeLabel, BorderLayout.PAGE_END);

        return sendPanel;
    }

    private JPanel getReceivePanel() {
        JPanel receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());

        JLabel authorLabel = new JLabel(message.getAuthor());
        authorLabel.setOpaque(true);
        authorLabel.setBorder(new EmptyBorder(2, 2, 0, 0));
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setBackground(new Color(37, 211, 102));
        authorLabel.setFont(new Font("Time New Roman", Font.BOLD, 11));
        receivePanel.add(authorLabel, BorderLayout.PAGE_START);

        JLabel msgLabel = new JLabel(getFormattedMessage(message.getMessage(), 280));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(new Color(37, 211, 102));
        msgLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        msgLabel.setBorder(new EmptyBorder(2, 0, 0, 2));
        receivePanel.add(msgLabel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setOpaque(true);
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setBackground(new Color(37, 211, 102));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        receivePanel.add(timeLabel, BorderLayout.PAGE_END);

        return receivePanel;
    }

    private static String getFormattedMessage(String message, int size) {
        StringBuilder formattedMessage = new StringBuilder();
        formattedMessage.append("<html>").append("<body>");
        formattedMessage.append("<p style=\"width: " + size + "px\">");
        formattedMessage.append(message.replaceAll("\n", "<br>"));
        formattedMessage.append("</p>").append("</body>").append("</html>");
        return formattedMessage.toString();
    }

    private JPanel getFileSendPanel() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel fileLabel = new JLabel(getFormattedMessage(message.getFile().getName(), 195));
        fileLabel.setOpaque(true);
        fileLabel.setIcon(new ImageIcon(FILE_ICON));
        fileLabel.setBackground(new Color(37, 211, 102));
        fileLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        fileLabel.setBorder(new EmptyBorder(10, 5, 2, 2));
        centerPanel.add(fileLabel, BorderLayout.CENTER);

        fileDownloadLabel = new JLabel(new ImageIcon(DOWNLOAD_ICON));
        fileDownloadLabel.setOpaque(true);
        fileDownloadLabel.setBorder(new EmptyBorder(2, 5, 2, 10));
        fileDownloadLabel.setBackground(new Color(37, 211, 102));
        centerPanel.add(fileDownloadLabel, BorderLayout.EAST);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(60);
        centerPanel.add(progressBar, BorderLayout.SOUTH);

        sendPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(37, 211, 102));
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        sendPanel.add(timeLabel, BorderLayout.PAGE_END);

        return sendPanel;
    }

    private JPanel getFileReceivePanel() {
        JPanel receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());

        JLabel authorLabel = new JLabel("Urvesh Patel");
        authorLabel.setOpaque(true);
        authorLabel.setBorder(new EmptyBorder(2, 2, 0, 0));
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setBackground(new Color(37, 211, 102));
        authorLabel.setFont(new Font("Time New Roman", Font.BOLD, 11));
        receivePanel.add(authorLabel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel fileLabel = new JLabel(getFormattedMessage(message.getFile().getName(), 195));
        fileLabel.setIcon(new ImageIcon(FILE_ICON));
        fileLabel.setOpaque(true);
        fileLabel.setBackground(new Color(37, 211, 102));
        fileLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        fileLabel.setBorder(new EmptyBorder(2, 0, 0, 2));
        centerPanel.add(fileLabel, BorderLayout.CENTER);

        fileDownloadLabel = new JLabel(new ImageIcon(DOWNLOAD_ICON));
        fileDownloadLabel.setOpaque(true);
        fileDownloadLabel.setBorder(new EmptyBorder(2, 5, 2, 10));
        fileDownloadLabel.setBackground(new Color(37, 211, 102));
        centerPanel.add(fileDownloadLabel, BorderLayout.EAST);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(60);
        centerPanel.add(progressBar, BorderLayout.SOUTH);

        receivePanel.add(centerPanel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setOpaque(true);
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setBackground(new Color(37, 211, 102));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        receivePanel.add(timeLabel, BorderLayout.PAGE_END);

        return receivePanel;
    }
}
