package com.example.dashboard1.Patient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import static com.example.dashboard1.Patient.PatientActivity.atoken;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.R;
import com.example.dashboard1.donechecklist;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class checklistfragment extends Fragment {
    String datecombine, datecombineapp;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference login = database.getReference("login");
    DatabaseReference adminsentnotif = database.getReference("adminsentnotif");
    DatabaseReference patientcount = database.getReference("patientcount");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference adminlogs = database.getReference("adminlogs");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
    String datetoday = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    EditText message;
    FloatingActionButton fab;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    DateFormat df = new SimpleDateFormat("MM.dd.yyyy 'at' hh:mm:ss aa");
    String datef= df.format(Calendar.getInstance().getTime());
    String username, name;
    TextView tv_donechecklist, tv_recovered, tv_datetoday;
    ScrollView sv_checklist;
    LinearLayout ll_answerchecklist;
    Button btnansChecklist;
    String barangay, refnum;
    double score = 0.0;
    private View rootview;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_donechecklist = rootview.findViewById(R.id.tv_donechecklist);
        tv_datetoday = rootview.findViewById(R.id.tv_datetoday);
        tv_recovered = rootview.findViewById(R.id.tv_recovered);
        btnansChecklist = rootview.findViewById(R.id.btnansChecklist);
        ll_answerchecklist = rootview.findViewById(R.id.ll_answerchecklist);
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("user","");
        name = sp.getString("name","");
        datecombineapp = datetoday+username;
        barangay = sp.getString("barangay","");
        refnum = sp.getString("refnum","");
        tv_datetoday.setText(getTodaysDate());

        checkifended();

        btnansChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), checklistActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      rootview = inflater.inflate(R.layout.fragment_checklist, container, false);
      return rootview;

    }
    private void logs(){
        Map<String, Object> map = new HashMap<>();
        map.put("action", "Answered Checklist");
        map.put("date",datef);
        FirebaseDatabase.getInstance().getReference().child("patientlogs").child(username)
                .push();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    private void checkifended(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String endedDate = snapshot.child("dateended").getValue().toString();
                        if(endedDate.equals("N/A")){
                            FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(datecombineapp)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                ll_answerchecklist.setVisibility(View.INVISIBLE);
                                                tv_donechecklist.setVisibility(View.VISIBLE);
                                                tv_recovered.setVisibility(View.INVISIBLE);
                                            }else{
                                                resetcurrentstatus();
                                                ll_answerchecklist.setVisibility(View.VISIBLE);
                                                tv_donechecklist.setVisibility(View.INVISIBLE);
                                                tv_recovered.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else{
                            ll_answerchecklist.setVisibility(View.INVISIBLE);
                            tv_donechecklist.setVisibility(View.INVISIBLE);
                            tv_recovered.setVisibility(View.VISIBLE);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private String makeDateString(int month, int day, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";

        return "JAN";
    }
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(month, day, year);
    }
    private void resetcurrentstatus(){
        Map<String, Object> map = new HashMap<>();
        map.put("currentStatus", "N/A");
        patientongoingall.child(username).updateChildren(map);
        patients.child(username).updateChildren(map);
    }
}