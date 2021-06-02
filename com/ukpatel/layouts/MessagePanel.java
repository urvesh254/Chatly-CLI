package com.ukpatel.layouts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.ukpatel.chatly.Message;

public class MessagePanel extends JPanel {
    public static final int USER_INFO = 0;
    public static final int USER_SEND = 1;
    public static final int USER_RECEIVE = 2;

    public MessagePanel(Message message, int messageType) {
        this.setLayout(new BorderLayout());

        if (messageType == MessagePanel.USER_SEND) {
            this.add(getSendPanel(message), BorderLayout.LINE_END);
        } else if (messageType == MessagePanel.USER_RECEIVE) {
            this.add(getReceivePanel(message), BorderLayout.LINE_START);
        } else {
            this.add(getInfoPanel(message), BorderLayout.CENTER);
        }
    }

    private JPanel getInfoPanel(Message message) {
        JPanel infoPanel = new JPanel();
        JLabel label = new JLabel(message.getMessage());
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        label.setOpaque(true);
        label.setBackground(Color.CYAN);
        infoPanel.add(label);
        return infoPanel;
    }

    private JPanel getSendPanel(Message message) {
        JPanel sendPanel = new JPanel();
        // sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.Y_AXIS));
        sendPanel.setLayout(new BorderLayout());

        // // String formatedMessage = "<html><pre style=\"width:280px;\">" + message.getMessage() + "</pre></html>";
        // // JLabel msgLabel = new JLabel(formatedMessage);
        // // JLabel msgLabel = new JLabel(message.getMessage());
        // JTextArea msgLabel = new JTextArea(message.getMessage());
        // msgLabel.setEditable(false);
        // msgLabel.setLineWrap(true);
        // msgLabel.setWrapStyleWord(true);
        // msgLabel.setOpaque(true);
        // msgLabel.setBackground(new Color(37, 211, 102));
        // // msgLabel.setBorder(new EmptyBorder(5, 0, 5, 5));
        // sendPanel.add(msgLabel, BorderLayout.CENTER);

        String formatedMessage = "<html><p style=\"width:150px;\">" + message.getMessage() + "</p></html>";
        JLabel msgLabel = new JLabel(formatedMessage);
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

    private JPanel getReceivePanel(Message message) {
        JPanel receivePanel = new JPanel();
        receivePanel.setLayout(new BorderLayout());

        JLabel authorLabel = new JLabel(message.getAuthor());
        authorLabel.setOpaque(true);
        authorLabel.setBorder(new EmptyBorder(2, 2, 0, 0));
        authorLabel.setBackground(new Color(37, 211, 102));
        authorLabel.setFont(new Font("Time New Roman", Font.PLAIN, 11));
        receivePanel.add(authorLabel, BorderLayout.PAGE_START);

        String formatedMessage = "<html><p style=\"width:150px;\">" + message.getMessage() + "</p></html>";
        JLabel msgLabel = new JLabel(formatedMessage);
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
}
