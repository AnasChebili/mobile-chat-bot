package com.example.mobile_chat_bot.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatSession {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long timestamp;

    public ChatSession(long timestamp) {
        this.timestamp = timestamp;
    }
}
