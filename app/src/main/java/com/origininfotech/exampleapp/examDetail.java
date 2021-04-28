package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.google.firebase.database.core.Path;

import java.util.ArrayList;

public class examDetail extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String year;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    DatabaseReference reference,r1;
    FloatingActionButton fb;
    public String number;
    Spinner spin;
    LinearLayout linearLayout;
    String[] examyear ;
    Context c;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        try {
        if (examyear[position] == "Sort by : Year") {
            r1 = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("name");
            r1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    number = dataSnapshot.getValue(String.class);
                    reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                    Query query = reference.orderByChild("sortC").equalTo(number+"false");
                    FirebaseRecyclerAdapter<dataHelper,dataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataHelper, dataViewHolder>
                            (dataHelper.class,R.layout.item_list,dataViewHolder.class,query) {
                        @Override
                        protected void populateViewHolder(dataViewHolder dataViewHolder, dataHelper dataHelper, int i) {
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
                    recyclerView.setAdapter(firebaseRecyclerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        } else {
            r1 = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("name");
            r1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    number = dataSnapshot.getValue(String.class);
                    reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                    Query query = reference.orderByChild("sortY").equalTo(number+"false"+examyear[position]);
                    FirebaseRecyclerAdapter<dataHelper,dataViewHolder> Adapter = new FirebaseRecyclerAdapter<dataHelper, dataViewHolder>
                            (dataHelper.class,R.layout.item_list,dataViewHolder.class,query) {
                        @Override
                        protected void populateViewHolder(dataViewHolder dataViewHolder, dataHelper dataHelper, int i) {
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

                    recyclerView.setAdapter(Adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        linearLayout = findViewById(R.id.linearLayout6);
        fb = findViewById(R.id.fab);
        c=examDetail.this;
        examyear = new String[]{"Sort by : Year", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,examyear);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(examDetail.this,addExam.class));
            }
        });

        spin.setAdapter(aa);

        year = intent.getStringExtra("YEAR");
        r1 = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid()).child("name");
        r1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue(String.class);
                reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                Query query = reference.orderByChild("sortC").equalTo(number+"false");
                FirebaseRecyclerAdapter<dataHelper,dataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataHelper, dataViewHolder>
                        (dataHelper.class,R.layout.item_list,dataViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(dataViewHolder dataViewHolder, dataHelper dataHelper, int i) {
                        try {
                        dataViewHolder.setCourseName(dataHelper.getCoursename());
                        dataViewHolder.setCName(dataHelper.getCname());
                        dataViewHolder.setEmail(mAuth.getCurrentUser().getEmail());
                        dataViewHolder.setExamDate(dataHelper.getExamdate());
                        dataViewHolder.setExamTime(dataHelper.getExamtime());
                        dataViewHolder.setDelete(dataHelper.getCoursename(),dataHelper.getExamyear(),c,number);
                        String raw =dataHelper.getVenue();
                        String format = raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
                        dataViewHolder.setVenue(format);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static class dataViewHolder extends RecyclerView.ViewHolder{
        View view;
        LinearLayout linearLayout;
        ImageButton b;
        CardView cardView;
        String course,year,number,candidateName,date;
        Context c;

        public dataViewHolder(View itemview){
            super(itemview);
            view=itemview;
            linearLayout = view.findViewById(R.id.Expandable);
            b = view.findViewById(R.id.imageButton);
            cardView = view.findViewById(R.id.card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(c, cardView);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());

                    try {

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                            Query query = databaseReference.orderByChild("delete").equalTo(candidateName+"false"+course+date);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        DatabaseReference ref = snapshot.getRef();
                                        ref.child("sort").setValue("");
                                        ref.child("sortC").setValue("");
                                        ref.child("sortD").setValue("");
                                        ref.child("sortY").setValue("");
                                        //Toast.makeText(c,snapshot.getRef().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Deal");
                            Query q = databaseRef.orderByChild("delete").equalTo(candidateName+"false"+course+date);
                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        DatabaseReference ref = snapshot.getRef();
                                        ref.child("sort").setValue("");
                                        ref.child("sortC").setValue("");
                                        ref.child("sortc").setValue("");
                                        //Toast.makeText(c,snapshot.getRef().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                                    //Toast.makeText(c,path.toString(),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    popup.show();
                }
            });

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
            candidateName = cName;
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
            date = examDate;
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_home, menu);
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

            Button button5 = (Button) view.findViewById(R.id.changePassword);
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ChangePassword.class));
                }
            });

            Button button3 = (Button) view.findViewById(R.id.updte);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), updateprofile.class));
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

            case R.id.filter:
            if (linearLayout.getVisibility() == View.GONE){
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
            }
    }
        return(super.onOptionsItemSelected(item));
    }
}