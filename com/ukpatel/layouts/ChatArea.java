package com.ukpatel.layouts;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.ukpatel.chatly.FileSending;
import com.ukpatel.chatly.Message;

public class ChatArea extends JPanel {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private JPanel messages;
    private JScrollPane scrollPane;
    private Box vertical = Box.createVerticalBox();

    private JTextField inputMessage;
    private JButton btnSend;
    private JLabel attachmentLabel;

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

        // Side Button Panel.
        JPanel btnPanel = new JPanel();

        // Attachment label
        attachmentLabel = new JLabel(new ImageIcon("assets/attachment-35.png"));
        btnPanel.add(attachmentLabel);

        // Send Button
        btnSend = new JButton(new ImageIcon("assets/send-35.png"));
        btnSend.setFont(new Font("Tahoma", Font.BOLD, 25));
        btnPanel.add(btnSend);

        inputPanel.add(btnPanel, BorderLayout.LINE_END);

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

    // Remove all message from panel.
    public void removeAllMessages() {
        vertical.removeAll();
    }

    // Only for sending the files.
    public synchronized void addMessage(Message message, ObjectOutputStream out, int messageType) {
        MessagePanel messagePanel = new MessagePanel(message, messageType);
        vertical.add(messagePanel);
        vertical.add(Box.createVerticalStrut(10));

        // Sending file to server.
        // Sending file one at a time.
        executorService.execute(new FileSending(message, out, messagePanel.getProgressBar(), false));

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

    public JLabel getAttachmentLabel() {
        return this.attachmentLabel;
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
