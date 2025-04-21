package com.example.mobile_chat_bot.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_chat_bot.model.ChatSession;

import java.util.List;

@Dao
public interface ChatSessionDao {
    @Insert
    long insert(ChatSession session);

    @Query("SELECT * FROM ChatSession ORDER BY timestamp DESC")
    List<ChatSession> getAllSessions();
}
