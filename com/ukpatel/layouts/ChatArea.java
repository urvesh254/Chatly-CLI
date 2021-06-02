package com.ukpatel.layouts;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import com.ukpatel.chatly.Message;

public class ChatArea extends JPanel {
    private JPanel messages;
    private Box vertical = Box.createVerticalBox();

    private JTextField inputMessage;
    private JButton btnSend;

    private Font gainFont = new Font("Tahoma", Font.PLAIN, 20);
    private Font lostFont = new Font("Tahoma", Font.ITALIC, 20);
    public static final String HINT = "Type a mesage";

    public ChatArea() {
        this.setLayout(new BorderLayout());

        messages = new JPanel(new BorderLayout());
        add(messages, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());

        inputMessage = new JTextField();
        inputMessage.setFont(lostFont);
        inputMessage.setText(HINT);
        inputMessage.setForeground(Color.gray);
        inputMessage.setBackground(Color.lightGray);
        inputMessage.setMaximumSize(new Dimension(500, 30));
        inputMessage.setPreferredSize(new Dimension(500, 30));
        inputMessage.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if (textField.getText().equals(HINT)) {
                    textField.setText("");
                    textField.setFont(gainFont);
                } else {
                    textField.setText(textField.getText());
                    textField.setFont(gainFont);
                }
                textField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if (textField.getText().equals(HINT) || textField.getText().length() == 0) {
                    textField.setText(HINT);
                    textField.setFont(lostFont);
                    textField.setForeground(Color.GRAY);
                } else {
                    textField.setText(textField.getText());
                    textField.setFont(gainFont);
                    textField.setForeground(Color.BLACK);
                }
            }
        });
        inputPanel.add(inputMessage, BorderLayout.CENTER);

        btnSend = new JButton("Send");
        btnSend.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnSend.setBackground(new Color(37, 211, 102));
        inputPanel.add(btnSend, BorderLayout.LINE_END);

        add(inputPanel, BorderLayout.SOUTH);
    }

    public synchronized void addMessage(Message message, int messageType) {
        vertical.add(new MessagePanel(message, messageType));
        vertical.add(Box.createVerticalStrut(10));

        messages.add(vertical, BorderLayout.PAGE_START);
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

    public void clearInputText() {
        this.inputMessage.setText("");
    }

    public JTextField getInputMessage() {
        return this.inputMessage;
    }
}
