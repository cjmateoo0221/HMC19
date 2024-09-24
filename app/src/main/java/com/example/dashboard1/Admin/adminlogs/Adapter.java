package com.example.dashboard1.Admin.adminlogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.dashboard1.Admin.adminlogs.Admin_Logs.context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewDataModel;
import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewMainAdapter;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter extends FirebaseRecyclerAdapter<Model, Adapter.myViewholder> {

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
    protected void onBindViewHolder(@NonNull Adapter.myViewholder holder, int position, @NonNull Model model) {
        holder.action.setText(model.getAction());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public Adapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_item, parent, false);
        return new Adapter.myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        TextView action, date, nodata;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            action = (TextView) itemView.findViewById(R.id.laction);
            date = (TextView) itemView.findViewById(R.id.ldate);
        }
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0 ){
            Toast.makeText(context, "No logs found on the selected date.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
