package com.example.mobile_chat_bot;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_chat_bot.adapter.MessageAdapter;
import com.example.mobile_chat_bot.api.MistralApiClient;
import com.example.mobile_chat_bot.data.AppDatabase;
import com.example.mobile_chat_bot.model.ChatSession;
import com.example.mobile_chat_bot.model.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MistralApiClient mistralApiClient;
    private EditText inputEditText;
    private FloatingActionButton sendButton;
    private FloatingActionButton newChatButton;
    private FloatingActionButton openChatHistoryButton;
    private RecyclerView chatRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private AppDatabase db;
    private int currentSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sendButton = findViewById(R.id.sendButton);
        newChatButton = findViewById(R.id.newChatButton);
        openChatHistoryButton = findViewById(R.id.openChatHistoryButton);
        inputEditText = findViewById(R.id.inputEditText);


        mistralApiClient = new MistralApiClient();

        db = AppDatabase.getInstance(this);



        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("sessionId")) {
            currentSessionId = intent.getIntExtra("sessionId", -1);
            messageList = db.messageDao().getMessagesForSession(currentSessionId);
        } else {
            ChatSession newSession = new ChatSession(System.currentTimeMillis());
            currentSessionId = (int) db.chatSessionDao().insert(newSession);
            messageList = new ArrayList<>();
        }

        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);

        sendButton.setOnClickListener((v)->sendMessage(v));
        newChatButton.setOnClickListener(v->newChat(v));
        openChatHistoryButton.setOnClickListener(v->openHistory(v));

    }

    public void sendMessage(View v) {
        String userMessage = inputEditText.getText().toString();
        if (!userMessage.isEmpty()) {





            Message msg = new Message(currentSessionId, "user", userMessage);
            db.messageDao().insert(msg);
            messageList.add(msg);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
            inputEditText.setText("");


            mistralApiClient.sendMessage(userMessage, response -> {
                runOnUiThread(() -> {

                    Message botReply = new Message(currentSessionId, "bot", response);
                    db.messageDao().insert(botReply);
                    messageList.add(botReply);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                });
            });
        }
    }

    public void newChat(View view) {
        ChatSession newSession = new ChatSession(System.currentTimeMillis());
        currentSessionId = (int) db.chatSessionDao().insert(newSession);

        // Clear current message list and update UI
        messageList.clear();
        messageAdapter.notifyDataSetChanged();

    }

    public void openHistory(View view) {
        Intent intent = new Intent(MainActivity.this, ChatHistoryActivity.class);
        startActivity(intent);
    }
}