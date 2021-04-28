package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Home extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    FloatingActionButton fb;
    RecyclerView listView;
    DatabaseReference reference,r1,r2,r3;
    String[] examyear ;
    public String number;
    Context c;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        examyear = new String[]{ "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};
        c = Home.this;
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        fb = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);

        try {

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,examyear);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);

        r1 = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("name");
        r1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue(String.class);
                reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                Query query = reference.orderByChild("sortC").equalTo(number+"false");
                FirebaseRecyclerAdapter<dataHelper, Home.dataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataHelper, Home.dataViewHolder>
                        (dataHelper.class,R.layout.item_list, Home.dataViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(Home.dataViewHolder dataViewHolder, dataHelper dataHelper, int i) {
                        dataViewHolder.setCourseName(dataHelper.getCoursename());
                        dataViewHolder.setCName(dataHelper.getCname());
                        dataViewHolder.setEmail(mAuth.getCurrentUser().getEmail());
                        dataViewHolder.setExamDate(dataHelper.getExamdate());
                        dataViewHolder.setExamTime(dataHelper.getExamtime());
                        dataViewHolder.setDelete(dataHelper.getCoursename(),dataHelper.getExamyear(),c,number);
                        String raw =dataHelper.getVenue();
                        String format = raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
                        dataViewHolder.setVenue(format);
                    }
                };

                listView.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    } catch (Exception e) {
        e.printStackTrace();
    }

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,addExam.class));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.menu:
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.activity_menu, (ViewGroup) findViewById(R.id.root));

            Button button1 = (Button) view.findViewById(R.id.desh);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                }
            });

            Button button2 = (Button) view.findViewById(R.id.dl);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User").child(mAuth.getUid()).child("role");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String srole = dataSnapshot.getValue(String.class);
                            if (srole.length() == 9) {
                                startActivity(new Intent(getApplicationContext(), deal.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), CDeal.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            });
            Button button3 = (Button) view.findViewById(R.id.updte);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), updateprofile.class));
                }
            });
            Button button5 = (Button) view.findViewById(R.id.changePassword);
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ChangePassword.class));
                }
            });
            Button button4 = (Button) view.findViewById(R.id.lgout);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });// etc.. for button2,3,4.
            alert.setView(view);
            AlertDialog alertDialog = alert.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);
            alertDialog.show();
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),examyear[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class dataViewHolder extends RecyclerView.ViewHolder{
        View view;
        LinearLayout linearLayout;
        ImageButton b;
        CardView cardView;
        String course,year,number;
        Context c;

        public dataViewHolder(View itemview){
            super(itemview);
            view=itemview;
            linearLayout = view.findViewById(R.id.Expandable);
            b = view.findViewById(R.id.imageButton);
            cardView = view.findViewById(R.id.card);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        linearLayout.setVisibility(View.VISIBLE);
                        b.setImageResource(R.drawable.ic_down);
                    } else {
                        linearLayout.setVisibility(View.GONE);
                        b.setImageResource(R.drawable.ic_gt);
                    }
                }
            });
        }
        public void setCourseName(String courseName){
            TextView coursename=view.findViewById(R.id.cname);
            coursename.setText(courseName);
        }
        public void setCName(String cName){
            TextView cname=view.findViewById(R.id.cName);
            cname.setText(cName);
        }
        void setDelete(String course,String year,Context c,String number){
            this.course = course;
            this.year = year;
            this.c = c;
            this.number = number;
        }
        public void setExamDate(String examDate){
            TextView examdate=view.findViewById(R.id.edate);
            examdate.setText(examDate);
        }
        public void setExamTime(String examTime){
            TextView examtime=view.findViewById(R.id.etime);
            examtime.setText(examTime);
        }
        public void setVenue(String venue){
            TextView examvenue=view.findViewById(R.id.evenue);
            examvenue.setText(venue);
        }
        public void setEmail(String email){
            TextView emailt=view.findViewById(R.id.email);
            emailt.setText(email);
        }
    }

}