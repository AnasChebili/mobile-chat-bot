package com.example.mobile_chat_bot.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobile_chat_bot.model.ChatSession;
import com.example.mobile_chat_bot.model.Message;

@Database(entities = {ChatSession.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ChatSessionDao chatSessionDao();
    public abstract MessageDao messageDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "chatbot_db"
            ).allowMainThreadQueries().build(); // For now allow main thread (just for learning!)
        }
        return INSTANCE;
    }
}
