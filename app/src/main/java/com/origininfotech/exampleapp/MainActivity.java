package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity {

    EditText name,email,number,pass,cpass,college,city,questionEdit;
    TextView login;
    ProgressBar progressBar;
    addUser userHelper;

    Button submit;
    ImageView ipass,icpass;
    String username,useremail,unumber,upass,ucpass,ucollege,ucity,urole,securityAnswer;
    Switch volunteer;
    Spinner spinner;
    String[] question = {"Select any one","What was your childhood nickname?","What is the name of your favorite childhood friend?","What was the name of your elementary / primary school?"
    };
    String userQuestion = "Select any one";

    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText) findViewById(R.id.Name);
        email=(EditText) findViewById(R.id.Email);
        number=(EditText) findViewById(R.id.Phn);
        pass=(EditText) findViewById(R.id.Password);
        cpass=(EditText) findViewById(R.id.CPassword);
        city=(EditText) findViewById(R.id.City);
        college=(EditText) findViewById(R.id.College);
        submit=(Button) findViewById(R.id.button);
        login = findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        volunteer = findViewById(R.id.switch1);
        spinner = findViewById(R.id.spinner2);
        questionEdit = findViewById(R.id.securityQuestion);

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

        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                R.layout.spinner_item,
                question);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userQuestion = question[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setAdapter(ad);

        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");


        if (mAuth.getCurrentUser() != null) {
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
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                if (userQuestion.equals("Select any one")) {

                    View selectedView = spinner.getSelectedView();
                    if (selectedView != null && selectedView instanceof TextView) {
                        TextView selectedTextView = (TextView) selectedView;
                        selectedTextView.setError("Required");
                    }
                }else if (number.getText().toString().length() < 10) {
                    number.setError("Invalid number format.");
                }
                else if (name.getText().toString().isEmpty()) {
                    name.setError("Required");
                }
                else if (email.getText().toString().isEmpty()) {
                    email.setError("Required");
                }
                else if (number.getText().toString().isEmpty()) {
                    number.setError("Required");
                }
                else if (pass.getText().toString().isEmpty()) {
                    pass.setError("Required");
                }
                else if (cpass.getText().toString().isEmpty()) {
                    cpass.setError("Required");
                }
                else if (college.getText().toString().isEmpty()) {
                    college.setError("Required");
                }
                else if (city.getText().toString().isEmpty()) {
                    city.setError("Required");
                }
                else if (questionEdit.getText().toString().isEmpty()) {
                    questionEdit.setError("Required");
                } else {

                if (pass.getText().toString().equals(cpass.getText().toString())) {

                    username = name.getText().toString();
                    useremail = email.getText().toString();
                    unumber = number.getText().toString();
                    upass = pass.getText().toString();
                    ucpass = cpass.getText().toString();
                    ucollege = college.getText().toString();
                    ucity = city.getText().toString();
                    if (volunteer.isChecked()){
                        urole = volunteer.getTextOn().toString();
                    } else {
                        urole = volunteer.getTextOff().toString();
                    }
                    securityAnswer = questionEdit.getText().toString();

                    userHelper = new addUser(username, useremail, unumber, ucollege, ucity.toLowerCase(),urole,userQuestion+securityAnswer);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                reference.child(mAuth.getCurrentUser().getUid()).setValue(userHelper);
                                sendLink();
                                Toast.makeText(getApplicationContext(), "Registration Successfull.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d("Error : ",task.getException().getMessage());
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Password and Confirm Password does not match.", Toast.LENGTH_SHORT).show();
                }
            }
            }
        });
    }

    private void sendLink() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Please check your Mail and click on link to verify yourself.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}