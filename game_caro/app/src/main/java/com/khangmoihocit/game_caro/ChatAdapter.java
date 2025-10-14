package com.khangmoihocit.game_caro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class ChatAdapter extends
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<String> chatMessages;
    public ChatAdapter (List<String> chatMessages) {
        this.chatMessages =
                chatMessages;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChatViewHolder (view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int
            position) {
        String message = chatMessages.get(position);
        holder.messageTextView.setText(message);
    }
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ChatViewHolder(@NonNull View itemView) {
            super (itemView);
            messageTextView =
                    itemView.findViewById(android.R.id.text1);
        }
    }
}