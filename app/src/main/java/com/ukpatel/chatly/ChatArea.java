package com.ukpatel.chatly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ukpatel.chatly.adapter.MessageAdapter;

import java.util.ArrayList;

public class ChatArea extends AppCompatActivity {

    private EditText msg;
    private Button btnSend;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        msg = findViewById(R.id.msgSend);
        btnSend = findViewById(R.id.qrScan);
        RecyclerView recyclerView = findViewById(R.id.history);
        MessageAdapter messageAdapter = new MessageAdapter(new ArrayList<Message>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        //Check for light or dark mode
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                recyclerView.setBackgroundColor(getResources().getColor(R.color.black));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                recyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }

        client = Client.getInstance();
        client.setInfo(this, messageAdapter, recyclerView);

        btnSend.setOnClickListener(view -> {
            String message = msg.getText().toString().trim();
            msg.setText("");
            if (message.isEmpty()) return;

            client.sendMessage(message);
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatArea.this);
        builder.setMessage("Do you want to leave the chat?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    client.stopConnection();
                    Toast.makeText(this, "You are disconnected.", Toast.LENGTH_SHORT).show();
                    Log.d("socket", "client stopped. in chatArea.");
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
