package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CDeal extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    DatabaseReference reference,ref1,ref2;
    String name;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_deal);

        try {

        mAuth = FirebaseAuth.getInstance();
        c = CDeal.this;

        recyclerView = findViewById(R.id.deallist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ref1 = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid()).child("name");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Deal");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Query query = reference.orderByChild("sortc").equalTo(name+"false"+"false");

                FirebaseRecyclerAdapter<dealHelper, CDeal.dataViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<dealHelper, CDeal.dataViewHolder>(dealHelper.class,R.layout.deal_list, CDeal.dataViewHolder.class,query) {
                    @Override
                    protected void populateViewHolder(CDeal.dataViewHolder dataViewHolder, dealHelper dealHelper, int i) {

                        dataViewHolder.setCName(dealHelper.getCname());
                        dataViewHolder.setName(dealHelper.getVname());
                        dataViewHolder.setCourseName(dealHelper.getCourse());
                        dataViewHolder.setExamDate(dealHelper.getDate());
                        dataViewHolder.setExamTime(dealHelper.getTime());
                        dataViewHolder.setVenue(dealHelper.getVenue());
                        dataViewHolder.setId(Integer.parseInt(dealHelper.getId()),c);
                        dataViewHolder.setStatus(dealHelper.getStatus());
                        dataViewHolder.setNumber(dealHelper.getNumber());
                        dataViewHolder.setVemail(dealHelper.getVemail());
                    }
                };
                recyclerView.setAdapter(recyclerAdapter);
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
        String name;
        private FirebaseAuth mAuth;
        String cemail,vemail,subject,cmessage,vmessage,course,number,CName,cnumber;
        int id;
        Button cancel;
        CardView cardView;
        Context c;

        public dataViewHolder(View itemview){
            super(itemview);
            view=itemview;
            mAuth = FirebaseAuth.getInstance();
            linearLayout = view.findViewById(R.id.Expandable);
            b = view.findViewById(R.id.imageButton);
            cardView = view.findViewById(R.id.card);
            cancel = view.findViewById(R.id.cancel);
            cancel.setText("Accept");
            try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Deal").child(String.valueOf(id)).child("cancel");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == "true") {
                        cancel.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid()).child("number");
            databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cnumber = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

            cemail = mAuth.getCurrentUser().getEmail();
            subject = "Regarding sharing information";
            cmessage = "Hello, \n"+"I am "+name+". I am here to help you as a writer in your exam (Course : "+course+"). My contact detail is +91 "+number+".";
            vmessage = "Hello, \n"+"I am "+CName+". Thank you for your support as a writer in my exam (Course : "+course+"). My contact detail is +91 "+cnumber+".";

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    cemail = mAuth.getCurrentUser().getEmail();
                    subject = "Regarding sharing information";
                    cmessage = "Hello, \n"+"I am "+name+". I am here to help you as a writer in your exam (Course : "+course+"). My contact detail is +91 "+number+".";
                    vmessage = "Hello, \n"+"I am "+CName+". Thank you for your support as a writer in my exam (Course : "+course+"). My contact detail is +91 "+cnumber+".";

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Deal").child(String.valueOf(id)).child("status");
                    databaseReference.setValue("Accepted");
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Deal").child(String.valueOf(id)).child("cancel");
                        databaseReference3.setValue("true");
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Deal").child(String.valueOf(id)).child("sort");
                        databaseReference1.setValue(name+"false"+"true");
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Deal").child(String.valueOf(id)).child("sortc");
                        databaseReference2.setValue(CName+"false"+"true");
                    JavaMailAPI candidateMail = new JavaMailAPI(c,cemail,subject,cmessage);
                    candidateMail.execute();
                    JavaMailAPI volunteerMail = new JavaMailAPI(c,vemail,subject,vmessage);
                    volunteerMail.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        public void setId(int id,Context c) {
            this.id = id;
            this.c = c;
        }

        public void setCourseName(String courseName){
            TextView coursename=view.findViewById(R.id.course);
            coursename.setText(courseName);
            course=courseName;
        }
        void setNumber(String number) {
            this.number=number;
        }
        public void setCName(String cName){
            TextView cname=view.findViewById(R.id.cname);
            cname.setText(cName);
            CName = cName;
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
        public void setStatus(String status){
            TextView Status=view.findViewById(R.id.status);
            Status.setText(status);
        }

        public void setName(String vname) {
            name = vname;
        }

        public void setVemail(String vemail) {
            this.vemail = vemail;
        }
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

}