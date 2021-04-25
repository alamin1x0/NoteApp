package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText signupemail, signuppassword;
    private RelativeLayout sigup;
    private TextView gotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        getSupportActionBar().hide();

        signupemail = findViewById(R.id.signupemail);
        signuppassword = findViewById(R.id.signuppassword);
        sigup = findViewById(R.id.sigup);
        gotologin = findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();


        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mail = signupemail.getText().toString().trim();
                String password = signuppassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else if (password.length()<4){
                    Toast.makeText(getApplicationContext(), "Password Should Greater than 4 Digits", Toast.LENGTH_SHORT).show();
                }else {
                    //registered the user firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //send eamil verification
    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification Email is Sent, Verify and Log In Again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signup.this,MainActivity.class));
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Failed to Send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}