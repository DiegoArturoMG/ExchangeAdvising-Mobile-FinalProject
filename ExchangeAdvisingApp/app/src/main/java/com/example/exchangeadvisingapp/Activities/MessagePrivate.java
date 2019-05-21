package com.example.exchangeadvisingapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.exchangeadvisingapp.Classes.Message;
import com.example.exchangeadvisingapp.R;

public class MessagePrivate extends AppCompatActivity {

    TextView tv_UserName;
    TextView tv_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_private);

        Intent intent = getIntent();

        Message msj = (Message) intent.getSerializableExtra("message");

        tv_UserName = findViewById(R.id.tv_UserNamePrivate);
        tv_Message = findViewById(R.id.tv_MessagePrivate);

        tv_UserName.setText(msj.getEmisor());
        tv_Message.setText(msj.getMensaje());

    }
}
