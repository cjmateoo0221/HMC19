package com.example.dashboard1.Admin.adminviewplogs;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.example.dashboard1.Admin.adminviewplogs.Admin_selectpforlogs.barangay;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class selectpAdapter extends FirebaseRecyclerAdapter<selectpModel, selectpAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public selectpAdapter(@NonNull FirebaseRecyclerOptions<selectpModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull selectpModel model) {
        String fullname = model.getFirstname() + " " + model.getLastname();
        holder.pnotifandlogs_pname.setText(fullname);
        holder.pnotifandlogs_pnum.setText(model.getPatientnum());
        holder.cv_plogsandnotf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (holder.pnotifandlogs_pname.getContext(), Admin_viewlogspatient.class);
                intent.putExtra("patientnum", model.getPatientnum());
                intent.putExtra("barangay", barangay);
                intent.putExtra("patientname", holder.pnotifandlogs_pname.getText().toString());
                holder.pnotifandlogs_pname.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientlogsandnotif_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView pnotifandlogs_pnum, pnotifandlogs_pname;
        RelativeLayout cv_plogsandnotf;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            pnotifandlogs_pname = (TextView) itemView.findViewById(R.id.pnotifandlogs_pname);
            pnotifandlogs_pnum = (TextView) itemView.findViewById(R.id.pnotifandlogs_pnum);
            cv_plogsandnotf = (RelativeLayout) itemView.findViewById(R.id.cv_plogsandnotf);

        }
    }
}
