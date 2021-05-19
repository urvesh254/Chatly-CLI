import java.net.*;
import java.io.*;

public class Client implements Runnable {

	private static int PORT;
	private static String HOST_NAME;

	private String clientName;
	private Thread t;
	private Socket socket;
	private BufferedReader clientInfo;

	public Client() {
		try {
			t = new Thread(this);
			clientInfo = new BufferedReader(new InputStreamReader(System.in));

			// Getting client information
			getClientInfo();

			// InetAddress ipAddress = InetAddress.getByName(HOST_NAME);

			// socket = new Socket(ipAddress, PORT);
			socket = new Socket(HOST_NAME, PORT);
			t.start();
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

			// Sending Information of the client.
			writer.writeUTF(clientName);
			writer.writeUTF(InetAddress.getLocalHost().toString());

			System.out.println(String.format("\nYou are connected with %s\n", HOST_NAME));

			String message;
			do {
				message = clientInfo.readLine();
				if (message.isEmpty())
					continue;
				writer.writeUTF(message);
			} while (!message.toLowerCase().equals("exit"));
			writer.close();
			if (t.isAlive()) {
				t.interrupt();
			}
			socket.close();
			clientInfo.close();
		}  catch (UnknownHostException e) {
			System.out.println("Server is not available on " + HOST_NAME);
		} catch (ConnectException e) {
			System.out.println("No Server running on Port " + PORT);
		} catch (SocketException e) {
			System.out.println("Server is closed.");
		} catch (IOException e) {
			System.out.println("Input/Output interruption.");
		} catch (Exception e) {
			System.out.println("Something is wrong.");
		}
		System.exit(0);
	}

	private void getClientInfo() throws IOException {
		System.out.print("Name : ");
		clientName = clientInfo.readLine();
		System.out.print("Server IPV4 Address : ");
		HOST_NAME = clientInfo.readLine();
		System.out.print("Port (Default 3333): ");
		try {
			PORT = Integer.parseInt(clientInfo.readLine());
		} catch (NumberFormatException e) {
			PORT = 3333;
		}

		if (clientName.isEmpty() || HOST_NAME.isEmpty()) {
			System.out.println("\n** Please enter valid Info.**");
			System.exit(0);
		}
	}

	public void run() {
		try(DataInputStream reader = new DataInputStream(socket.getInputStream())) {

			String message;
			while (true) {
				message = reader.readUTF();
				System.out.println(message);
			}
		} catch (SocketException e) {
			System.out.println("Server is closed.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String arg[]) {
		try {
			new Client();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}