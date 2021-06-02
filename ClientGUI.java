import java.awt.CardLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.ukpatel.layouts.ChatArea;
import com.ukpatel.layouts.InfoPanel;

public class ClientGUI extends JFrame {

    private ChatArea chatArea;
    private InfoPanel infoPanel;
    private CardLayout card;

    private String userName;
    private String HOST_ADDRESS;
    private int PORT;

    public ClientGUI() {
        card = new CardLayout();
        this.setLayout(card);

        infoPanel = new InfoPanel();
        add(infoPanel, "infoPanel");

        chatArea = new ChatArea();
        add(chatArea, "chatArea");

        infoPanel.getConnectButton().addActionListener(e -> {
            if (isInvalidInfo()) {
                return;
            }

            // connect with socket.

            card.show(getContentPane(), "chatArea");
        });

        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chatly");
    }

    private boolean isInvalidInfo() {

        if (infoPanel.getUserName().isEmpty()) {
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

        return false;
    }

    public static void main(String[] args) {
        new ClientGUI().setVisible(true);
    }

    private String getTime() {
        //Displaying current date and time in 12 hour format with AM/PM
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time = sdf.format(new Date()).toString();
        return time;
    }

    private void showToast(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error Message", JOptionPane.WARNING_MESSAGE);
    }
}
