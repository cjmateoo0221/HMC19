package com.example.dashboard1.Patient;

import static com.example.dashboard1.Patient.PatientActivity.atoken;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class temp extends Fragment {

    private View rootview;
    String  datecombineapp;
    ImageButton btnnext, btnback;
    String datetoday = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    String status;
    String email;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    String barangay, refnum;
    String username, name;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    EditText txttemp, et_healthcon;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnnext = rootview.findViewById(R.id.temp_btnfinish);
        txttemp = rootview.findViewById(R.id.ctemp);
        btnback = rootview.findViewById(R.id.temp_btnback);
        et_healthcon = rootview.findViewById(R.id.et_healthcon);
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("user","");
        email = sp.getString("email","");
        name = sp.getString("name","");
        barangay = sp.getString("barangay","");
        refnum = sp.getString("refnum","");
        datecombineapp = datetoday+username;
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txttemp.getText().toString().length() == 0){
                    txttemp.setError("Temperature data is Required");
                    Toast.makeText(getContext(), "Temperature data is required", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(txttemp.getText().toString()) >= 43 || Integer.parseInt(txttemp.getText().toString()) <= 32){
                    txttemp.setError("Please enter a valid temperature data.");
                    Toast.makeText(getContext(), "Please enter a valid temperature data.", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm submission of monitoring data?");
                    builder.setMessage("This data will be sent to the MHO admin. Please answer it with utmost honesty");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            /* double totalscore = checklistClass.coughscore + checklistClass.diarrheascore + checklistClass.fatiguescore + checklistClass.headachescore + checklistClass.jointpainscore + checklistClass.shortscore + checklistClass.sorescore + checklistClass.vomitscore;
                            if(totalscore <= 0){
                                status = "No Symptom";
                            }else if (totalscore == 12.5 || totalscore <= 50){
                                status = "Mild";
                            }else if (totalscore == 51 || totalscore <= 87.5){
                                status = "Moderate";
                            }else if (totalscore == 100){
                                status = "Severe";
                            }*/
                            int temp = Integer.parseInt(txttemp.getText().toString());
                            if(checklistClass.cough.equals("No") && checklistClass.headache.equals("No") && checklistClass.fatigue.equals("No") && checklistClass.jointpain.equals("No") && checklistClass.diarrhea.equals("No") && checklistClass.vomit.equals("No") && checklistClass.sorethroat.equals("No") && checklistClass.shortness.equals("No") && temp <= 37){
                                status = "No Symptom";
                            }
                            if((checklistClass.cough.equals("Yes") || checklistClass.headache.equals("Yes") || checklistClass.fatigue.equals("Yes") || checklistClass.jointpain.equals("Yes") || checklistClass.diarrhea.equals("Yes") || checklistClass.vomit.equals("Yes") || checklistClass.sorethroat.equals("Yes")) && checklistClass.shortness.equals("No") && temp <= 37){
                                status = "Mild";
                            }else if ((checklistClass.cough.equals("Yes") || checklistClass.headache.equals("Yes") || checklistClass.fatigue.equals("Yes") || checklistClass.jointpain.equals("Yes") || checklistClass.diarrhea.equals("Yes") || checklistClass.vomit.equals("Yes") || checklistClass.sorethroat.equals("Yes")) && checklistClass.shortness.equals("No") && temp > 37){
                                status = "Mild";
                            }else if (checklistClass.cough.equals("No") && checklistClass.headache.equals("No") && checklistClass.fatigue.equals("No") && checklistClass.jointpain.equals("No") && checklistClass.diarrhea.equals("No") && checklistClass.vomit.equals("No") && checklistClass.sorethroat.equals("No") && checklistClass.shortness.equals("No") && temp > 37){
                                status = "Mild";
                            }
                            if(checklistClass.shortness.equals("Yes") && temp <= 37){
                                status = "Moderate";
                            }else if(checklistClass.shortness.equals("Yes") && temp > 37){
                                status = "Moderate";
                            }else if(checklistClass.cough.equals("Yes") && checklistClass.shortness.equals("Yes") && temp <= 37){
                                status = "Moderate";
                            }
                            if (checklistClass.shortness.equals("Yes") && checklistClass.cough.equals("Yes") && temp > 37){
                                status = "Severe";
                            }
                            Map<String, Object> map = new HashMap<>();
                            map.put("patientnum", username);
                            map.put("name", name);
                            map.put("remark"," ");
                            map.put("status", status);
                            map.put("datecombine", date+username);
                            map.put("dateofmonitor", date);
                            map.put("diarrhea",checklistClass.diarrhea);
                            map.put("cough",checklistClass.cough);
                            map.put("temp", txttemp.getText().toString()+"°C");
                            map.put("fatigue",checklistClass.fatigue);
                            map.put("answered", "no");
                            map.put("healthcon", et_healthcon.getText().toString());
                            map.put("headache",checklistClass.headache);
                            map.put("jointpain",checklistClass.jointpain);
                            map.put("shortness",checklistClass.shortness);
                            map.put("sorethroat",checklistClass.sorethroat);
                            map.put("vomit",checklistClass.vomit);

                            FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child((date+username))
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            updatecurrentStatus();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("action", "Answered Checklist");
                                            map.put("date",datewithtime);
                                            FirebaseDatabase.getInstance().getReference().child("patientlogs").child(username).child(datetoday)
                                                    .push().setValue(map);
                                            sendnotiftoadmin();
                                            setDailystatus();
                                            //Fragmentini.FragmentADD(new donechecklist(), getActivity().getSupportFragmentManager());
                                            resetchecklistData();
                                            Intent intent = new Intent(getActivity(), PatientActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checklistClass.score <= 0){
                    checklistClass.score = 0;
                }else{
                    checklistClass.score = checklistClass.score - 12.5;
                }
                Fragmentini.FragmentReplace(new vomit(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_temp, container, false);
        return  rootview;
    }
    private void updatecurrentStatus(){
        Map<String, Object> map = new HashMap<>();
        map.put("currentStatus", date+status);
        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).child(username).updateChildren(map);
        FirebaseDatabase.getInstance().getReference().child("patients").child(username).updateChildren(map);
        FirebaseDatabase.getInstance().getReference().child("patientongoingall").child(username).updateChildren(map);
    }
    public void sendnotiftoadmin(){
       Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("action", "Answered checklist for " +date);
        map.put("patientnum", username);
        FirebaseDatabase.getInstance().getReference().child("adminnotif").child(datetoday).push()
                .setValue(map);
        FCMSend.Builder build = new FCMSend.Builder(atoken)
                .setTitle(name)
                .setBody("Answered checklist for " +date)
                .setClickAction("<Action>");
        build.send().Result();
    }
    private void resetchecklistData(){
        checklistClass.cough = "";
        checklistClass.coughscore = 0.0;
        checklistClass.diarrhea = "";
        checklistClass.diarrheascore = 0.0;
        checklistClass.sorethroat = "";
        checklistClass.sorescore = 0.0;
        checklistClass.jointpain = "";
        checklistClass.jointpainscore = 0.0;
        checklistClass.shortness = "";
        checklistClass.shortscore = 0.0;
        checklistClass.headache="";
        checklistClass.headachescore = 0.0;
        checklistClass.fatigue = "";
        checklistClass.fatiguescore = 0.0;
        checklistClass.vomit = "";
        checklistClass.vomitscore = 0.0;
    }

    private void setDailystatus(){
        Map<String, Object> map = new HashMap<>();
        map.put("patientnum", username);
        map.put("barangay", barangay);
        map.put("email", email);
        map.put("name", name);
        map.put("status", status);
        map.put("date", datetoday);
        map.put("currentStatus", datetoday+status);
        map.put("refnum", refnum);

        FirebaseDatabase.getInstance().getReference().child("dailystatus").child(datetoday+status).child(barangay).child(username).setValue(map);
    }
}