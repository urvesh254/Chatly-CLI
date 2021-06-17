package com.ukpatel.chatly.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ukpatel.chatly.Message;
import com.ukpatel.chatly.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    private final ArrayList<Message> messages;

    private static final int MESSAGE_INFO = 0;
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVE = 2;

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_INFO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_message, parent, false);
            return new InfoViewHolder(view);
        } else if (viewType == MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message, parent, false);
            return new SendViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_message, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getClass() == InfoViewHolder.class) {
            InfoViewHolder viewHolder = (InfoViewHolder) holder;
            viewHolder.message.setText(message.getMessage());
        } else if (holder.getClass() == SendViewHolder.class) {
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.message.setText(message.getMessage());
            viewHolder.time.setText(message.getTime());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.message.setText(message.getMessage());
            viewHolder.time.setText(message.getTime());
            viewHolder.author.setText(message.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getMessageType() == Message.USER_EXIT) {
            return MESSAGE_INFO;
        } else if (message.getMessageType() == Message.USER_JOIN) {
            return MESSAGE_INFO;
        }else if (message.getMessageType() == Message.MESSAGE_RECEIVE) {
            return MESSAGE_RECEIVE;
        }  else {
            return MESSAGE_SENT;
        }
    }

    public void addData(Message message) {
        messages.add(message);
        Log.d("message", message.toString());
        notifyDataSetChanged();
    }

    public static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView time, message;

        public SendViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView time, message, author;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            author = itemView.findViewById(R.id.author);
        }
    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        public InfoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
        }
    }
}
