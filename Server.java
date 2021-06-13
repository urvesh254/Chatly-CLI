import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.ukpatel.chatly.Message;

public class Server implements Runnable {
	private static final ArrayList<ObjectOutputStream> clientsOutputStreams = new ArrayList<>();
	private static final ArrayList<ObjectInputStream> clientsInputStreams = new ArrayList<>();
	private static final String DATA_PARENT_DIRECTORY = "Chatly_Server_Data";
	private static final String SERVER_FILE_EXTENSION = "chatly.filedata";

	private static int PORT;
	private static String HOST_NAME;
	private static String HOST_ADDRESS;
	private Thread readerThread;
	private String clientName;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;

	public Server(Socket socket) {
		readerThread = new Thread(this);
		try {
			this.writer = new ObjectOutputStream(socket.getOutputStream());
			this.reader = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
		}

		readerThread.start();
	}

	public void run() {
		try {
			clientsOutputStreams.add(writer);
			clientsInputStreams.add(reader);

			Message userInfo = (Message) reader.readObject();
			clientName = userInfo.getAuthor();
			String ipAddress = userInfo.getMessage();

			sendOtherClients(new Message(clientName, Message.USER_JOIN, clientName + " is join the chat."));
			System.out.println(clientName + " at " + ipAddress + " is join the chat.");

			Message message;
			while (true) {
				message = (Message) reader.readObject();

				switch (message.getMessageType()) {
				case Message.USER_EXIT:
					throw new Exception();
				case Message.FILE_INFO_SEND:
					initializeFileStream(message);
					System.out.println(message.getFile().getName() + "File Send Info.");
					break;
				case Message.FILE_SENDING:
					fileSendingAction(message);
					break;
				case Message.FILE_SENT:
					fileSentAction(message);
					break;
				case Message.MESSAGE_SEND:
					System.out.println(clientName + " : " + message.getMessage());
					sendOtherClients(new Message(clientName, Message.MESSAGE_RECEIVE, message.getMessage()));
				case Message.FILE_INFO_RECEIVE:
					break;
				case Message.FILE_RECEIVING:
					break;
				case Message.FILE_RECEIVED:
					break;
				default:
					System.out.println(message + " " + message.getMessageType());
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			System.out.println(clientName + " left the chat.");
			sendOtherClients(new Message("Server", Message.USER_EXIT, clientName + " left the chat."));
		} finally {
			clientsOutputStreams.remove(writer);
			clientsInputStreams.remove(reader);
		}
	}

	private DataOutputStream fileOut = null;

	private void initializeFileStream(Message message) {
		try {
			String fileName = String.format("%s_%s_%s.%s", message.getFile().getName(), message.getAuthor(),
					message.getTime().replace(":", ""), SERVER_FILE_EXTENSION);
			File file = new File(DATA_PARENT_DIRECTORY, fileName);
			fileOut = new DataOutputStream(new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fileSendingAction(Message message) {
		try {
			fileOut.write(message.getData(), 0, message.getByteRead());
			fileOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fileSentAction(Message message) {
		System.out.println("File Sent.");
		// Closing the fileOut
		if (fileOut != null) {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println(String.format("%s sent the %s file.", message.getAuthor(), message.getFile().getName()));

		// Sending other client to file info.
		Message msg = new Message(message.getAuthor(), Message.FILE_INFO_RECEIVE, "");
		msg.setFile(message.getFile());
		sendOtherClients(msg);
	}

	private void sendOtherClients(Message message) {
		try {
			for (ObjectOutputStream clientWriter : clientsOutputStreams) {
				if (clientWriter == writer) {
					continue;
				}
				try {
					clientWriter.writeObject(message);
				} catch (SocketException e) {
				}
			}
		} catch (Exception e) {
		}

	}

	public static void main(String arg[]) {
		ServerSocket ss = null;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			HOST_NAME = InetAddress.getLocalHost().getHostName();
			HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();

			// Initializing Server Data Directory.
			File directory = new File(DATA_PARENT_DIRECTORY);
			if (!directory.exists()) {
				directory.mkdir();
			}

			System.out.print("Port For Server (Default 54321) : ");
			try {
				PORT = Integer.parseInt(reader.readLine());
			} catch (NumberFormatException e) {
				System.out.println("\nPort No. you enter is invalid.\nServer is started on default port no 54321.");
				PORT = 54321;
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