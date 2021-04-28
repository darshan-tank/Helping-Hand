package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class createNewPass extends AppCompatActivity {

    EditText pass,cpass;
    Button submit;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    ImageView ipass,icpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_pass);

        try {

        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        submit = findViewById(R.id.buttonsub);
        progressBar = findViewById(R.id.progressBar3);
        ipass = findViewById(R.id.ipass);
        icpass = findViewById(R.id.icpass);

        ipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass.setSelection(pass.length());
                } else {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass.setSelection(pass.length());
                }
            }
        });

        icpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cpass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    cpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    cpass.setSelection(cpass.length());
                } else {
                    cpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    cpass.setSelection(cpass.length());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass.getText().toString().isEmpty()) {
                    pass.setError("Required");
                } else if (cpass.getText().toString().isEmpty()) {
                    cpass.setError("Required");
                } else {
                    if (pass.getText().toString().equals(cpass.getText().toString())) {
                        if (pass.getText().toString().length() < 6) {
                            pass.setError("Password should be at least 6 characters");
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            mAuth.getCurrentUser().updatePassword(pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Password Updated.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(createNewPass.this, LoginActivity.class);
                                    intent.putExtra("flag", "false");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}