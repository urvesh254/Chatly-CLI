package com.ukpatel.layouts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.net.InetAddress;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InfoPanel extends JPanel {

    private JTextField clientName;
    private JTextField serverAddress;
    private JTextField serverPortNo;
    private JButton connect;

    public InfoPanel() {
        try {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel gameTitle = new JLabel("Chatly", SwingConstants.CENTER);
            gameTitle.setFont(new Font("Lemon", Font.BOLD, 40));
            gameTitle.setAlignmentX(CENTER_ALIGNMENT);
            gameTitle.setAlignmentY(CENTER_ALIGNMENT);
            this.add(gameTitle, BorderLayout.NORTH);

            // Getting information from user panel.
            JPanel getInfo = new JPanel();
            getInfo.setLayout(new BoxLayout(getInfo, BoxLayout.Y_AXIS));
            getInfo.setAlignmentX(CENTER_ALIGNMENT);
            getInfo.setAlignmentY(CENTER_ALIGNMENT);
            getInfo.setPreferredSize(new Dimension(500, 350));

            // Player 1 information Panel.
            JPanel playerNameInfo = new JPanel();

            JLabel playerNameLabel = new JLabel("Name :  ");
            playerNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            playerNameInfo.add(playerNameLabel);

            clientName = new JTextField(InetAddress.getLocalHost().getHostName());
            clientName.setFont(new Font("Arial", Font.PLAIN, 18));
            clientName.setPreferredSize(new Dimension(170, 30));
            playerNameInfo.add(clientName);

            // Server Host Information.
            JPanel serverHostInfo = new JPanel();

            JLabel labelServerHostInfo = new JLabel("Host Address : ");
            labelServerHostInfo.setFont(new Font("Arial", Font.BOLD, 18));
            serverHostInfo.add(labelServerHostInfo);

            serverAddress = new JTextField("ukpatel");
            serverAddress.setPreferredSize(new Dimension(170, 30));
            serverAddress.setFont(new Font("Arial", Font.PLAIN, 18));
            serverHostInfo.add(serverAddress);

            // Server Port Information.
            JPanel serverPortInfo = new JPanel();

            JLabel labelPortInfo = new JLabel("Port No. : ");
            labelPortInfo.setFont(new Font("Arial", Font.BOLD, 18));
            serverPortInfo.add(labelPortInfo);

            serverPortNo = new JTextField("54321");
            serverPortNo.setPreferredSize(new Dimension(170, 30));
            serverPortNo.setFont(new Font("Arial", Font.PLAIN, 18));
            serverPortInfo.add(serverPortNo);

            // Add all information panel into main panel(getInfo)
            getInfo.add(playerNameInfo);
            getInfo.add(serverHostInfo);
            getInfo.add(serverPortInfo);

            // Button for connecting the server.
            JPanel connectPanel = new JPanel();

            connect = new JButton("Connect");
            connect.setFont(new Font("Arial", Font.BOLD, 20));
            connect.setAlignmentX(CENTER_ALIGNMENT);
            connectPanel.add(connect);

            this.add(Box.createRigidArea(new Dimension(10, 90)));
            this.add(getInfo);
            this.add(Box.createRigidArea(new Dimension(10, 100)));
            this.add(connectPanel);
            this.add(Box.createRigidArea(new Dimension(10, 30)));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public String getClientName() {
        return this.clientName.getText();
    }

    public String getServerAddress() {
        return this.serverAddress.getText();
    }

    public String getServerPortNo() {
        return this.serverPortNo.getText();
    }

    public JButton getConnectButton() {
        return this.connect;
    }
}
