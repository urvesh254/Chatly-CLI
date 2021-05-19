import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server implements Runnable {
	private static ArrayList<Socket> clients = new ArrayList<>();
	private static int PORT = 3334;
	private static String HOST_NAME;
	private static String HOST_ADDRESS;
	private Thread t;
	private String clientName;
	private Socket socket;

	public Server(Socket socket) {
		this.socket = socket;
		t = new Thread(this, "Accept");
		t.start();
	}

	public void run() {
		clients.add(socket);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			clientName = reader.readLine();
			sendOtherClients(clientName + " is join the chat.");
			System.out.println(clientName + " at " + reader.readLine() + " is join the chat.");

			String message;
			while (true) {
				message = reader.readLine();
				if (message.toLowerCase().equals("exit")) {
					throw new Exception();
				} else {
					System.out.println(clientName + " : " + message);
					sendOtherClients(clientName + " : " + message);
				}
			}

		} catch (Exception e) {
			// System.out.println(e.toString());
			System.out.println(clientName + " left the chat.");
			sendOtherClients(clientName + " left the chat.");
		}
		clients.remove(socket);
	}

	private void sendOtherClients(String message) {
		try {
			PrintWriter writer;
			for (Socket client : clients) {
				if (client == socket) {
					continue;
				}
				try {
					writer = new PrintWriter(client.getOutputStream(), true);
					writer.println(message);
				} catch (SocketException e) {
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String arg[]) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			HOST_NAME = InetAddress.getLocalHost().getHostName();
			HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();

			System.out.print("Port For Server : ");
			PORT = Integer.parseInt(reader.readLine());

			ServerSocket ss = new ServerSocket(PORT);
			System.out.println(String.format("\nServer HOST NAME : %s" , HOST_NAME));
			System.out.println(String.format("Server HOST ADDRESS : %s" , HOST_ADDRESS));
			System.out.println(String.format("Server is started on Port No.: %d\n" , PORT));

			while (true) {
				Socket socket = ss.accept();
				new Server(socket);
			}

		} catch (BindException e) {
			System.out.println("Port is already inuse. Please choose another.");
		} catch (Exception e) {
			System.out.println("Something is wrong.");
		}
	}
}