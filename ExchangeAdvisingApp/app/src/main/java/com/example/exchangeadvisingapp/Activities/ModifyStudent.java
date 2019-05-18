package com.example.exchangeadvisingapp.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
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

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyStudent extends AppCompatActivity {

    private TextView tv_age;
    private ImageView im_UserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    private DatePickerDialog.OnDateSetListener spd_age_listener;

    private FirebaseAuth mAuth;
    private FirebaseUser userFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseReference;

    private static String userNameAuth;
    Student studentInformation;

    CircleImageView civ_photo;
    EditText et_name;
    EditText et_lastname;
    TextView tv_dateOfBirth;
    EditText et_phoneNumber;
    AutoCompleteTextView et_city;
    AutoCompleteTextView et_school;
    AutoCompleteTextView et_career;
    EditText et_aboutYou;

    private DatabaseReference databaseEducation;
    public String[] cityArreglo;
    public String[] schoolArreglo;
    public String[] careerArreglo;

    public boolean imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_student);

        mAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Student");

        imageSelected = false;

        im_UserPhoto = findViewById(R.id.iv_userPhoto);
        et_name = findViewById(R.id.et_name);
        et_lastname = findViewById(R.id.et_lastname);
        tv_dateOfBirth = findViewById(R.id.tv_dateOfBirth);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_city = findViewById(R.id.et_city);
        et_school = findViewById(R.id.et_school);
        et_career = findViewById(R.id.et_career);
        et_aboutYou = findViewById(R.id.et_aboutYou);

        userFirebaseAuth = mAuth.getCurrentUser();
        userNameAuth = userFirebaseAuth.getUid();

        //Carga datos al inicio
        cargarDatos();

        Button btn_update = findViewById(R.id.btn_career_update);
        btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = et_name.getText().toString();
                String lastName = et_lastname.getText().toString();
                String dateOfBirth = tv_dateOfBirth.getText().toString();
                String phoneNumber = et_phoneNumber.getText().toString();
                String city = et_city.getText().toString();
                String school = et_school.getText().toString();
                String career = et_career.getText().toString();
                String aboutYou = et_aboutYou.getText().toString();

                if(name.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || phoneNumber.isEmpty() || city.isEmpty() || school.isEmpty() || career.isEmpty() || aboutYou.isEmpty() || pickedImgUri.getLastPathSegment().length() == 0){
                    showMessage("Please verify all fields");
                }else{
                    showMessage("Updating account, wait a moment...");
                    Student student = new Student(name,lastName,dateOfBirth,phoneNumber,city,school,career,aboutYou);
                    if(imageSelected == true){
                        updateUserInfo(student,pickedImgUri,mAuth.getCurrentUser());
                    }else{
                        updateUserInfo(student);
                    }

                }

            }
        });

        Button btn_signup_Education = findViewById(R.id.btn_signup_Education);
        btn_signup_Education.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = et_name.getText().toString();
                String lastName = et_lastname.getText().toString();
                String dateOfBirth = tv_dateOfBirth.getText().toString();
                String phoneNumber = et_phoneNumber.getText().toString();
                String city = et_city.getText().toString();
                String school = et_school.getText().toString();
                String career = et_career.getText().toString();
                String aboutYou = et_aboutYou.getText().toString();

                if(name.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || phoneNumber.isEmpty() || city.isEmpty() || school.isEmpty() || career.isEmpty() || aboutYou.isEmpty() || pickedImgUri.getLastPathSegment().length() == 0){
                    showMessage("Please verify all fields");
                }else{
                    showMessage("Updating account, wait a moment...");
                    Student student = new Student(name,lastName,dateOfBirth,phoneNumber,city,school,career,aboutYou);
                    if(imageSelected == true){
                        updateUserInfo(student,pickedImgUri,mAuth.getCurrentUser());
                    }else{
                        updateUserInfo(student);
                    }

                }

            }
        });

        //UserPhoto
        im_UserPhoto = findViewById(R.id.iv_userPhoto);
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
                        ModifyStudent.this,
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

        Button btn_continue_Education = findViewById(R.id.btn_continue_Education);
        btn_continue_Education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_infoPersonal = findViewById(R.id.ll_infoPersonal);
                ll_infoPersonal.setVisibility(View.GONE);
                LinearLayout ll_Education = findViewById(R.id.ll_Education);
                ll_Education.setVisibility(View.VISIBLE);
            }
        });

        TextView btn_update_return = findViewById(R.id.tv_Return);
        btn_update_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_infoPersonal = findViewById(R.id.ll_infoPersonal);
                ll_infoPersonal.setVisibility(View.VISIBLE);
                LinearLayout ll_Education = findViewById(R.id.ll_Education);
                ll_Education.setVisibility(View.GONE);
            }
        });

        /***************************************************************************/
        /*City*/
        Button btn_accept = findViewById(R.id.btn_continue_Education);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_name.getText().toString().isEmpty() || et_lastname.getText().toString().isEmpty() || tv_dateOfBirth.getText().toString().isEmpty() || et_phoneNumber.getText().toString().isEmpty() || et_aboutYou.getText().toString().isEmpty()){
                    showMessage("Please verify all fields");
                }else{
                    LinearLayout ll_infoPersonal = findViewById(R.id.ll_infoPersonal);
                    ll_infoPersonal.setVisibility(View.GONE);
                    LinearLayout ll_Education = findViewById(R.id.ll_Education);
                    ll_Education.setVisibility(View.VISIBLE);
                    LinearLayout ll_City = findViewById(R.id.ll_city);
                    ll_City.setVisibility(View.VISIBLE);
                    et_city.setText("");
                    et_school.setText("");
                    et_career.setText("");

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
                updateUI();
            }
        });

        /*School*/
        Button btn_accept_City = findViewById(R.id.btn_accept_City);
        btn_accept_City.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_School = findViewById(R.id.ll_school);
                ll_School.setVisibility(View.VISIBLE);
                LinearLayout ll_btn_accept_City = findViewById(R.id.ll_btn_accept_City);
                ll_btn_accept_City.setVisibility(View.GONE);
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
                LinearLayout ll_School = findViewById(R.id.ll_school);
                ll_School.setVisibility(View.GONE);
                LinearLayout ll_Career = findViewById(R.id.ll_career);
                ll_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_Career = findViewById(R.id.ll_btn_accept_Career);
                ll_btn_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_School.setVisibility(View.GONE);
                LinearLayout ll_btn_accept_City = findViewById(R.id.ll_btn_accept_City);
                ll_btn_accept_City.setVisibility(View.VISIBLE);
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
                LinearLayout ll_Career = findViewById(R.id.ll_career);
                ll_Career.setVisibility(View.VISIBLE);
                LinearLayout ll_btn_accept_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_accept_School.setVisibility(View.GONE);
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
                LinearLayout ll_Career = findViewById(R.id.ll_career);
                ll_Career.setVisibility(View.GONE);
                LinearLayout ll_btn_accept_School = findViewById(R.id.ll_btn_accept_School);
                ll_btn_accept_School.setVisibility(View.VISIBLE);
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
        /***************************************************************************/

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

    private void cargarDatos() {
        pickedImgUri = userFirebaseAuth.getPhotoUrl();
        im_UserPhoto.setImageURI(pickedImgUri);
        Glide.with(this).load(pickedImgUri).into(im_UserPhoto);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = userDatabase.getReference();

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

            studentInformation = new Student();

            if(ds.getKey().equals("Student")){
                studentInformation.setUsername(ds.child(userNameAuth).getValue(Student.class).getUsername());
                studentInformation.setName(ds.child(userNameAuth).getValue(Student.class).getName());
                studentInformation.setLastname(ds.child(userNameAuth).getValue(Student.class).getLastname());
                studentInformation.setDateOfBirth(ds.child(userNameAuth).getValue(Student.class).getDateOfBirth());
                studentInformation.setGender(ds.child(userNameAuth).getValue(Student.class).getGender());
                studentInformation.setPhonenumber(ds.child(userNameAuth).getValue(Student.class).getPhonenumber());
                studentInformation.setCity(ds.child(userNameAuth).getValue(Student.class).getCity());
                studentInformation.setSchool(ds.child(userNameAuth).getValue(Student.class).getSchool());
                studentInformation.setCareer(ds.child(userNameAuth).getValue(Student.class).getCareer());
                studentInformation.setAbout(ds.child(userNameAuth).getValue(Student.class).getAbout());

                et_name.setText(studentInformation.getName());
                et_lastname.setText(studentInformation.getLastname());
                tv_dateOfBirth.setText(studentInformation.getDateOfBirth());
                et_phoneNumber.setText(studentInformation.getPhonenumber());
                et_city.setText(studentInformation.getCity());
                et_school.setText(studentInformation.getSchool());
                et_career.setText(studentInformation.getCareer());
                et_aboutYou.setText(studentInformation.getAbout());
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

    private void updateUserInfo(final Student student) {
        registrarStudent(student);
        updateUI();
    }

    private void registrarStudent(final Student student) {
        student.setUsername(userFirebaseAuth.getEmail());
        student.setGender(studentInformation.getGender());

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Student");

        userDatabaseReference.child(userNameAuth).setValue(student);

        showMessage("Account updated");
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

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestForPermissions(){
        if(ContextCompat.checkSelfPermission(ModifyStudent.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ModifyStudent.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(ModifyStudent.this,"Please accept for required permissions",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(ModifyStudent.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
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
            imageSelected = true;
        }

    }
}
