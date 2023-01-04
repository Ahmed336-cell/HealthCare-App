package com.elm.healthcareapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elm.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email , password;
    private Button login;
    private String emailstr, passwordstr;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);
        init();
        mAuht = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

    }
    private void init(){
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        login = findViewById(R.id.login_btn);
    }
    private void check(){
        emailstr = email.getText().toString().trim();
        passwordstr = password.getText().toString();
        if (emailstr.isEmpty()){
            email.setError("Enter your Email");
        }else if (!emailstr.matches(emailPattern) ) {
            email.setError("enter valid email");
        }else if (passwordstr.isEmpty()) {
            password.setError("enter passwprd");
        }else{
            loginFun();
        }
    }

    private void loginFun(){
        mAuht.signInWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful()){
               Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(LoginActivity.this,MainActivity.class);
               startActivity(intent);
               finish();
           }else{
               Toast.makeText(LoginActivity.this, "Can't login check your email and password", Toast.LENGTH_SHORT).show();

           }
            }
        });
    }
}