package com.example.exchangeadvisingapp.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exchangeadvisingapp.Classes.Career;
import com.example.exchangeadvisingapp.Classes.City;
import com.example.exchangeadvisingapp.Classes.Education;
import com.example.exchangeadvisingapp.Classes.School;
import com.example.exchangeadvisingapp.Classes.Student;
import com.example.exchangeadvisingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

public class Signup extends AppCompatActivity {

    private TextView tv_age;
    private ImageView im_UserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    private DatePickerDialog.OnDateSetListener spd_age_listener;

    private FirebaseAuth mAuth;
    private DatabaseReference StudentDatabase;

    static EditText et_username;
    static EditText et_password;
    static EditText et_name;
    static EditText et_lastname;
    static TextView tv_dateOfBirth;
    static RadioGroup rg_gender;
    static RadioButton rb_Female;
    static RadioButton rb_Male;
    static EditText et_phoneNumber;
    static AutoCompleteTextView et_city;
    static AutoCompleteTextView et_school;
    static AutoCompleteTextView et_career;
    static EditText et_aboutYou;

    private DatabaseReference databaseEducation;
    public String[] cityArreglo;
    public String[] schoolArreglo;
    public String[] careerArreglo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pickedImgUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.userphoto);

        mAuth = FirebaseAuth.getInstance();
        StudentDatabase = FirebaseDatabase.getInstance().getReference("Student");

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_lastname = findViewById(R.id.et_lastname);
        tv_dateOfBirth = findViewById(R.id.tv_dateOfBirth);
        rg_gender = findViewById(R.id.rg_gender);
        rb_Female = findViewById(R.id.rb_Female);
        rb_Male = findViewById(R.id.rb_Male);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_city = findViewById(R.id.et_city);
        et_school = findViewById(R.id.et_school);
        et_career = findViewById(R.id.et_career);
        et_aboutYou = findViewById(R.id.et_aboutYou);

        Button btn_continue = findViewById(R.id.btn_signup);
        btn_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int radioButtonID = rg_gender.getCheckedRadioButtonId();
                View radioButton = rg_gender.findViewById(radioButtonID);
                int idx = rg_gender.indexOfChild(radioButton);
                RadioButton r = (RadioButton)  rg_gender.getChildAt(idx);

                String userName = et_username.getText().toString();
                String password = et_password.getText().toString();
                String name = et_name.getText().toString();
                String lastName = et_lastname.getText().toString();
                String dateOfBirth = tv_dateOfBirth.getText().toString();
                String gender = r.getText().toString();;
                String phoneNumber = et_phoneNumber.getText().toString();
                String city = et_city.getText().toString();
                String school = et_school.getText().toString();
                String career = et_career.getText().toString();
                String aboutYou = et_aboutYou.getText().toString();

                if(userName.isEmpty() || password.isEmpty() || name.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || gender.isEmpty() || phoneNumber.isEmpty() || city.isEmpty() || school.isEmpty() || career.isEmpty() || aboutYou.isEmpty()){
                    showMessage("Please verify all fields");
                }else{
                    showMessage("Wait a moment. Creating account...");
                    Student student = new Student(userName,name,lastName,dateOfBirth,gender,phoneNumber,city,school,career,aboutYou);
                    createUserAcount(student,password);
                }

            }
        });

        //UserPhoto
        im_UserPhoto = findViewById(R.id.iv_userPhotoSignup);
        im_UserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermissions();
                }else{
                    openGallery();
                }
            }
        });

        //Init Age DatePickerDialogNational Polytechnic institute
        tv_age = findViewById(R.id.tv_dateOfBirth);
        tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Signup.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        spd_age_listener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        spd_age_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = month + "/" + dayOfMonth + "/" + year;
                tv_age.setText(date);
            }
        };

        /*City*/
        Button btn_accept = findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_username.getText().toString().isEmpty() || et_password.getText().toString().isEmpty() || et_name.getText().toString().isEmpty() || et_lastname.getText().toString().isEmpty() || tv_dateOfBirth.getText().toString().isEmpty() || et_phoneNumber.getText().toString().isEmpty() || et_aboutYou.getText().toString().isEmpty() || pickedImgUri.getLastPathSegment().length() == 0){
                    showMessage("Please verify all fields");
                }else{
                    LinearLayout ll_infoPersonal = findViewById(R.id.ll_infoPersonal);
                    ll_infoPersonal.setVisibility(View.GONE);
                    LinearLayout ll_Education = findViewById(R.id.ll_Education);
                    ll_Education.setVisibility(View.VISIBLE);
                    LinearLayout ll_City = findViewById(R.id.ll_City);
                    ll_City.setVisibility(View.VISIBLE);

                    et_city.setText("");
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
                }
            }
        });
        /*City*/

        TextView tv_Return = findViewById(R.id.tv_Return);
        tv_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_Education = findViewById(R.id.ll_Education);
                ll_Education.setVisibility(View.GONE);
                LinearLayout ll_City = findViewById(R.id.ll_City);
                ll_City.setVisibility(View.GONE);
                LinearLayout ll_School = findViewById(R.id.ll_School);
                ll_School.setVisibility(View.GONE);
                LinearLayout ll_Career = findViewById(R.id.ll_Career);
                ll_Career.setVisibility(View.GONE);
                LinearLayout ll_infoPersonal = findViewById(R.id.ll_infoPersonal);
                ll_infoPersonal.setVisibility(View.VISIBLE);
            }
        });

        /*School*/
        Button btn_accept_City = findViewById(R.id.btn_accept_City);
        btn_accept_City.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_School = findViewById(R.id.ll_School);
                ll_School.setVisibility(View.VISIBLE);
                LinearLayout ll_btn_city_accept = findViewById(R.id.ll_btn_accept_City);
                ll_btn_city_accept.setVisibility(View.GONE);
                et_school.setText("");
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

        //Button btn_accept_return_School = findViewById(R.id.btn_accept_return_School);
        et_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout ll_School = findViewById(R.id.ll_School);
                ll_School.setVisibility(View.GONE);
                LinearLayout ll_btn_city_accept = findViewById(R.id.ll_btn_accept_City);
                ll_btn_city_accept.setVisibility(View.VISIBLE);
                LinearLayout ll_Career = findViewById(R.id.ll_Career);
                ll_Career.setVisibility(View.GONE);
                et_city.setText("");
                return false;
            }

        });

        /*LinearLayout ll_School = findViewById(R.id.ll_School);
        ll_School.setVisibility(View.GONE);
        LinearLayout ll_Career = findViewById(R.id.ll_Career);
        ll_Career.setVisibility(View.GONE);
        et_city.setText("");*/

        /*Career*/
        Button btn_accept_School = findViewById(R.id.btn_accept_School);
        btn_accept_School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_Career = findViewById(R.id.ll_Career);
                ll_Career.setVisibility(View.VISIBLE);
                LinearLayout ll_btn_school_accept = findViewById(R.id.ll_btn_accept_School);
                ll_btn_school_accept.setVisibility(View.GONE);
                et_career.setText("");
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

        et_school.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout ll_Career = findViewById(R.id.ll_Career);
                ll_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_school_accept = findViewById(R.id.ll_btn_accept_School);
                ll_btn_school_accept.setVisibility(View.VISIBLE);
                et_school.setText("");
                return false;
            }

        });

        et_career.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout ll_btn_accept_Career = findViewById(R.id.ll_btn_accept_Career);
                ll_btn_accept_Career.setVisibility(View.VISIBLE);
                et_career.setText("");
                return false;
            }

        });

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

    private void createUserAcount(final Student student, String password) {
        mAuth.createUserWithEmailAndPassword(student.getUsername(),password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUserInfo(student,pickedImgUri,mAuth.getCurrentUser());
                }else{
                    showMessage("Account creation failed "+task.getException().getMessage());
                }
            }
        });
    }

    private void updateUserInfo(final Student student, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photo");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(student.getName())
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            registrarStudent(student);
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }

    private void registrarStudent(Student student) {
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        StudentDatabase.child(id).setValue(student);

        /*Agregar a localizacion de estudiante*/
        DatabaseReference StudentDatabaseEducationPosition;
        StudentDatabaseEducationPosition = FirebaseDatabase.getInstance().getReference("Localization");
        String idStudent = mAuth.getCurrentUser().getUid();
        StudentDatabaseEducationPosition.child(student.getCity()).child(student.getSchool()).child(student.getCareer()).child(idStudent).setValue(mAuth.getCurrentUser().getEmail());

        showMessage("Account created");
    }

    private void updateUI() {
        Intent HomeActivity = new Intent(getApplicationContext(),Login.class);
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestForPermissions(){
        if(ContextCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Signup.this,"Please accept for required permissions",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(Signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }else{
            openGallery();
        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedImgUri = data.getData();
            im_UserPhoto.setImageURI(pickedImgUri);
        }

    }

}
