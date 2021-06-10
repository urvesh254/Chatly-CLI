import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ukpatel.chatly.Message;
import com.ukpatel.layouts.ChatArea;
import com.ukpatel.layouts.InfoPanel;
import com.ukpatel.layouts.MessagePanel;

public class ClientGUI extends JFrame implements Runnable {

    private ChatArea chatArea;
    private InfoPanel infoPanel;
    private CardLayout card;

    private String clientName;
    private String HOST_ADDRESS;
    private int PORT;

    private Socket socket;
    private ObjectOutputStream sender;

    public ClientGUI() {
        card = new CardLayout();
        this.setLayout(card);

        infoPanel = new InfoPanel();
        add(infoPanel, "infoPanel");

        chatArea = new ChatArea();
        add(chatArea, "chatArea");

        addActionListeners();

        setSize(500, 700);
        setIconImage(new ImageIcon("./assets/Chatly_logo.png").getImage());
        setLocationRelativeTo(null);
        // setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chatly");
    }

    private void addActionListeners() {

        infoPanel.getConnectButton().addActionListener(e -> {
            connectToServer();
        });

        infoPanel.getConnectButton().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == '\n')
                    connectToServer();
            }
        });

        chatArea.getBtnSend().addActionListener(e -> {
            if (chatArea.getMessageText().isEmpty()) {
                return;
            }
            sendMessage(chatArea.getMessageText());
        });

        chatArea.getInputMessage().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    if (chatArea.getMessageText().isEmpty()) {
                        return;
                    }
                    sendMessage(chatArea.getMessageText());
                }
            }

        });

    }

    private void connectToServer() {
        if (isInvalidInfo()) {
            return;
        }
        // connect with socket.
        if (isConnected()) {
            card.show(getContentPane(), "chatArea");
            chatArea.getInputMessage().requestFocusInWindow();
        }
    }

    private void sendMessage(String messageText) {
        Message message = new Message(clientName, Message.MESSAGE_SEND, messageText);
        try {
            sender.writeObject(message);
            chatArea.addMessage(message, MessagePanel.USER_SEND);
            chatArea.clearInputMessageField();
        } catch (SocketException e) {
            showToast("Server is closed.");
        } catch (Exception e) {
            showToast("Some problem in connection. \nRestart the application and reconnect to ther server.");
        }
    }

    private boolean isConnected() {
        try {
            Thread readerThread = new Thread(this);
            socket = new Socket(HOST_ADDRESS, PORT);
            readerThread.start();
            sender = new ObjectOutputStream(socket.getOutputStream());

            // Sending Information of the client.
            sender.writeObject(new Message(clientName, Message.USER_JOIN, InetAddress.getLocalHost().toString()));
            String msg = String.format("\nYou are connected with %s\n", HOST_ADDRESS);
            chatArea.addMessage(new Message(clientName, Message.USER_JOIN, msg), MessagePanel.USER_INFO);
        } catch (UnknownHostException e) {
            showToast("\nServer is not available on " + HOST_ADDRESS);
            return false;
        } catch (ConnectException e) {
            showToast("\nNo Server running on Port " + PORT);
            return false;
        } catch (SocketException e) {
            showToast("\nServer is closed.");
            return false;
        } catch (IOException e) {
            showToast("\nInput/Output interruption.");
            return false;
        } catch (Exception e) {
            showToast("\nSomething is wrong.");
            return false;
        }
        return true;
    }

    private boolean isInvalidInfo() {

        if (infoPanel.getClientName().isEmpty()) {
            showToast("Name should not be empty.");
            return true;
        }
        if (infoPanel.getServerAddress().isEmpty()) {
            showToast("Host Name should not be empty.");
            return true;
        }
        if (infoPanel.getServerPortNo().isEmpty()) {
            showToast("Port should not be empty.");
            return true;
        }
        try {
            int portNo = Integer.parseInt(infoPanel.getServerPortNo());
            if (portNo < 1 || portNo > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showToast("Port should be in between 1 to 65535");
            return true;
        }

        clientName = infoPanel.getClientName();
        HOST_ADDRESS = infoPanel.getServerAddress();
        PORT = Integer.parseInt(infoPanel.getServerPortNo());

        return false;
    }

    public static void main(String[] args) {
        try {
            new ClientGUI().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error Message", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void run() {
        try (ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())) {

            Message message;
            while (true) {
                message = (Message) reader.readObject();
                if (message.getMessageType() == Message.MESSAGE_RECEIVE)
                    chatArea.addMessage(message, MessagePanel.USER_RECEIVE);
                else
                    chatArea.addMessage(message, MessagePanel.USER_INFO);
            }
        } catch (SocketException e) {
            showToast("Server is closed.");
            card.show(getContentPane(), "infoPanel");
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Some problem in connection. \nRestart the application and reconnect to ther server.");
        }
    }
}