package com.example.exchangeadvisingapp.Classes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Education {

    private ArrayList<City> city;
    private ArrayList<School> school;
    private ArrayList<Career> career;

    private static final String TAG = "MyActivity";

    //private FirebaseDatabase database;
    static private DatabaseReference databaseCity;
    static private DatabaseReference databaseSchool;
    static private DatabaseReference databaseCareer;

    int i=0;
    int j=0;
    int iterCity=0;
    int iterSchool=0;

    public Education() {
        city = new ArrayList<>();
        school = new ArrayList<>();
        career = new ArrayList<>();
    }

    /*public Education(ArrayList<City> city, ArrayList<School> school, ArrayList<Career> career) {
        this.city = city;
        this.school = school;
        this.career = career;
    }*/

    public ArrayList<City> getCityEducation() {
        return city;
    }

    public ArrayList<School> getSchooEducation() {
        return school;
    }

    public ArrayList<Career> getCareerEducation() {
        return career;
    }

    public void setCityEducation(ArrayList<City> city) {
        this.city = city;
    }

    public void setSchoolEducation(ArrayList<School> school) {
        this.school = school;
    }

    public void setCareerEducation(ArrayList<Career> career) {
        this.career = career;
    }

    public void getEducation() {
        Log.d(TAG,"-------------------CITY------------------------");
        databaseCity = FirebaseDatabase.getInstance().getReference("Education");
        databaseCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d(TAG,"-------------------CITIES------------------------");
                    //city.add(ds.getKey());
                    city.add(new City(ds.getKey()));
                    //Log.d(TAG,"----------CITIES: "+ds.getKey()+"-----------------");
                }

                //Log.d(TAG,"----------CITIES-----------------");
                //Log.d(TAG,city.toString());
                Log.d(TAG,"-------------------GET_SCHOOLS------------------------");
                getSchools();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getSchools() {
        Log.d(TAG,"---------------------SCHOOL----------------------------");
        //Log.d(TAG,"----------ENTRO-----------------");

        for(i=0;i<city.size();i++){

            final String cityVal;

            //Log.d(TAG,"------------->"+city.get(i).getNombre());
            //Log.d(TAG,"------------->"+city.get(i).getNombre());

            cityVal = city.get(i).getNombre();
            //Log.d(TAG,"--------------1-------->"+cityVal);

            databaseSchool = FirebaseDatabase.getInstance().getReference("Education").child(cityVal);

            //for(j=0;j<city.size();j++){

            databaseSchool.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //Log.d(TAG,"-----------2----------->"+cityVal);


                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        school.add(new School(cityVal,ds.getKey()));
                        getCareers(cityVal,ds.getKey());
                        //cityClass.get(i).addSchool(new School(ds.getKey()));
                        //Log.d(TAG,"------City: "+cityClass.get(i).getNombre()+"----School: "+ds.getKey()+"-----------------");
                    }

                    //Log.d(TAG,school.toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //}

        }
    }

    public void getCareers(String cityIter, final String schoolIter) {
        Log.d(TAG,"---------------------CAREER---------------------------");
        //Log.d(TAG,"----------ENTRO-----------------");

        //for(iterCity=0;i<city.size();i++){
        //for(iterSchool=0;j<school.size();j++){

        final String cityVal;
        final String schoolVal;

        //cityVal = city.get(i).getNombre();

        //if(school.get(j).getCity().equals(cityVal)){

        //schoolVal = school.get(j).getNombre();

        //Log.d(TAG,"------------->"+s);
        databaseCareer = FirebaseDatabase.getInstance().getReference("Education").child(cityIter).child(schoolIter);
        databaseCareer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //career.add(ds.getValue(String.class));
                    career.add(new Career(schoolIter,ds.getValue(String.class)));
                }
                //Log.d(TAG,"-------------CAREERS--------------");
                //Log.d(TAG,career.toString());


                printAll();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //}
        //}
        //}

    }

    public void printAll() {
        Log.d(TAG,"-------------------------------------------------------");

        /*Log.d(TAG,"------------------------------------------------");
        Log.d(TAG,"-------CITY-------");
        Log.d(TAG,city.toString());
        Log.d(TAG,"-------SCHOOL-------");
        Log.d(TAG,school.toString());
        Log.d(TAG,"-------CAREER-------");
        Log.d(TAG,career.toString());
        Log.d(TAG,"------------------------------------------------");*/

        Log.d(TAG,"----------------EMPEZO IMPRESION-------------------");

        Log.d(TAG,"------------------------------------------------");

        for(i=0;i<city.size();i++){
            Log.d(TAG,city.get(i).getNombre());
        }

        Log.d(TAG,"------------------------------------------------");

        for(i=0;i<school.size();i++){
            Log.d(TAG,school.get(i).getCity() + "->" + school.get(i).getNombre());
        }

        Log.d(TAG,"------------------------------------------------");

        for(i=0;i<career.size();i++){
            Log.d(TAG,career.get(i).getSchool() + "->" + career.get(i).getNombre());
        }

        Log.d(TAG,"------------------------------------------------");

    }



}
