package com.example.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in, if yes, redirect to MainActivity
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        // Find views by their IDs
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    public void signup(View view) {
        String username = name.getText().toString();
        String useremail = email.getText().toString();
        String userpassword = password.getText().toString();
        sharedPreferences= getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime=sharedPreferences.getBoolean("firsttime",true);
        if(isFirstTime){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();
            Intent intent=new Intent(RegistrationActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userpassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userpassword.length() < 6) {
            Toast.makeText(this, "Password too short,enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }





    public void signin(View view){
        startActivity(new Intent(RegistrationActivity.this, Login.class));
    }}
