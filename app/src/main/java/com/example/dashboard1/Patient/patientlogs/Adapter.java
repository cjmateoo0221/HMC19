package com.example.dashboard1.Patient.patientlogs;

import static com.example.dashboard1.Patient.patientlogs.PatientLogs.context;

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
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plogs_item, parent, false);
        return new Adapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView action, date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            action = (TextView) itemView.findViewById(R.id.paction);
            date = (TextView) itemView.findViewById(R.id.pdate);
        }
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0 ){
            Toast.makeText(context, "No Logs found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
