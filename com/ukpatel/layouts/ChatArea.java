package com.ukpatel.layouts;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import com.ukpatel.chatly.Message;

public class ChatArea extends JPanel {
    private JPanel messages;
    private Box vertical = Box.createVerticalBox();

    private JTextArea inputMessage;
    private JButton btnSend;

    private Font gainFont = new Font("Tahoma", Font.PLAIN, 20);
    private Font lostFont = new Font("Tahoma", Font.ITALIC, 20);
    public static final String HINT = "Type a mesage";

    public ChatArea() {
        this.setLayout(new BorderLayout());

        messages = new JPanel(new BorderLayout());
        add(messages, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());

        inputMessage = new JTextArea(1, 10);
        inputMessage.setWrapStyleWord(true);
        inputMessage.setLineWrap(true);
        inputMessage.setFont(lostFont);
        inputMessage.setText(HINT);
        inputMessage.setForeground(Color.gray);
        inputMessage.setBackground(Color.lightGray);
        inputMessage.setMaximumSize(new Dimension(500, 30));
        inputMessage.setPreferredSize(new Dimension(500, 30));
        inputMessage.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                JTextArea textArea = (JTextArea) e.getSource();
                if (textArea.getText().equals(HINT)) {
                    textArea.setText("");
                    textArea.setFont(gainFont);
                } else {
                    textArea.setText(textArea.getText());
                    textArea.setFont(gainFont);
                }
                textArea.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextArea textArea = (JTextArea) e.getSource();
                if (textArea.getText().equals(HINT) || textArea.getText().length() == 0) {
                    textArea.setText(HINT);
                    textArea.setFont(lostFont);
                    textArea.setForeground(Color.GRAY);
                } else {
                    textArea.setText(textArea.getText());
                    textArea.setFont(gainFont);
                    textArea.setForeground(Color.BLACK);
                }
            }
        });

        JScrollPane jScrollPane = new JScrollPane(inputMessage);
        inputPanel.add(jScrollPane, BorderLayout.CENTER);

        btnSend = new JButton("Send");
        btnSend.setFont(new Font("Tahoma", Font.BOLD, 25));
        inputPanel.add(btnSend, BorderLayout.LINE_END);

        add(inputPanel, BorderLayout.SOUTH);
    }

    public synchronized void addMessage(Message message, int messageType) {
        vertical.add(new MessagePanel(message, messageType));
        vertical.add(Box.createVerticalStrut(10));

        add(vertical, BorderLayout.PAGE_START);
        validate();
    }

    public JButton getBtnSend() {
        return this.btnSend;
    }

    public String getMessageText() {
        if (this.inputMessage.getText().equals(HINT)) {
            return "";
        }
        return this.inputMessage.getText();
    }
}
