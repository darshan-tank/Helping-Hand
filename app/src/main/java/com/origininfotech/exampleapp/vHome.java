package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class vHome extends AppCompatActivity {

    RecyclerView recyclerView;
    Button date1,date2,apply;
    DatabaseReference reference,reference1,ref1;
    private FirebaseAuth mAuth;
    RecyclerView.Adapter mAdapter;
    LinearLayout linearLayout;
    TextView nopost;
    Context c;

    private Calendar calendar,cal;
    private int year, month, day;
    String dt1,dt2,examdate;
    int sday,smonth,syear,eday,emonth,eyear;
    ArrayList <dataHelper> newData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_home);

        try {

        c = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();

        nopost = findViewById(R.id.nopost);
        linearLayout = findViewById(R.id.linearLayout6);
        date1 = findViewById(R.id.d1);
        date2 = findViewById(R.id.d2);
        apply = findViewById(R.id.apply);

        calendar = Calendar.getInstance();
        cal = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newData.clear();
                reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                reference1 = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid()).child("city");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String city = dataSnapshot.getValue(String.class);
                        Query query = reference.orderByChild("sort").equalTo(city+"false");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int dday,dmonth,dyear;
                                //dataHelper dh = dataSnapshot.getValue(dataHelper.class);

                                //"Day : "+dday+", Month : "+dmonth+", Year : "+dyear
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    dataHelper dh = snapshot.getValue(dataHelper.class);
                                    dday = Integer.parseInt(dh.getExamdate().substring(0,2));
                                    dmonth = Integer.parseInt(snapshot.child("examdate").getValue().toString().substring(3,5));
                                    dyear= Integer.parseInt(snapshot.child("examdate").getValue().toString().substring(6,10));
                                    //Toast.makeText(getApplicationContext(),"Day : "+sday+", Month : "+smonth+", Year : "+syear,Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(),"Day : "+eday+", Month : "+emonth+", Year : "+eyear,Toast.LENGTH_SHORT).show();
                                    if (sday<= dday && dday <= eday && smonth<= dmonth && dmonth <= emonth && syear<= dyear && dyear <= eyear) {
                                       newData.add(dh);
                                        //Toast.makeText(getApplicationContext(),"Day : "+dday+", Month : "+dmonth+", Year : "+dyear,Toast.LENGTH_SHORT).show();
                                    }

                                    mAdapter = new customAdapter(vHome.this, newData);
                                    recyclerView.setAdapter(mAdapter);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        recyclerView = findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

                reference = FirebaseDatabase.getInstance().getReference().child("ExamPosT");
                reference1 = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid()).child("city");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String city = dataSnapshot.getValue(String.class);
                        Query query = reference.orderByChild("sort").equalTo(city+"false");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() < 0) {
                                    nopost.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        FirebaseRecyclerAdapter<dataHelper, dataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataHelper, dataViewHolder>
                                (dataHelper.class,R.layout.item_list2, dataViewHolder.class,query) {
                            @Override
                            protected void populateViewHolder(dataViewHolder dataViewHolder, dataHelper dataHelper, int i) {

                                final String[] name = new String[1];

                                dataViewHolder.setEmail(dataHelper.getEmail());
                                dataViewHolder.setCourseName(dataHelper.getCoursename());
                                dataViewHolder.setExamDate(dataHelper.getExamdate());
                                dataViewHolder.setExamTime(dataHelper.getExamtime());
                                dataViewHolder.setDeal(dataHelper.getCname(),dataHelper.getCname()+"false"+"false",dataHelper.getCoursename(),dataHelper.getExamdate(),dataHelper.getExamtime(),dataHelper.getVenue(),c);
                                dataViewHolder.setCName(dataHelper.getCname());
                                String raw =dataHelper.getVenue();
                                String format = raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
                                dataViewHolder.setVenue(format);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this,
                    firstdate, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this,
                    seconddate, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener firstdate = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    sday = arg3;
                    smonth = arg2 + 1;
                    syear = arg1;
                    dt1 = arg3 + "-" + (arg2 + 1) + "-" + arg1;
                    date1.setText(arg3 + "-" + (arg2 + 1) + "-" + arg1);
                }
            };

    private DatePickerDialog.OnDateSetListener seconddate = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    eday = arg3;
                    emonth = arg2 + 1;
                    eyear = arg1;
                    dt2 = arg3 + "-" + (arg2 + 1) + "-" + arg1;
                    date2.setText(arg3 + "-" + (arg2 + 1) + "-" + arg1);
                }
            };

    public static class dataViewHolder extends RecyclerView.ViewHolder{
        View view;
        Button bt;
        DatabaseReference ref,ref1;
        FirebaseAuth mAuth;
        LinearLayout linearLayout;
        String cname,course,date,time,venue,nmbr,sortc;
        ImageButton b;
        String name;
        CardView cardView;
        Context c;

        public dataViewHolder(View itemview){
            super(itemview);
            view=itemview;
            mAuth = FirebaseAuth.getInstance();
            bt = view.findViewById(R.id.button2);
            linearLayout = view.findViewById(R.id.Expandable);
            b = view.findViewById(R.id.imageButton);
            cardView = view.findViewById(R.id.card);
            try {
            ref = FirebaseDatabase.getInstance().getReference().child("Deal");
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
            ref1 = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid()).child("number");
            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nmbr = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long id = dataSnapshot.getChildrenCount();
                            id++;
                            final dealHelper data = new dealHelper(String.valueOf(id),cname,mAuth.getCurrentUser().getEmail(),cname+"false"+"false",name,course,nmbr,name+"false"+"false",date,time,venue,"Not Accepted","false",cname+"false"+course+date);
                            ref.child(String.valueOf(id)).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(c,"Request Submitted.",Toast.LENGTH_SHORT).show();
                                }
                            });
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
        }
        public void setCourseName(String courseName){
            TextView coursename=view.findViewById(R.id.cname);
            coursename.setText(courseName);
        }
        public void setCName(String cName){
            TextView cname=view.findViewById(R.id.cName);
            cname.setText(cName);
        }
        public void  setDeal(String cname,String sortc,String course,String date,String time,String venue,Context c){
            this.cname = cname;
            this.sortc=sortc;
            this.course = course;
            this.date = date;
            this.time = time;
            this.venue = venue;
            this.c=c;
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