package com.example.dashboard1.Patient.patientdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class pdadapter extends FirebaseRecyclerAdapter<pdmodel, pdadapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public pdadapter(@NonNull FirebaseRecyclerOptions<pdmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull pdmodel model) {
        holder.address.setText(model.getAddress());
        holder.occupation.setText(model.getOccupation());
        holder.age.setText(model.getAge());
        holder.email.setText(model.getEmail());
        holder.firstname.setText(model.getFirstname());
        holder.lastname.setText(model.getLastname());
        holder.middlename.setText(model.getMiddlename());
        holder.sex.setText(model.getSex());
        holder.contactnum.setText("09"+model.getContactnum());
        holder.birtdate.setText(model.getBirthday());
        holder.locofwork.setText(model.getLocofwork());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientpersonaldetails, parent,false);
        return new pdadapter.myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView firstname, lastname, middlename, address, age, email, contactnum, locofwork, name, occupation, sex, birtdate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.pdaddress);
            age = (TextView) itemView.findViewById(R.id.pdage);
            email = (TextView) itemView.findViewById(R.id.pdemail);
            contactnum = (TextView) itemView.findViewById(R.id.pdcontact);
            locofwork = (TextView) itemView.findViewById(R.id.pdloc);
            firstname = (TextView) itemView.findViewById(R.id.pdfirstname);
            lastname = (TextView) itemView.findViewById(R.id.pdlastname);
            middlename = (TextView) itemView.findViewById(R.id.pdmiddlename);
            occupation = (TextView) itemView.findViewById(R.id.pdoccu);
            sex = (TextView) itemView.findViewById(R.id.pdsex);
            birtdate = (TextView) itemView.findViewById(R.id.pdbday);
        }
    }
}
