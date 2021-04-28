package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    String srole;
    ProgressBar progressBar;
    TextView reg,forgot;
    ImageButton view;
    Button login;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar2);
        password = findViewById(R.id.password);
        reg = findViewById(R.id.textView2);
        view = findViewById(R.id.imageButton2);
        login = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        String flag = "true";
        forgot = findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.forgot_password, (ViewGroup) findViewById(R.id.root));

                final EditText emailText = view.findViewById(R.id.emailreset);

                final ProgressBar progressBarreset = view.findViewById(R.id.progressBarReset);

                Button ResetSubmit = view.findViewById(R.id.resetbutton);

                ResetSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (emailText.getText().toString().isEmpty()) {
                            emailText.setError("Required");
                        } else {
                            progressBarreset.setVisibility(View.VISIBLE);
                            mAuth.sendPasswordResetEmail(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBarreset.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Please check your Mail and click on link to reset your password.",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });

                alert.setView(view);
                AlertDialog alertDialog = alert.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        flag = getIntent().getStringExtra("flag");
        getIntent().removeExtra("flag");

        if (flag == "true") {
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User").child(mAuth.getUid()).child("role");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String srole = dataSnapshot.getValue(String.class);
                            if (srole.length() == 9) {
                                startActivity(new Intent(getApplicationContext(), vHome.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), examDetail.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), 0);
                if (username.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
                    username.setError("Required");
                    password.setError("Required");
                }
                else {
                    String email = username.getText().toString();
                    String pass = password.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("User").child(mAuth.getUid()).child("role");
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        srole = dataSnapshot.getValue(String.class);
                                        if (mAuth.getCurrentUser().isEmailVerified()){
                                            Toast.makeText(getApplicationContext(),"Login Successfull.",Toast.LENGTH_SHORT).show();
                                            if (srole.length() == 9) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                startActivity(new Intent(getApplicationContext(), vHome.class));
                                            } else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                startActivity(new Intent(getApplicationContext(), examDetail.class));
                                            }
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"Please check your Mail and click on link to verify yourself.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.length());
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });

    }
}