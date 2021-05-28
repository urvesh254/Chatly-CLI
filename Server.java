import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server implements Runnable {
	private static ArrayList<Socket> clients = new ArrayList<>();
	private static int PORT;
	private static String HOST_NAME;
	private static String HOST_ADDRESS;
	private Thread readerThread;
	private String clientName;
	private Socket socket;

	public Server(Socket socket) {
		this.socket = socket;
		readerThread = new Thread(this);
		readerThread.start();
	}

	public void run() {
		clients.add(socket);
		try (ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())) {

			Message userInfo = (Message) reader.readObject();
			clientName = userInfo.getAuthor();
			String ipAddress = userInfo.getMessage();

			// sendOtherClients(clientName + " is join the chat.");
			sendOtherClients(new Message(clientName, Message.USER_JOIN, clientName + " is join the chat."));
			System.out.println(clientName + " at " + ipAddress + " is join the chat.");

			Message message;
			while (true) {
				message = (Message) reader.readObject();
				if (message.getMessageType() == Message.USER_EXIT) {
					throw new Exception();
				} else {
					System.out.println(clientName + " : " + message.getMessage());
					sendOtherClients(new Message(clientName, Message.MESSAGE, message.getMessage()));
				}
			}

		} catch (Exception e) {
			// System.out.println(e.toString());
			System.out.println(clientName + " left the chat.");
			sendOtherClients(new Message(clientName, Message.USER_EXIT, clientName + " left the chat."));
		}
		clients.remove(socket);
	}

	private void sendOtherClients(Message message) {
		try {
			ObjectOutputStream writer;
			for (Socket client : clients) {
				if (client == socket) {
					continue;
				}
				try {
					writer = new ObjectOutputStream(client.getOutputStream());
					writer.writeObject(message);
				} catch (SocketException e) {
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String arg[]) {
		ServerSocket ss = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			HOST_NAME = InetAddress.getLocalHost().getHostName();
			HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();

			System.out.print("Port For Server (Default 3333) : ");
			try {
				PORT = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.println("\nPort No. you enter is invalid.\n Server is started on default port no 3333.");
				PORT = 3333;
			}

			ss = new ServerSocket(PORT);
			System.out.println(String.format("\nServer HOST NAME : %s", HOST_NAME));
			System.out.println(String.format("Server HOST ADDRESS : %s", HOST_ADDRESS));
			System.out.println(String.format("Server is started on Port No.: %d\n", PORT));

			while (true) {
				Socket socket = ss.accept();
				new Server(socket);
			}

		} catch (BindException e) {
			System.out.println("Port is already inuse. Please choose another.");
		} catch (Exception e) {
			System.out.println("Something is wrong.");
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}