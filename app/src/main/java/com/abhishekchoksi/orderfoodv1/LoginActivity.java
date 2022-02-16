package com.abhishekchoksi.orderfoodv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextView textViewSignUp;
    private EditText email,password;
    private Button btnSignIn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        textViewSignUp = findViewById(R.id.textViewSignUp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.btnSignIn);
        loadingPB = findViewById(R.id.idPBLoadingSignIn);
        mAuth = FirebaseAuth.getInstance();

        // adding click listener for Sign In button.
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checking if the password and confirm password is equal or not.
                loadingPB.setVisibility(View.VISIBLE);

                // getting data from our edit text.
                String UserEmail = email.getText().toString();
                String UserPassword = password.getText().toString();

                // checking if the text fields are empty or not.
                if(TextUtils.isEmpty(UserEmail) || TextUtils.isEmpty(UserPassword)){
                    Toast.makeText(LoginActivity.this, "Please add your credentials..", Toast.LENGTH_SHORT).show();
                } else if(UserPassword.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password Too Short, Please Enter Minimum 6 Characters!", Toast.LENGTH_SHORT).show();
                } else{
                    // on below line we are creating a new user by passing email and password.
                    mAuth.signInWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // on below line we are checking if the task is success or not.
                            if(task.isSuccessful()){
                                // in on success method we are hiding our progress bar and opening a Sign In activity.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();

                                // on below line we are opening our mainactivity.
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            } else {
                                // in else condition we are displaying a failure toast message.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Please Enter Valid User Credentials..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // adding on click for Sign Up textView.
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });
    }

}