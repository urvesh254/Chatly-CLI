package com.ukpatel.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

public class ScanAndConnect extends AppCompatActivity {

    private CodeScanner codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_and_connect);
        Bundle bundle = getIntent().getExtras();
        String wifiIP = bundle.getString("wifiIP");
        String userName = bundle.getString("userName");
        codeScanner(wifiIP, userName);
    }

    private void codeScanner(String wifiIP, String userName) {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull @NotNull Result result) {
                String details[] = result.getText().split(",");
                if (details.length == 2) {
                    Client client = Client.getInstance(ScanAndConnect.this,
                            wifiIP,
                            userName,
                            details[0],
                            Integer.parseInt(details[1])
                    );
                    if (client.isSocketConnected()) {
                        Log.d("socket", "in main socket connected: " + client.isSocketConnected());
                        startActivity(new Intent(ScanAndConnect.this, ChatArea.class));
                        finish();
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(ScanAndConnect.this, "This QR code is not valid.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ScanAndConnect.this, MainActivity.class));
        finish();
        codeScanner.releaseResources();
        super.onBackPressed();
    }
}