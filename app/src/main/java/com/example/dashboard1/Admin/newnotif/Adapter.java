package com.example.dashboard1.Admin.newnotif;


import android.view.LayoutInflater;
import static com.example.dashboard1.Admin.newnotif.AdminNotif.context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter extends FirebaseRecyclerAdapter<Model, Adapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
        holder.action.setText(model.getAction());
        holder.date.setText(model.getDate());
        holder.patientnum.setText(model.getPatientnum());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifadminitem, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView date, patientnum, action;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.na_date);
            patientnum = (TextView) itemView.findViewById(R.id.na_pnum);
            action = (TextView) itemView.findViewById(R.id.na_action);
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0){
           Toast.makeText(context, "No notification found on the selected date.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
