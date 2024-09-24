package com.example.dashboard1.Admin.adminviewplogs;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.example.dashboard1.Admin.adminviewplogs.Admin_viewlogspatient.context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.adminlogs.Adapter;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class viewlogsAdapter extends FirebaseRecyclerAdapter<viewlogsModel, viewlogsAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public viewlogsAdapter(@NonNull FirebaseRecyclerOptions<viewlogsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull viewlogsModel model) {
        holder.action.setText(model.getAction());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView action, date;
        public myViewHolder(@NonNull View itemView) {
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
