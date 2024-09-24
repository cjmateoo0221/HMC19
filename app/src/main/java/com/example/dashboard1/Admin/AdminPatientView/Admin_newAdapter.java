package com.example.dashboard1.Admin.AdminPatientView;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay.act;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Admin_newAdapter extends FirebaseRecyclerAdapter<Admin_newDataModel, Admin_newAdapter.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Admin_newAdapter(@NonNull FirebaseRecyclerOptions<Admin_newDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Admin_newDataModel model) {
        String fullname = model.getFirstname() + " " + model.getLastname();
        holder.pnum.setText(model.getPatientnum());
        holder.fullname.setText(fullname);
        holder.age.setText(model.getAge());

        holder.rl_pviewpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.pnum.getContext(), Admin_nvPatient.class);
                intent.putExtra("barangay", barangay);
                intent.putExtra("from",act);
                intent.putExtra("patientnum", holder.pnum.getText().toString());
                holder.rl_pviewpatient.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pview_item, parent,false);
        return new Admin_newAdapter.myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        RelativeLayout rl_pviewpatient;
        TextView pnum, fullname, age;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            pnum = (TextView) itemView.findViewById(R.id.pview_pnum);
            fullname = (TextView) itemView.findViewById(R.id.pview_pname);
            pnum = (TextView) itemView.findViewById(R.id.pview_pnum);
            age = (TextView) itemView.findViewById(R.id.pview_page);
            rl_pviewpatient = (RelativeLayout) itemView.findViewById(R.id.rl_pviewpatient);
        }
    }
}
