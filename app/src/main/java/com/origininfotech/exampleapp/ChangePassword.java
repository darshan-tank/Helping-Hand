package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    EditText answer;
    Spinner spinner;
    Button submit;
    String[] question = {"Select any one","What was your childhood nickname?","What is the name of your favorite childhood friend?","What was the name of your elementary / primary school?"
    };
    String userQuestion = "Select any one";

    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        try {

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        answer = findViewById(R.id.securityQuestion);
        spinner = findViewById(R.id.spinner2);
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

        submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userQuestion.equals("Select any one")) {

                    View selectedView = spinner.getSelectedView();
                    if (selectedView != null && selectedView instanceof TextView) {
                        TextView selectedTextView = (TextView) selectedView;
                        selectedTextView.setError("Required");
                    }
                }else if (answer.getText().toString().isEmpty()) {
                    answer.setError("Required");
                } else {
                    reference = database.getReference().child("User").child(mAuth.getCurrentUser().getUid()).child("securityquestionsort");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String match = userQuestion+answer.getText().toString();
                            if (dataSnapshot.getValue().toString().equals(match)) {
                                startActivity(new Intent(ChangePassword.this,createNewPass.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Match Not Found.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}