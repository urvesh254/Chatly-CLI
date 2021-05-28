package com.ukpatel.chatly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Formattable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String USER_NAME = Build.MODEL;
    private static String HOST_ADDRESS = "";
    private static int PORT = 54321;

    private Button btnConnect;
    private EditText userName, hostName, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        userName = findViewById(R.id.userName);
        hostName = findViewById(R.id.hostName);
        port = findViewById(R.id.portNo);
        btnConnect = findViewById(R.id.btnConnect);

        // Set default Info.
        userName.setText(USER_NAME);
        if (!HOST_ADDRESS.isEmpty())
            hostName.setText(HOST_ADDRESS);
        if (PORT != 0)
            port.setText(String.valueOf(PORT));

//        hostName.setText("192.168.0.125");
//        port.setText("54321");
        btnConnect.setOnClickListener(this);
    }

    private void showConnectOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please connect to Wi-Fi.")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }

    private boolean isInvalidInfo() {

        if (userName.getText().toString().isEmpty()) {
            showToast("Name should not be empty.");
            return true;
        }
        if (hostName.getText().toString().isEmpty()) {
            showToast("Host Name should not be empty.");
            return true;
        }
        if (port.getText().toString().isEmpty()) {
            showToast("Port should not be empty.");
            return true;
        }
        try {
            int portNo = Integer.parseInt(port.getText().toString());
            if (portNo < 1 || portNo > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showToast("Port should be in between 1 to 65535");
            return true;
        }

        return false;
    }

    private boolean isConnectedToWifi() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        Log.d("wifi", "" + wifiManager.isWifiEnabled());

        boolean isHotspotOn = false;
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            isHotspotOn = (Boolean) method.invoke(wifiManager);
        } catch (Exception ignored) {
        }

        return wifiManager.isWifiEnabled() || isHotspotOn;
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private String getIP() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        Log.d("wifi", ipAddress);
        return ipAddress;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (isInvalidInfo()) {
            return;
        }

        // Check if wifi is connected or not?
        if (!isConnectedToWifi()) {
            showConnectOption();
            return;
        }

        final String wifiIP = getIP();

        btnConnect.setEnabled(false);
        btnConnect.setText("Connecting...");
        new Handler().post(() -> {
            USER_NAME = userName.getText().toString();
            HOST_ADDRESS = hostName.getText().toString();
            PORT = Integer.parseInt(port.getText().toString());
            Client client = Client.getInstance(this,
                    wifiIP,
                    USER_NAME,
                    HOST_ADDRESS,
                    PORT
            );
            if (client.isSocketConnected()) {
                Log.d("socket", "in main socket connected: " + client.isSocketConnected());
                Toast.makeText(this, "Connected to server.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ChatArea.class));
                this.finish();
            } else {
                btnConnect.setText("Connect");
                btnConnect.setEnabled(true);
            }
        });
    }
}