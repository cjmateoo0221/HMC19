package com.example.dashboard1.Patient.notif;

import static com.example.dashboard1.Patient.notif.PatientNotifView.context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class pnadapter extends FirebaseRecyclerAdapter<pnmodel, pnadapter.myViewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public pnadapter(@NonNull FirebaseRecyclerOptions<pnmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull pnmodel model) {
        holder.date.setText(model.getDate());
        holder.message.setText(model.getMessage());
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pnotifitem,parent,false);
        return new myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        TextView date, message;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.pndate);
            message = (TextView) itemView.findViewById(R.id.pnotif);
        }
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0 ){
            Toast.makeText(context, "No Notifications found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
