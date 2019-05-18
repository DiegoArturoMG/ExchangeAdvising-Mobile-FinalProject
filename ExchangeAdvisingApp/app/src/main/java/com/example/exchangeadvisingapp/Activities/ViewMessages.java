package com.example.exchangeadvisingapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.exchangeadvisingapp.Adapters.MessageAdapter;
import com.example.exchangeadvisingapp.Classes.Career;
import com.example.exchangeadvisingapp.Classes.City;
import com.example.exchangeadvisingapp.Classes.Education;
import com.example.exchangeadvisingapp.Classes.Message;
import com.example.exchangeadvisingapp.Classes.MessageReceived;
import com.example.exchangeadvisingapp.Classes.School;
import com.example.exchangeadvisingapp.Classes.Student;
import com.example.exchangeadvisingapp.Classes.StudentLocalization;
import com.example.exchangeadvisingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.exchangeadvisingapp.Activities.Signup.et_aboutYou;

public class ViewMessages extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "MyActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<MessageReceived> listMessages;

    private String emisorID;
    private String mensaje;
    private String emisorUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.rv_Messages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listMessages = new ArrayList<>();


        getMessagesReceived();

    }

    private void getMessagesReceived() {
        mAuth = FirebaseAuth.getInstance();
        String idStudentCurrent = mAuth.getCurrentUser().getUid();
        FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userDatabaseReference = userDatabase.getReference("Messages").child(idStudentCurrent).child("Recibidos");

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){

            emisorID = ds.getValue(Message.class).getEmisor();
            mensaje = ds.getValue(Message.class).getMensaje();

            final FirebaseDatabase userDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userDatabaseReference = userDatabase.getReference("Student").child(ds.getValue(Message.class).getEmisor());

            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue(Student.class).getName();
                    String lastname = dataSnapshot.getValue(Student.class).getLastname();

                    emisorUserName = String.format("%s %s",name,lastname);

                    Log.d(TAG,"-----"+ emisorID +"|"+ mensaje +"|"+ emisorUserName+"--------");

                    listMessages.add(new MessageReceived(emisorID, emisorUserName, mensaje));

                    adapter = new MessageAdapter(listMessages,ViewMessages.this);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        //agregarALista();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }

    private void updateUI() {
        Intent HomeActivity = new Intent(getApplicationContext(),StudentMenu.class);
        startActivity(HomeActivity);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
