package com.example.exchangeadvisingapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exchangeadvisingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button btnLogin;
    private EditText et_username;
    private EditText et_password;

    private FirebaseAuth mAuth;

    private Intent HomeActivity;
    private Intent SignupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        HomeActivity = new Intent(this,StudentMenu.class);

        SignupActivity = new Intent(this,Signup.class);

        et_username = findViewById(R.id.et_usernameLogin);
        et_password = findViewById(R.id.et_passwordLogin);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    showMessage("Please verify all fields");
                }else{
                    showMessage("Wait a moment...");
                    signIn(username,password);
                }
            }
        });

        //To Signup
        Button btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SignupActivity);
            }
        });

    }

    private void signIn(String username, String password) {
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI();
                }else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            updateUI();
        }

    }
}
