package com.origininfotech.exampleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder>  {

    private Context context;
    private List<dataHelper> dataHelperList;

    public customAdapter(Context context, List dataHelperList) {
        this.context = context;
        this.dataHelperList = dataHelperList;
    }
    @NonNull
    @Override
    public customAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.ViewHolder holder, int position) {
        dataHelper data = dataHelperList.get(position);
        holder.cnam.setText(data.getCname());
        holder.coursename.setText(data.getCoursename());
        holder.emailt.setText(data.getEmail());
        holder.examdate.setText(data.getExamdate());
        holder.examtime.setText(data.getExamtime());
        holder.examvenue.setText(data.getVenue());
        holder.cname = data.getCname();
        holder.course = data.getCoursename();
        holder.date = data.getExamdate();
        holder.time = data.getExamtime();
        holder.venue = data.getVenue();
    }

    @Override
    public int getItemCount() {
        return dataHelperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView coursename,cnam,examtime,examvenue,emailt,examdate;
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

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            coursename=view.findViewById(R.id.cname);
            cnam=view.findViewById(R.id.cName);
            examtime=view.findViewById(R.id.etime);
            examvenue=view.findViewById(R.id.evenue);
            emailt=view.findViewById(R.id.email);
            examdate=view.findViewById(R.id.edate);

            mAuth = FirebaseAuth.getInstance();
            bt = view.findViewById(R.id.button2);
            linearLayout = view.findViewById(R.id.Expandable);
            b = view.findViewById(R.id.imageButton);
            cardView = view.findViewById(R.id.card);
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

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long id = dataSnapshot.getChildrenCount();
                            id++;
                            final dealHelper data = new dealHelper(String.valueOf(id),cname,mAuth.getCurrentUser().getEmail(),cname+"false"+"false",name,course,nmbr,name+"false"+"false",date,time,venue,"Not Accepted","false",cname+"false"+course+date);
                            ref.child(String.valueOf(id)).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,"Request Submitted.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
