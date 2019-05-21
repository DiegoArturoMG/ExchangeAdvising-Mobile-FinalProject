package com.example.exchangeadvisingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.exchangeadvisingapp.Activities.MessagePrivate;
import com.example.exchangeadvisingapp.Classes.Message;
import com.example.exchangeadvisingapp.Classes.MessageReceived;
import com.example.exchangeadvisingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView userName;
    public TextView message;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.tv_UserName);
        message = (TextView) itemView.findViewById(R.id.tv_Message);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }

    public static class MessageAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<MessageReceived> MessageList;
        private Context context;

        public MessageAdapter(List<MessageReceived> messageList, Context context) {
            this.MessageList = messageList;
            this.context = context;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.message_received_list,parent,false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, int i) {
            recyclerViewHolder.userName.setText(MessageList.get(i).getUserName());
            recyclerViewHolder.message.setText(MessageList.get(i).getMessage());

            final Message msj = new Message(MessageList.get(i).getUserName(),MessageList.get(i).getMessage());

            recyclerViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(view.getContext(),MessagePrivate.class);
                    intent.putExtra("message",msj);

                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return MessageList.size();
        }

    }

}


