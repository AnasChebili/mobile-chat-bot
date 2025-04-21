package com.example.mobile_chat_bot.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(foreignKeys = @ForeignKey(
        entity = ChatSession.class,
        parentColumns = "id",
        childColumns = "sessionId",
        onDelete = ForeignKey.CASCADE
))
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int sessionId;

    public String sender;
    public String content;

    public Message(int sessionId, String sender, String content) {
        this.sessionId = sessionId;
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
