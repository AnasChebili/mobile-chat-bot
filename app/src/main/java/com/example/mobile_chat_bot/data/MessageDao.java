package com.example.mobile_chat_bot.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_chat_bot.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(Message message);

    @Query("SELECT * FROM Message WHERE sessionId = :sessionId")
    List<Message> getMessagesForSession(int sessionId);
}
