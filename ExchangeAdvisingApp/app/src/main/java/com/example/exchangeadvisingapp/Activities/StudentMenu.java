package com.example.exchangeadvisingapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.exchangeadvisingapp.Classes.Student;
import com.example.exchangeadvisingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentMenu extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;

    private String userID;

    private Dialog popWriteMesage;

    TextView tv_username;
    CircleImageView civ_photo;
    TextView tv_nameLastname;
    TextView tv_city;
    TextView tv_school;
    TextView tv_career;
    TextView tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //userDatabaseReference = FirebaseDatabase.getInstance().getReference("Student");

        civ_photo = findViewById(R.id.civ_photouser);
        tv_username = findViewById(R.id.tv_userName);
        tv_nameLastname = findViewById(R.id.tv_name_lastname);
        tv_city = findViewById(R.id.tv_city);
        tv_school = findViewById(R.id.tv_school);
        tv_career = findViewById(R.id.tv_career);
        tv_description = findViewById(R.id.tv_description);

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        updateInfo();

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent HomeActivity = new Intent(getApplicationContext(),Login.class);
                startActivity(HomeActivity);
                finish();
            }
        });

        Button btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeActivity = new Intent(getApplicationContext(),ModifyStudent.class);
                startActivity(HomeActivity);
            }
        });


        //iniPopup();

        TextView btn_writeMessage = findViewById(R.id.tv_writeMessage);
        btn_writeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeActivity = new Intent(getApplicationContext(),SendMessage.class);
                startActivity(HomeActivity);
            }
        });

        TextView btn_viewMessages = findViewById(R.id.tv_viewMessages);
        btn_viewMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeActivity = new Intent(getApplicationContext(),ViewMessages.class);
                startActivity(HomeActivity);
                finish();
            }
        });

    }

    private void updateInfo() {
        tv_username.setText(currentUser.getEmail());
        //tv_nameLastname.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(civ_photo);


        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance();

        userDatabaseReference = userDatabase.getReference("Student");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //toastMessage("Successfully signed in with: "+user.getEmail());
                }else{
                    //toastMessage("Successfully signed out");
                }
            }
        };

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

            Student studentInformation = new Student();

            Log.d(TAG,"----------------------");
            Log.d(TAG,ds.toString());


            if(ds.getKey().equals(userID)){
                studentInformation.setName(ds.getValue(Student.class).getName());
                studentInformation.setLastname(ds.getValue(Student.class).getLastname());
                studentInformation.setCity(ds.getValue(Student.class).getCity());
                studentInformation.setSchool(ds.getValue(Student.class).getSchool());
                studentInformation.setCareer(ds.getValue(Student.class).getCareer());
                studentInformation.setAbout(ds.getValue(Student.class).getAbout());

                tv_nameLastname.setText(studentInformation.getName()+" "+studentInformation.getLastname());
                tv_city.setText(studentInformation.getCity());
                tv_school.setText(studentInformation.getSchool());
                tv_career.setText(studentInformation.getCareer());;
                tv_description.setText(studentInformation.getAbout());
            }

        }
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

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void iniPopup(){
        popWriteMesage = new Dialog(this);
        popWriteMesage.setContentView(R.layout.popup_add_message);
        popWriteMesage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWriteMesage.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popWriteMesage.getWindow().getAttributes().gravity = Gravity.CENTER;
    }


}
