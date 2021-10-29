package com.ukpatel.chatly;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ukpatel.chatly.adapter.MessageAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private static final int connectionTimeout = 4000;
    public static String clientName;
    private static Client client = null;
    private boolean isConnected = false;
    private String HOST_NAME;
    private int PORT;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;

    private Socket socket = null;
    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private AppCompatActivity context;

    private Client(AppCompatActivity context, String ipAddress, String username, String hostName, int port) {
        this.context = context;
        clientName = username;
        this.HOST_NAME = hostName;
        this.PORT = port;

        Thread readerThread = new Thread(this);

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST_NAME, PORT), connectionTimeout);
            isConnected = true;
            receiver = new ObjectInputStream(socket.getInputStream());
            sender = new ObjectOutputStream(socket.getOutputStream());

            readerThread.start();

            // Sending Information of the client.
            sender.writeObject(new Message(clientName, Message.USER_JOIN, ipAddress));

        } catch (SocketTimeoutException e) {
            showToast("Please check Host Address and Port.");
            Log.d("socket", "SocketTimeoutException : " + e);
        } catch (UnknownHostException e) {
            showToast("Server is not available on " + HOST_NAME);
        } catch (IllegalArgumentException | IOException e) {
            Log.d("socket", "IllegalArgumentException | IOException : " + e);
            showToast("No Server running on Port " + PORT);
        } catch (Exception e) {
            Log.d("socket", "in constructor: " + e.toString());
            showToast("Something is wrong.");
        }

    }

    public static Client getInstance() {
        return client;
    }

    public static Client getInstance(AppCompatActivity context, String ipAddress, String username, String hostName, int port) {
        if (client == null || !client.isSocketConnected()) {
            client = new Client(context, ipAddress, username, hostName, port);
        }
        return client;
    }

    public boolean isSocketConnected() {
        return this.isConnected;
    }

    public void setInfo(@NotNull AppCompatActivity context, @NotNull MessageAdapter history, @NotNull RecyclerView recyclerView) {
        this.context = context;
        this.messageAdapter = history;
        this.recyclerView = recyclerView;
        history.addData(new Message("", Message.USER_JOIN, String.format("\nYou are connected with %s\n", HOST_NAME)));
    }

    public void sendMessage(String message) {
        try {
            if (!socket.isOutputShutdown()) {
                Message msg = new Message(clientName, Message.MESSAGE_SEND, message);
                sender.writeObject(msg);

                messageAdapter.addData(msg);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                sender.flush();
            } else {
                throw new Exception("socket is " + socket.isConnected());
            }
        } catch (SocketException e) {
            showToast(e.toString());
        } catch (Exception e) {
            Log.d("socket", "send in exception " + e.toString());
            stopConnection();
            showDialog("Server is closed.");
        }
    }

    public void stopConnection() {
        try {
            socket.close();
            Log.d("socket", "thread closed.");
        } catch (Exception ignored) {
            Log.d("socket", "exception in thread closed.");
        }
        socket = null;
        client = null;
        isConnected = false;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) receiver.readObject();
                Log.d("message", "Received : " + message.toString());
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    messageAdapter.addData(message);
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                });
            }
        } catch (SocketException e) {
            Log.d("socket", "In thread " + e.toString());
            socket = null;
            client = null;
            isConnected = false;
            if (e.toString().contains("Connection reset")) {
                Looper.prepare();
                showDialog("Server is closed.");
                Looper.loop();
            } else if (e.toString().contains("connection abort")) {
                Looper.prepare();
                showDialog("You are disconnected due to your network issue.\nPlease connect to network and rejoin.");
                Looper.loop();
            }
        } catch (Exception e) {
            Log.d("socket", "in thread exception : " + e.toString());
        }
        socket = null;
        client = null;
        isConnected = false;
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    Log.d("socket", "Server is closed. in thread in socket exception");
                    context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                    context.finish();
                    dialogInterface.cancel();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showToast(String msg) {
        context.runOnUiThread(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
    }
}
