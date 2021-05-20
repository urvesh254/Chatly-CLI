package com.ukpatel.chatly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatArea extends AppCompatActivity {

    private EditText msg;
    private TextView history;
    private Button btnSend;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        msg = findViewById(R.id.msgSend);
        btnSend = findViewById(R.id.btnSend);
        history = findViewById(R.id.history);
        history.setMovementMethod(new ScrollingMovementMethod());

        //Check for light or dark mode
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                history.setBackgroundColor(getResources().getColor(R.color.black));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                history.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }

        client = Client.getInstance();
        client.setInfo(this, history);

        btnSend.setOnClickListener(view -> {
            String message = msg.getText().toString().trim();
            msg.setText("");
            if (message.isEmpty()) return;

            client.sendMessage(message);
            history.append(String.format("\nYou : %s\n", message));
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
