package com.example.mobile_chat_bot.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_chat_bot.R;
import com.example.mobile_chat_bot.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = v.findViewById(R.id.messageTextView);
        }
    }

    public MessageAdapter(List<Message> messages) {
        messageList = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message current = messageList.get(position);
        holder.messageTextView.setText(current.getSender() + ": " + current.getContent());
        holder.messageTextView.setVisibility(View.VISIBLE);
        Log.d("ChatAdapter", "Binding message: role=" + current.getSender() + ", content=" + current.getContent());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
