package com.example.exchangeadvisingapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.exchangeadvisingapp.Classes.Message;
import com.example.exchangeadvisingapp.Classes.Student;
import com.example.exchangeadvisingapp.Classes.StudentLocalization;
import com.example.exchangeadvisingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendMessage extends AppCompatActivity {

    AutoCompleteTextView et_city;
    AutoCompleteTextView et_school;
    AutoCompleteTextView et_career;
    EditText et_aboutYou;

    private DatabaseReference databaseEducation;
    public String[] cityArreglo;
    public String[] schoolArreglo;
    public String[] careerArreglo;

    ArrayList<StudentLocalization> idStudentsLocalization;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mAuth = FirebaseAuth.getInstance();

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

        idStudentsLocalization = new ArrayList<>();

        et_city = findViewById(R.id.et_city);
        et_school = findViewById(R.id.et_school);
        et_career = findViewById(R.id.et_career);
        et_aboutYou = findViewById(R.id.et_aboutYou);

        Button btn_clean = findViewById(R.id.btn_clean);
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_city.setText("");
                et_school.setText("");
                et_career.setText("");
                et_aboutYou.setText("");
                LinearLayout ll_btn_accept_City = findViewById(R.id.ll_btn_accept_City);
                ll_btn_accept_City.setVisibility(View.VISIBLE);
                LinearLayout ll_btn_accept_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_accept_School.setVisibility(View.GONE);
                LinearLayout ll_btn_accept_Career = findViewById(R.id.ll_btn_accept_Career);
                ll_btn_accept_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_sendMessage = findViewById(R.id.ll_btn_sendMessage);
                ll_btn_sendMessage.setVisibility(View.GONE);
                et_city.setEnabled(true);
                et_school.setEnabled(false);
                et_career.setEnabled(false);
                et_aboutYou.setEnabled(false);
            }
        });

        /***************************************************************************/
        /*City*/
        et_city.setText("");
        et_school.setText("");
        et_career.setText("");

        et_city.setEnabled(true);
        et_school.setEnabled(false);
        et_career.setEnabled(false);
        et_aboutYou.setEnabled(false);


        final ArrayList<String> cityArrayList = new ArrayList<>();
        databaseEducation = FirebaseDatabase.getInstance().getReference("Education");
        databaseEducation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityArreglo = new String[]{};
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    cityArrayList.add(ds.getKey());
                }
                cityArreglo = new String[cityArrayList.size()];
                for(int i=0;i<cityArrayList.size();i++){
                    cityArreglo[i] = cityArrayList.get(i);
                }
                setCities();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*City*/

        TextView tv_Return = findViewById(R.id.tv_Return);
        tv_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });

        /*School*/
        Button btn_accept_City = findViewById(R.id.btn_accept_City);
        btn_accept_City.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_btn_accept_City = findViewById(R.id.ll_btn_accept_City);
                ll_btn_accept_City.setVisibility(View.GONE);
                LinearLayout ll_btn_accept_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_accept_School.setVisibility(View.VISIBLE);
                et_city.setEnabled(false);
                et_school.setEnabled(true);
                final ArrayList<String> schoolArrayList = new ArrayList<>();
                databaseEducation = FirebaseDatabase.getInstance().getReference("Education").child(et_city.getText().toString());
                databaseEducation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        schoolArreglo = new String[]{};
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            schoolArrayList.add(ds.getKey());
                        }
                        schoolArreglo = new String[schoolArrayList.size()];
                        for(int i=0;i<schoolArrayList.size();i++){
                            schoolArreglo[i] = schoolArrayList.get(i);
                        }
                        setSchools();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        /*School*/

        /*Career*/
        Button btn_accept_School = findViewById(R.id.btn_accept_School);
        btn_accept_School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_btn_accept_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_accept_School.setVisibility(View.GONE);
                LinearLayout ll_btn_accept_Career = findViewById(R.id.ll_btn_accept_Career);
                ll_btn_accept_Career.setVisibility(View.VISIBLE);
                et_school.setEnabled(false);
                et_career.setEnabled(true);
                final ArrayList<String> careerArrayList = new ArrayList<>();
                databaseEducation = FirebaseDatabase.getInstance().getReference("Education").child(et_city.getText().toString()).child(et_school.getText().toString());
                databaseEducation.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        careerArreglo = new String[]{};
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            careerArrayList.add(ds.getValue().toString());
                        }
                        careerArreglo = new String[careerArrayList.size()];
                        for(int i=0;i<careerArrayList.size();i++){
                            careerArreglo[i] = careerArrayList.get(i);
                        }
                        setCareers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        Button btn_accept_Career = findViewById(R.id.btn_accept_Career);
        btn_accept_Career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_btn_accept_Career = findViewById(R.id.ll_btn_accept_Career);
                ll_btn_accept_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_sendMessage = findViewById(R.id.ll_btn_sendMessage);
                ll_btn_sendMessage.setVisibility(View.VISIBLE);
                et_career.setEnabled(false);
                et_aboutYou.setEnabled(true);
            }
        });


        //**************************************************************************/
        //*****Sending message*******/ btn_sendMessage
        //**************************************************************************/

        Button btn_sendMessage = findViewById(R.id.btn_sendMessage);
        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudents();
            }
        });


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

    private void setCareers() {
        et_career.setThreshold(1);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,careerArreglo);
        et_career.setAdapter(adapterCity);
        et_career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_career.showDropDown();
            }
        });
    }

    private void setSchools() {
        et_school.setThreshold(1);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,schoolArreglo);
        et_school.setAdapter(adapterCity);
        et_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_school.showDropDown();
            }
        });
    }

    private void setCities() {
        et_city.setThreshold(1);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,cityArreglo);
        et_city.setAdapter(adapterCity);
        et_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_city.showDropDown();
            }
        });
    }

    private void getStudents() {
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference("Localization").child(et_city.getText().toString()).child(et_school.getText().toString()).child(et_career.getText().toString());

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
            idStudentsLocalization.add(new StudentLocalization(ds.getKey(),ds.getValue(String.class)));
        }

        /*Agregar a localizacion de estudiante*/
        String idStudent = mAuth.getCurrentUser().getUid();
        DatabaseReference StudentDatabaseEducationPositionEnviado = FirebaseDatabase.getInstance().getReference("Messages").child(idStudent).child("Enviados");
        DatabaseReference StudentDatabaseEducationPositionRecibido;

        DatabaseReference dbMessageEnviado;
        DatabaseReference dbMessageRecibido;

        Message msj = new Message();

        for(StudentLocalization receptor : idStudentsLocalization){
            if(!idStudent.equals(receptor.getId())){
                dbMessageEnviado = StudentDatabaseEducationPositionEnviado.push();
                msj = new Message(idStudent,receptor.getId(),et_aboutYou.getText().toString());
                dbMessageEnviado.setValue(msj);

                StudentDatabaseEducationPositionRecibido = FirebaseDatabase.getInstance().getReference("Messages").child(receptor.getId()).child("Recibidos");
                dbMessageRecibido = StudentDatabaseEducationPositionRecibido.push();
                msj = new Message(idStudent,receptor.getId(),et_aboutYou.getText().toString());
                dbMessageRecibido.setValue(msj);
            }
        }
        showMessage("You've sent your message successfully");
        updateUI();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
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
