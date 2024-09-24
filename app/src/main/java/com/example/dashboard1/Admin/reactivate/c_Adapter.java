package com.example.dashboard1.Admin.reactivate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.dashboard1.Admin.reactivate.Admin_reactivatepAccount.context;

import com.example.dashboard1.Admin.Admin_AddPatientActivity;
import com.example.dashboard1.Admin.adminlogs.Adapter;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class c_Adapter extends FirebaseRecyclerAdapter<c_Model, c_Adapter.myViewholder> {
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    Calendar calendar = Calendar.getInstance();
    int month = calendar.get(Calendar.MONTH);;
    int today = calendar.get(Calendar.DAY_OF_MONTH);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference patientdismissedbrgy = database.getReference("patientdismissedbrgy");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
    String startday, noofinfect, birthday, address, age, closecontact, contactnum, currentStatus, dateadded, dateended, disability, email, healthcond, household, locofwork, middlename, occupation, pass, patienttoken, pwd, refnum, senior, sex, status, swab, user, vaccinestat, patientnum, firstname, lastname, barangay;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public c_Adapter(@NonNull FirebaseRecyclerOptions<c_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, final int position, @NonNull c_Model cModel) {
        String fullname = cModel.firstname + " " + cModel.lastname;
        holder.c_pnum.setText(cModel.getPatientnum());
        barangay = cModel.getBarangay();
        holder.c_pname.setText(fullname);
        birthday = cModel.getBirthday();
        startday = cModel.getStartday();
        noofinfect = cModel.getNoofinfect();
        status = cModel.getStatus();
        address = cModel.getAddress();
        age = cModel.getAge();
        closecontact = cModel.getClosecontact();
        contactnum = cModel.getContactnum();
        currentStatus = cModel.getCurrentStatus();
        dateadded = cModel.getDateadded();
        dateended = cModel.getDateended();
        disability = cModel.getDisability();
        email = cModel.getEmail();
        firstname = cModel.getFirstname();
        healthcond = cModel.getHealthcond();
        household = cModel.getHousehold();
        lastname = cModel.getLastname();
        locofwork = cModel.getLocofwork();
        middlename = cModel.getMiddlename();
        occupation = cModel.getOccupation();
        pass = cModel.getPass();
        patientnum = cModel.getPatientnum();
        patienttoken = cModel.getPatienttoken();
        pwd = cModel.getPwd();
        refnum = cModel.getRefnum();
        senior = cModel.getSenior();
        sex = cModel.getSex();
        swab  = cModel.getSwab();
        user = cModel.getUser();
        vaccinestat = cModel.getVaccinestat();
        holder.btnreactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.c_pname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.reactivatedialog))
                        .setExpanded(true, 1700)
                        .create();

                View v = dialogPlus.getHolderView();
                TextView rpnum = v.findViewById(R.id.r_pnum);
                TextView rpname = v.findViewById(R.id.r_pname);
                Button btnreactivate = v.findViewById(R.id.btnr_confirm);
                rpname.setText(holder.c_pname.getText().toString());
                rpnum.setText(holder.c_pnum.getText().toString());
                dialogPlus.show();
                btnreactivate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int initinfect = Integer.parseInt(noofinfect) + 1;
                        Map<String, Object> map = new HashMap<>();
                        map.put("address", address);
                        map.put("startday", String.valueOf(today));
                        map.put("age", age);
                        map.put("birthday", birthday);
                        map.put("household", household);
                        map.put("barangay", barangay);
                        map.put("closecontact", closecontact);
                        map.put("noofinfect", String.valueOf(initinfect));
                        map.put("contactnum", contactnum);
                        map.put("currentStatus", "");
                        map.put("dateadded", date);
                        map.put("dateended", "N/A");
                        map.put("disability", disability);
                        map.put("email", email);
                        map.put("firstname", firstname);
                        map.put("healthcond", healthcond);
                        map.put("lastname", lastname);
                        map.put("locofwork", locofwork);
                        map.put("middlename", middlename);
                        map.put("occupation", occupation);
                        map.put("pass", pass);
                        map.put("patientnum", rpnum.getText().toString());
                        map.put("patienttoken", "");
                        map.put("pwd", pwd);
                        map.put("refnum", refnum);
                        map.put("senior", senior);
                        map.put("sex", sex);
                        map.put("status", "Active");
                        map.put("swab", swab);
                        map.put("user", user);
                        map.put("vaccinestat", vaccinestat);
                        Toast.makeText(context, "Patient account successfully reactivated", Toast.LENGTH_SHORT).show();
                        patientdismissed.child(holder.c_pnum.getText().toString()).removeValue();
                        patientdismissedbrgy.child(barangay).child(holder.c_pnum.getText().toString()).removeValue();
                        patients.child(holder.c_pnum.getText().toString()).updateChildren(map);
                        patientongoingall.child(holder.c_pnum.getText().toString()).setValue(map);
                        patientongoing.child(barangay).child(holder.c_pnum.getText().toString()).setValue(map);
                        patientlogs.child(holder.c_pnum.getText().toString()).removeValue();
                        notifications.child(holder.c_pnum.getText().toString()).removeValue();
                        patientmonitoring.child(holder.c_pnum.getText().toString()).removeValue();
                        incrementGraph();
                        dialogPlus.dismiss();
                    }
                });

            }
        });
    }

    @NonNull
    @Override
    public c_Adapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concluded_pitem, parent, false);
        return new c_Adapter.myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        TextView c_pnum, c_pname;
        Button btnreactivate;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            c_pnum = (TextView) itemView.findViewById(R.id.c_pnumber);
            c_pname = (TextView) itemView.findViewById(R.id.c_pname);
            btnreactivate = (Button) itemView.findViewById(R.id.btn_reactivate);
        }
    }
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0 ){
            Toast.makeText(context, "No patients found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    private void incrementGraph(){
        ChartValues.child("des").child(String.valueOf(month)).child("count")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int value = Integer.parseInt(snapshot.getValue().toString());
                        int incvalue = value + 1;
                        snapshot.getRef().setValue(incvalue);
                        //Toast.makeText(context, "Patient account successfully reactivated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error while performing action", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
