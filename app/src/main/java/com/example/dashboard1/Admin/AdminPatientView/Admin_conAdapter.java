package com.example.dashboard1.Admin.AdminPatientView;

import static com.example.dashboard1.Admin.AdminPatientView.EndedFragment.nodata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Admin_conAdapter extends FirebaseRecyclerAdapter<Admin_newDataModel, Admin_conAdapter.myViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Admin_conAdapter(@NonNull FirebaseRecyclerOptions<Admin_newDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Admin_newDataModel model) {
        String fullname = model.getFirstname() + " " + model.getLastname();
        holder.pnum.setText(model.getPatientnum());
        holder.fullname.setText(fullname);
        holder.age.setText(model.getAge());
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pview_item, parent,false);
        return new Admin_conAdapter.myViewholder(view);
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
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0){
            nodata.setVisibility(View.VISIBLE);
        }else{
            nodata.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
