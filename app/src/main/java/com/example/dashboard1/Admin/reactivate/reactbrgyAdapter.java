package com.example.dashboard1.Admin.reactivate;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay.act;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.dashboard1.Admin.reactivate.Admin_finalreactSelectBrgy.context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity;
import com.example.dashboard1.Admin.AdminPatientView.sbrgyAdapter;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.Holder;

import org.w3c.dom.Text;

public class reactbrgyAdapter extends FirebaseRecyclerAdapter<reactbrgyModel, reactbrgyAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public reactbrgyAdapter(@NonNull FirebaseRecyclerOptions<reactbrgyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull reactbrgyModel model) {
        holder.react_brgytxt.setText(model.getName());
        String brgy = model.getName();

        FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(brgy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.react_count.setText(String.valueOf(snapshot.getChildrenCount()));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(holder.react_count.getText().toString().equals("0")){

                }else{
                    holder.react_brgytxt.setVisibility(View.VISIBLE);
                    holder.react_count.setVisibility(View.VISIBLE);
                    holder.reactcl_brgyitem.setVisibility(View.VISIBLE);
                }
            }
        }, 500);

        holder.reactcl_brgyitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (holder.react_brgytxt.getContext(), Admin_reactivatepAccount.class);
                intent.putExtra("barangay", holder.react_brgytxt.getText().toString());
                holder.reactcl_brgyitem.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reactivate_brgyitem, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView react_brgytxt, react_count;
        ConstraintLayout reactcl_brgyitem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            react_brgytxt = (TextView) itemView.findViewById(R.id.react_brgytext);
            react_count = (TextView) itemView.findViewById(R.id.react_count);
            reactcl_brgyitem = (ConstraintLayout) itemView.findViewById(R.id.reactcl_brgyitem);

            react_brgytxt.setVisibility(View.GONE);
            react_count.setVisibility(View.GONE);
            reactcl_brgyitem.setVisibility(View.GONE);
        }
    }

}
