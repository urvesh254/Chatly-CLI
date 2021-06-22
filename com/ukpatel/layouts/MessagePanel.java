package com.ukpatel.layouts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private static final Font MESSAGE_FONT = new Font("Tahoma", Font.PLAIN, 18);

    private Message message;
    private JLabel fileDownloadLabel;
    private JProgressBar progressBar;
    private FontMetrics metrics;

    public MessagePanel(Message message, int messageType) {
        this.metrics = this.getFontMetrics(MESSAGE_FONT);
        this.message = message;
        this.setLayout(new BorderLayout());

        if (messageType == MessagePanel.USER_SEND) {
            if (message.getMessageType() == Message.FILE_INFO_SEND)
                this.add(getFileSendPanel(), BorderLayout.LINE_END);
            else
                this.add(getSendPanel(), BorderLayout.LINE_END);
        } else if (messageType == MessagePanel.USER_RECEIVE) {
            if (message.getMessageType() == Message.FILE_INFO)
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
        label.setFont(new Font("Tahoma", Font.BOLD, 12));
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        label.setOpaque(true);
        label.setBackground(Color.CYAN);
        infoPanel.add(label);
        return infoPanel;
    }

    private JPanel getSendPanel() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        int msgWidth = getMessageWidth(message.getMessage(), 280);
        JLabel msgLabel = new JLabel(getFormattedMessage(message.getMessage(), msgWidth));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(new Color(37, 211, 102));
        msgLabel.setFont(MESSAGE_FONT);
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

        int msgWidth = getMessageWidth(message.getMessage(), 280);
        JLabel msgLabel = new JLabel(getFormattedMessage(message.getMessage(), msgWidth));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(new Color(37, 211, 102));
        msgLabel.setFont(MESSAGE_FONT);
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

        // JLabel fileLabel = new JLabel(getFormattedMessage(message.getFile().getName(), 195));
        JLabel fileLabel = new JLabel(getFormattedMessage(message.getFile().getName(), 236));
        fileLabel.setOpaque(true);
        fileLabel.setIcon(new ImageIcon(FILE_ICON));
        fileLabel.setBackground(new Color(37, 211, 102));
        fileLabel.setFont(MESSAGE_FONT);
        fileLabel.setBorder(new EmptyBorder(10, 5, 2, 2));
        centerPanel.add(fileLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Tahoma", Font.BOLD, 12));
        centerPanel.add(progressBar, BorderLayout.SOUTH);

        sendPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(37, 211, 102));

        JLabel fileSize = new JLabel(getFileSize(message.getFile().length()), SwingConstants.RIGHT);
        fileSize.setBorder(new EmptyBorder(2, 2, 2, 0));
        fileSize.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        bottomPanel.add(fileSize, BorderLayout.LINE_START);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        bottomPanel.add(timeLabel, BorderLayout.LINE_END);

        sendPanel.add(bottomPanel, BorderLayout.SOUTH);

        return sendPanel;
    }

    private JPanel getFileReceivePanel() {
        JPanel receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());

        JLabel authorLabel = new JLabel(message.getAuthor());
        authorLabel.setOpaque(true);
        authorLabel.setBorder(new EmptyBorder(2, 2, 0, 0));
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setBackground(new Color(37, 211, 102));
        authorLabel.setFont(new Font("Time New Roman", Font.BOLD, 11));
        receivePanel.add(authorLabel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel fileLabel = new JLabel(getFormattedMessage(message.getFile().getName(), 236));
        fileLabel.setIcon(new ImageIcon(FILE_ICON));
        fileLabel.setOpaque(true);
        fileLabel.setBackground(new Color(37, 211, 102));
        fileLabel.setFont(MESSAGE_FONT);
        fileLabel.setBorder(new EmptyBorder(2, 0, 0, 2));
        centerPanel.add(fileLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Tahoma", Font.BOLD, 12));

        fileDownloadLabel = new JLabel(new ImageIcon(DOWNLOAD_ICON));
        fileDownloadLabel.setOpaque(true);
        fileDownloadLabel.setBorder(new EmptyBorder(2, 5, 2, 10));
        fileDownloadLabel.setBackground(new Color(37, 211, 102));
        fileDownloadLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                centerPanel.add(progressBar, BorderLayout.SOUTH);
                centerPanel.validate();
            }
        });
        centerPanel.add(fileDownloadLabel, BorderLayout.EAST);

        receivePanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Pannel.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(37, 211, 102));

        String size = getFileSize(Long.parseLong(message.getMessage()));
        JLabel fileSize = new JLabel(size, SwingConstants.RIGHT);
        fileSize.setBorder(new EmptyBorder(2, 2, 2, 0));
        fileSize.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        bottomPanel.add(fileSize, BorderLayout.LINE_START);

        JLabel timeLabel = new JLabel(message.getTime(), SwingConstants.RIGHT);
        timeLabel.setBorder(new EmptyBorder(0, 0, 2, 2));
        timeLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        bottomPanel.add(timeLabel, BorderLayout.LINE_END);

        receivePanel.add(bottomPanel, BorderLayout.SOUTH);

        return receivePanel;
    }

    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    public JLabel getFileDownloadLabel() {
        return this.fileDownloadLabel;
    }

    private int getMessageWidth(String msg, final int limit) {
        int width = metrics.stringWidth(msg);
        return width > limit ? limit : width;
    }

    private String getFileSize(long fileSize) {
        String s = String.valueOf(fileSize);
        int len = s.length();
        String ch = "";
        float size = 0;
        if (len > 9) {
            ch = "G";
            size = (float) fileSize / 1000000000;
        } else if (len > 6) {
            ch = "M";
            size = (float) fileSize / 1000000;
        } else if (len > 3) {
            ch = "K";
            size = (float) fileSize / 1000;
        } else {
            size = (float) fileSize;
        }
        return String.format("%.1f %sB", size, ch);
    }
}
