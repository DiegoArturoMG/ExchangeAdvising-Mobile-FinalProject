package com.example.exchangeadvisingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exchangeadvisingapp.Classes.MessageReceived;
import com.example.exchangeadvisingapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageReceived> MessageList;
    private Context context;

    public MessageAdapter(List<MessageReceived> messageList, Context context) {
        MessageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_received_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MessageReceived msjList = MessageList.get(i);

        viewHolder.tv_UserName.setText(msjList.getUserName());
        viewHolder.tv_Message.setText(msjList.getMessage());

    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_UserName;
        public TextView tv_Message;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_UserName = (TextView) itemView.findViewById(R.id.tv_UserName);
            tv_Message = (TextView) itemView.findViewById(R.id.tv_Message);


        }
    }

}
