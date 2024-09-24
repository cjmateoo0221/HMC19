package com.example.dashboard1.Admin.AdminPatientMonitoring;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalSelectBarangy.status;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class brgyAdapter extends FirebaseRecyclerAdapter<brgyModel, brgyAdapter.myViewHolder> {
     String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
     Long totalbrgycount;
    ViewGroup.LayoutParams params;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public brgyAdapter(@NonNull FirebaseRecyclerOptions<brgyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull brgyModel model) {

        holder.brgytxt.setText(model.getName());
        String brgy = model.getName();
        if(status.equals("Not Answered")){
            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+"Severe")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.brgy_severecount.setText(Long.toString(snapshot.getChildrenCount()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
          FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+"Mild")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.brgy_mildcount.setText(Long.toString(snapshot.getChildrenCount()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+"Moderate")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.brgy_moderatecount.setText(Long.toString(snapshot.getChildrenCount()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+"Mild")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.brgy_mildcount.setText(Long.toString(snapshot.getChildrenCount()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+"No Symptom")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.brgy_nosympcount.setText(Long.toString(snapshot.getChildrenCount()));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            totalbrgycount = snapshot.getChildrenCount();
                            Long nosympcount = Long.parseLong(holder.brgy_nosympcount.getText().toString());
                            Long mildcount = Long.parseLong(holder.brgy_mildcount.getText().toString());
                            Long severecount = Long.parseLong(holder.brgy_severecount.getText().toString());
                            Long moderatecount = Long.parseLong(holder.brgy_moderatecount.getText().toString());

                            Long notanscount = totalbrgycount - (nosympcount + mildcount + severecount + moderatecount);
                            holder.tv_count.setText(String.valueOf(notanscount));

                            if(holder.tv_count.getText().toString().equals("0")){

                            }else{
                                holder.cl_brgyitem.setVisibility(View.VISIBLE);
                                holder.brgytxt.setVisibility(View.VISIBLE);
                                holder.tv_count.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }, 500);
            holder.cl_brgyitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (holder.brgy_mildcount.getContext(), Admin_PatientMonitoringActivity.class);
                    intent.putExtra("barangay", model.getName());
                    intent.putExtra("status", status);
                    holder.cl_brgyitem.getContext().startActivity(intent);
                }
            });
        }else{
                    FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+status).child(brgy)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    holder.tv_count.setText(String.valueOf(snapshot.getChildrenCount()));
                                    if(holder.tv_count.getText().toString().equals("0")){

                                    }else{
                                        holder.cl_brgyitem.setVisibility(View.VISIBLE);
                                        holder.brgytxt.setVisibility(View.VISIBLE);
                                        holder.tv_count.setVisibility(View.VISIBLE);
                                    }
                                    /*if(holder.tv_count.getText().toString().equals("0")){
                                        holder.itemView.setVisibility(View.GONE);
                                        params.height = 0;
                                        params.width = 0;
                                        holder.itemView.setLayoutParams(params);
                                    }else{
                                        holder.itemView.setVisibility(View.VISIBLE);
                                    }*/
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
            holder.cl_brgyitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (holder.brgy_mildcount.getContext(), Admin_PatientMonitoringActivity.class);
                    intent.putExtra("barangay", model.getName());
                    intent.putExtra("status", status);
                    holder.cl_brgyitem.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monitoring_barangayitem, parent, false);
        return new myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView brgytxt, tv_count, brgy_severecount, brgy_mildcount, brgy_moderatecount, brgy_nosympcount;
        ConstraintLayout cl_brgyitem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            brgytxt = (TextView) itemView.findViewById(R.id.brgytxt);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            cl_brgyitem = (ConstraintLayout) itemView.findViewById(R.id.cl_brgyitem);
            brgy_severecount = (TextView) itemView.findViewById(R.id.brgy_severecount);
            brgy_mildcount = (TextView) itemView.findViewById(R.id.brgy_mildcount);
            brgy_moderatecount = (TextView) itemView.findViewById(R.id.brgy_moderatecount);
            brgy_nosympcount = (TextView) itemView.findViewById(R.id.brgy_nosympcount);

            cl_brgyitem.setVisibility(View.GONE);
            brgytxt.setVisibility(View.GONE);
            tv_count.setVisibility(View.GONE);
            brgy_severecount.setVisibility(View.GONE);
            brgy_mildcount.setVisibility(View.GONE);
            brgy_moderatecount.setVisibility(View.GONE);
            brgy_nosympcount.setVisibility(View.GONE);
        }
    }

}
