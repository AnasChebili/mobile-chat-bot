package com.example.mobile_chat_bot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_chat_bot.data.AppDatabase;
import com.example.mobile_chat_bot.model.ChatSession;

import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<ChatSession> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        listView = findViewById(R.id.chat_history_listview);
        sessionList = AppDatabase.getInstance(this).chatSessionDao().getAllSessions();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_chat_history,
                sessionList.stream().map(s -> "Chat @ " + new java.util.Date(s.timestamp)).toArray(String[]::new)
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ChatSession session = sessionList.get(i);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("sessionId", session.id);
            startActivity(intent);
        });
    }
}
