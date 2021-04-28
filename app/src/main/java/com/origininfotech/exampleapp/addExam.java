package com.origininfotech.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class addExam extends AppCompatActivity {

    EditText ename,cname,etime,evenue,eyear,eemail;
    String examname,examdate,examtime,examvenue,examyear,number,email,cName;
    Button submit;
    ProgressBar progressBar;
    int index=0;
    FirebaseDatabase database;
    DatabaseReference reference,reference1,reference2,reference3,ref;
    FirebaseAuth mAuth;

    private int  mHour, mMinute;

    private Calendar calendar,c;
    private int year, month, day;
    String date1,date2,time1,time2;
    Button dte1,dte2,tme1,tme2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        ename = findViewById(R.id.Name);
        evenue = findViewById(R.id.Venue);
        eyear = findViewById(R.id.Year);
        eemail = findViewById(R.id.email);
        submit = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        dte1 = findViewById(R.id.date1);
        dte2 = findViewById(R.id.date2);
        tme1 = findViewById(R.id.time1);
        tme2 = findViewById(R.id.time2);
        cname = findViewById(R.id.CName);

        calendar = Calendar.getInstance();
        c = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        eyear.setText(String.valueOf(year));

        mAuth = FirebaseAuth.getInstance();

        eemail.setText(mAuth.getCurrentUser().getEmail());

        ref = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getUid()).child("name");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cname.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("ExamPost");
        reference3 = database.getReference("ExamPosT");
        reference2 = database.getReference("ExamPostV");
        reference1 = database.getReference("User").child(mAuth.getUid()).child("number");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cname.getText().toString().isEmpty() && ename.getText().toString().isEmpty() && time1.equals("") && time2.equals("") && eyear.getText().toString().isEmpty() && eemail.getText().toString().isEmpty() && date1.equals("") && date2.equals("") && evenue.getText().toString().isEmpty()) {
                    ename.setError("Required");
                    cname.setError("Required");
                    eyear.setError("Required");
                    eemail.setError("Required");
                    etime.setError("Required");
                    dte1.setError("Required");
                    dte2.setError("Required");
                    tme1.setError("Required");
                    tme2.setError("Required");
                    evenue.setError("Required");
                }
                else{
                    examname = ename.getText().toString();
                    examyear = eyear.getText().toString();
                    email = eemail.getText().toString();
                    cName = cname.getText().toString();
                    examtime = time1 + time2;
                    examvenue = evenue.getText().toString();
                    examdate = date1 + " To " + date2;
                    progressBar.setVisibility(View.VISIBLE);

                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            number = dataSnapshot.getValue(String.class);
                            //final addExamHelper examHelper = new addExamHelper(examname,examdate,examtime,examvenue,examyear,email,cName,examvenue+"false","false");

                            reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    index = (int) (dataSnapshot.getChildrenCount() + 1);
                                    final addExamHelper examHelper = new addExamHelper(String.valueOf(index),cName+"false",examname,examdate,examtime,examvenue,examyear,email,cName,examvenue+"false",cName+"false"+examname+examdate,cName+"false"+examyear,examvenue+"false"+date1);
                                    reference3.child(String.valueOf(index)).setValue(examHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(),"Exam Added.",Toast.LENGTH_SHORT);
                                                  startActivity(new Intent(addExam.this,examDetail.class));
                                                } else {
                                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                                                 }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                              //  @Override
                                //public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                  //  index = (int) (dataSnapshot.getChildrenCount() + 1);
                                    //final addExamHelper examvHelper = new addExamHelper(String.valueOf(index),examyear+"-"+examname,examname,examdate,examtime,examvenue,examyear,email,cName,examvenue+"false","false");
                                   // reference2.child(String.valueOf(index)).setValue(examvHelper);
                                //}

                               //@Override
                               //public void onCancelled(@NonNull DatabaseError databaseError) {

                              // }
                            //});
                            //reference.child(number).child(examyear).child(examyear+"-"+examname).setValue(examHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                              //  @Override
                                //public void onComplete(@NonNull Task<Void> task) {
                                  //  if (task.isSuccessful()) {
                                    //    Toast.makeText(getApplicationContext(),"Exam Added.",Toast.LENGTH_SHORT);
                                      //  startActivity(new Intent(addExam.this,Home.class));
                                    //} else {
                                    //    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                                   // }
                               // }
                            //});
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });

                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this,
                    firstdate, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this,
                    seconddate, year, month, day);
        } else if (id == 3) {
            return new TimePickerDialog(this,
                    firsttime, mHour, mMinute, false);
        } else if (id == 4) {
            return new TimePickerDialog(this,
                    secondtime, mHour, mMinute, false);
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
                    String day = String.valueOf(arg3);
                    String month = String.valueOf(arg2);

                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    int mnth = Integer.parseInt(month);

                    date1 = day + "-" + ("0"+ (mnth + 1)) + "-" + arg1;
                    dte1.setText(day + "-" + ("0"+ (mnth + 1)) + "-" + arg1);
                }
            };


    public void choosestart(View view) {
        showDialog(1);
    }

    public void chooseend(View view) {
        showDialog(2);
    }

    private DatePickerDialog.OnDateSetListener seconddate = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    String day = String.valueOf(arg3);
                    String month = String.valueOf(arg2);

                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    int mnth = Integer.parseInt(month);

                    date2 = day + "-" + ("0"+ (mnth + 1)) + "-" + arg1;
                    dte2.setText(day + "-" + ("0"+ (mnth + 1)) + "-" + arg1);
                }
            };

    public void choosestarttime(View view) {
        showDialog(3);
    }

    public void chooseendtime(View view) {
        showDialog(4);
    }

    private TimePickerDialog.OnTimeSetListener firsttime = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 12) {
                        tme1.setText(hourOfDay + ":" + minute + " AM");
                        time1 = hourOfDay + ":" + minute + " AM To ";
                    } else {
                        tme1.setText(hourOfDay + ":" + minute + " PM");
                        time1 = hourOfDay + ":" + minute + " PM To ";
                    }
                }
            };
    private TimePickerDialog.OnTimeSetListener secondtime = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 12) {
                        tme2.setText(hourOfDay + ":" + minute + " AM");
                        time2 = hourOfDay + ":" + minute + " AM";
                    } else {
                        tme2.setText(hourOfDay + ":" + minute + " PM");
                        time2 = hourOfDay + ":" + minute + " PM";
                    }
                }
            };

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
                }
            });
            Button button2 = (Button) view.findViewById(R.id.dl);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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