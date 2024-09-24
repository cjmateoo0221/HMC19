package com.example.dashboard1.Admin.AdminPatientView;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalSelectBarangy.status;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay.act;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sbrgyAdapter extends FirebaseRecyclerAdapter<sbrgyModel, sbrgyAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public sbrgyAdapter(@NonNull FirebaseRecyclerOptions<sbrgyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull sbrgyModel model) {
        holder.brgytxt.setText(model.getName());
        String brgy = model.getName();

        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tv_activecount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(brgy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tv_concludedcount.setText(String.valueOf(snapshot.getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(holder.tv_concludedcount.getText().toString().equals("0") && holder.tv_activecount.getText().toString().equals("0")){

               }else{
                   holder.cl_brgyitem.setVisibility(View.VISIBLE);
                   holder.brgytxt.setVisibility(View.VISIBLE);
                   holder.tv_activecount.setVisibility(View.VISIBLE);
                   holder.tv_concludedcount.setVisibility(View.VISIBLE);
               }
            }
        }, 500);
        holder.cl_brgyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (holder.brgytxt.getContext(), Admin_PatientViewActivity.class);
                intent.putExtra("barangay", holder.brgytxt.getText().toString());
                intent.putExtra("from", act);
                holder.cl_brgyitem.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pview_brgyitem, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView brgytxt, tv_activecount, tv_concludedcount;
        ConstraintLayout cl_brgyitem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            brgytxt = (TextView) itemView.findViewById(R.id.pview_brgytxt);
            tv_activecount = (TextView) itemView.findViewById(R.id.tv_activecount);
            tv_concludedcount = (TextView) itemView.findViewById(R.id.tv_concludedcount);
            cl_brgyitem = (ConstraintLayout) itemView.findViewById(R.id.cl_pviewbrgyitem);

            cl_brgyitem.setVisibility(View.GONE);
            brgytxt.setVisibility(View.GONE);
            tv_activecount.setVisibility(View.GONE);
            tv_concludedcount.setVisibility(View.GONE);

        }
    }
}
