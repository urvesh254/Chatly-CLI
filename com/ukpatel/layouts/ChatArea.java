package com.ukpatel.layouts;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import com.ukpatel.chatly.Message;

public class ChatArea extends JPanel {
    private JPanel messages;
    private JScrollPane scrollPane;
    private Box vertical = Box.createVerticalBox();

    private JTextField inputMessage;
    private JButton btnSend;

    private Font gainFont = new Font("Tahoma", Font.PLAIN, 20);
    private Font lostFont = new Font("Tahoma", Font.ITALIC, 20);
    public static final String HINT = "Type a mesage";

    public ChatArea() {
        this.setLayout(new BorderLayout());

        messages = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(messages);
        add(scrollPane, BorderLayout.CENTER);

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
        inputMessage.requestFocusInWindow();
        scrollToBottom(scrollPane);
        validate();
    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
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

    public void clearInputMessageField() {
        this.inputMessage.setText("");
    }

    public JTextField getInputMessage() {
        return this.inputMessage;
    }
}
