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
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			// Sending Information of the client.
			writer.println(clientName);
			writer.println(InetAddress.getLocalHost());

			System.out.println(String.format("\nYou are connected with %s\n", HOST_NAME));

			String message;
			do {
				message = clientInfo.readLine();
				if (message.isEmpty())
					continue;
				writer.println(message);
			} while (!message.toLowerCase().equals("exit"));
			writer.close();
			if (t.isAlive()) {
				t.stop();
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
		System.out.print("Server Host Name / IPV4 Address : ");
		HOST_NAME = clientInfo.readLine();
		System.out.print("Port  : ");
		PORT = Integer.parseInt(clientInfo.readLine());
	}

	public void run() {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			String message;
			while (true) {
				message = reader.readLine();
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