package com.elm.healthcareapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.elm.healthcareapp.R;
import com.elm.healthcareapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity   {
    private Spinner genderSp,bloodSp;
    private ProgressBar progressBar;
    private EditText firstname,lastname,age,email,password,phone,city,weight,height;
    private Button Signupbtn;
    private String firstNamestr , lastNamestr , gender , bloodType,emailstr,passwordstr,phonestr,citystr;
    private String ageint,wieghtint,heightint , userId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //define
        initialize();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();

            }
        });










        //spinner of gender
        ArrayAdapter<CharSequence> adapterGender= ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSp.setAdapter(adapterGender);
        genderSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //spinner of blood type
        ArrayAdapter<CharSequence>adapterBlood= ArrayAdapter.createFromResource(this,R.array.blood, android.R.layout.simple_spinner_dropdown_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSp.setAdapter(adapterBlood);
        bloodSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodType = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }


    private void check(){
        heightint = height.getText().toString();
        emailstr =email.getText().toString().trim();
        passwordstr = password.getText().toString();
        firstNamestr = firstname.getText().toString();
        lastNamestr = lastname.getText().toString();
        citystr = city.getText().toString();
        ageint = age.getText().toString();
        phonestr = "+2"+phone.getText().toString();
        wieghtint  = weight.getText().toString();

        if (emailstr.isEmpty()){
            email.setError("Enter your Email");
        }else if (!emailstr.matches(emailPattern) ) {
            email.setError("enter valid email");
        }else if (passwordstr.isEmpty()){
            password.setError("enter passwprd");
        }else if(passwordstr.length()<5) {
            password.setError("enter password more than 5 characters");
        }else if (firstNamestr.isEmpty()) {
            firstname.setError("enter your first name");
        }else if (lastNamestr.isEmpty()) {
            lastname.setError("enter your last name");
        }else if (citystr.isEmpty()) {
            city.setError("enter your city");
        }else if (ageint.isEmpty()) {
            age.setError("enter your age");
        }else if (phonestr.isEmpty()){
            phone.setError("enter your phone");
        }else if (phonestr.length()<11){
           phone.setError("enter valid number");
        }else if (wieghtint.isEmpty() ){
            weight.setError("enter your weight");
        }else if (heightint.isEmpty()){
            height.setError("enter your height");
        }else{
            signUp();
        }
    }

    private void initialize(){
        genderSp = findViewById(R.id.gender_sp);
        bloodSp = findViewById(R.id.blood_type_sp);
        firstname = findViewById(R.id.fisrtName_edt);
        lastname = findViewById(R.id.lastName_edt);
        age = findViewById(R.id.age_edt);
        email = findViewById(R.id.email_edt);
        password = findViewById(R.id.password_edt);
        phone = findViewById(R.id.phone_edt);
        city = findViewById(R.id.city_edt);
        weight = findViewById(R.id.weight_edt);
        height = findViewById(R.id.height_edt);
        Signupbtn = findViewById(R.id.signup_btn);
        progressBar = findViewById(R.id.progress);
    }

    private void signUp(){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailstr,passwordstr)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users")
                                    .document(userId);
                           User user = new User(userId,firstNamestr,lastNamestr,ageint,phonestr,bloodType,gender,emailstr,citystr,heightint,wieghtint);
                           documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                   progressBar.setVisibility(View.GONE);
                                   Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                           });

                        }else{
                            Toast.makeText(SignUpActivity.this, "Registration Failed ..try again!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
    }
}