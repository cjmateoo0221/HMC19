package com.example.dashboard1.Admin.adminviewplogs;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalSelectBarangy.status;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.brgyAdapter;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sbrgylogsAdapter extends FirebaseRecyclerAdapter<sbrgylogsModel, sbrgylogsAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public sbrgylogsAdapter(@NonNull FirebaseRecyclerOptions<sbrgylogsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull sbrgylogsModel model) {
        holder.brgytxt.setText(model.getName());
        String brgy = model.getName();
        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tv_count.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(holder.tv_count.getText().toString().equals("0")){

                }else{
                    holder.cl_brgyitem.setVisibility(View.VISIBLE);
                    holder.brgytxt.setVisibility(View.VISIBLE);
                    holder.tv_count.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);
        holder.cl_brgyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (holder.cl_brgyitem.getContext(), Admin_selectpforlogs.class);
                intent.putExtra("barangay", model.getName());
                holder.cl_brgyitem.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monitoring_barangayitem, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView brgytxt, tv_count;
        ConstraintLayout cl_brgyitem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            brgytxt = (TextView) itemView.findViewById(R.id.brgytxt);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            cl_brgyitem = (ConstraintLayout) itemView.findViewById(R.id.cl_brgyitem);

            cl_brgyitem.setVisibility(View.GONE);
            brgytxt.setVisibility(View.GONE);
            tv_count.setVisibility(View.GONE);
        }
    }
}
