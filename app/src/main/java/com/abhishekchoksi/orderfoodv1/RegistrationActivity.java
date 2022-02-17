package com.abhishekchoksi.orderfoodv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegistrationActivity extends AppCompatActivity {
    private TextView textViewSignIn;
    private EditText email,password;
    private Button btnSignUp;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        textViewSignIn = findViewById(R.id.textViewSignIn);
        email = findViewById(R.id.EditTextEmail);
        password = findViewById(R.id.EditTextPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        loadingPB = findViewById(R.id.idPBLoading);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            finish();
        }

        sharedPreferences = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);
        if(isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            startActivity(new Intent(RegistrationActivity.this,OnBoardingActivity.class));
            finish();
        }

        // adding click listener for Sign Up button.
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checking if the password and confirm password is equal or not.
                loadingPB.setVisibility(View.VISIBLE);

                // getting data from our edit text.
                String UserEmail = email.getText().toString();
                String UserPassword = password.getText().toString();

                // checking if the text fields are empty or not.
                if(TextUtils.isEmpty(UserEmail) || TextUtils.isEmpty(UserPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please add your credentials..", Toast.LENGTH_SHORT).show();
                }else if(UserPassword.length() < 6){
                    Toast.makeText(RegistrationActivity.this, "Password Too Short, Please Enter Minimum 6 Characters!", Toast.LENGTH_SHORT).show();
                } else{
                    // on below line we are creating a new user by passing email and password.
                    mAuth.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // on below line we are checking if the task is success or not.
                            if(task.isSuccessful()){
                                // in on success method we are hiding our progress bar and opening a Sign In activity.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                                finish();
                            } else {
                                // in else condition we are displaying a failure toast message.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Fail to register user..\n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // adding on click for Sign In textView.
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}