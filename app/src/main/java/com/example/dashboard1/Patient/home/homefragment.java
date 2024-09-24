package com.example.dashboard1.Patient.home;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalMonitoring;
import com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient.Admin_newPerPatientMonitor;
import com.example.dashboard1.Admin.Admin_AddPatientActivity;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Loadingdialog1;
import com.example.dashboard1.Patient.Fragmentini;
import com.example.dashboard1.Patient.checklistfragment;
import com.example.dashboard1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.shuhart.stepview.StepView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class homefragment extends Fragment {
    private View rootview;
    ScrollView sv_mainhome;
    ImageView img_statusinfo;
    Loadingdialog loadingdialog;
    LinearLayout ll_step, ll_progressdone, ll_notanschecklistdata, ll_statusnotans, ll_notans, ll_done, ll_nostatus, ll_answer, ll_checklistable, ll_nochecklistdata, ll_nosymp, ll_mild, ll_moderate, ll_severe;
    TextView tv_recovered, tv_endDate, cough, temp, sorethroat, shortness, vomit, diarrhea, fatigue, headache, jointpain;
    String username;
    BottomNavigationView bottomNavigationView;
    StepView stepView;
    RecyclerView recyclerView;
    Button btn_answer;
    String selecteddate;
    HorizontalCalendar horizontalCalendar;
    Calendar startDate, endDate;
    String patientstartDate, patientendDate;
    String date;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_homefragment, container, false);
        loadingdialog = new Loadingdialog(getActivity());
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.btmnav);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img_statusinfo = rootview.findViewById(R.id.img_statusinfo);
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("user","");
        ll_progressdone = rootview.findViewById(R.id.ll_progressdone);
        ll_step = rootview.findViewById(R.id.ll_step);
        patientstartDate = sp.getString("startDate","");
        patientendDate = sp.getString("endDate","");
        cough = rootview.findViewById(R.id.htv_cough);
        ll_notanschecklistdata = rootview.findViewById(R.id.ll_notanschecklistdata);
        btn_answer = rootview.findViewById(R.id.btn_answer);
        ll_statusnotans = rootview.findViewById(R.id.ll_statusnotans);
        ll_notans = rootview.findViewById(R.id.ll_notans);
        stepView = rootview.findViewById(R.id.step_view);
        diarrhea = rootview.findViewById(R.id.htv_diarrhea);
        ll_done = rootview.findViewById(R.id.ll_done);
        sv_mainhome = rootview.findViewById(R.id.sv_mainhome);
        ll_answer = rootview.findViewById(R.id.ll_answer);
        ll_nostatus = rootview.findViewById(R.id.ll_nostatus);
        ll_checklistable = rootview.findViewById(R.id.ll_checklistable);
        ll_nochecklistdata = rootview.findViewById(R.id.ll_nochecklistdata);
        ll_nosymp = rootview.findViewById(R.id.ll_nosymp);
        ll_mild = rootview.findViewById(R.id.ll_mild);
        ll_moderate = rootview.findViewById(R.id.ll_moderate);
        ll_severe = rootview.findViewById(R.id.ll_severe);
        fatigue = rootview.findViewById(R.id.htv_fatigue);
        headache = rootview.findViewById(R.id.htv_headache);
        jointpain = rootview.findViewById(R.id.htv_jointpain);
        shortness = rootview.findViewById(R.id.htv_shortness);
        sorethroat = rootview.findViewById(R.id.htv_sorethroat);
        temp = rootview.findViewById(R.id.htv_temp);
        vomit = rootview.findViewById(R.id.htv_vomit);
        date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        getstartDate();
        getstartDay();
        /* starts before 1 month from now */
        /*
        String enddate = "04-30-2022";
        /* ends after 1 month from now
        Calendar endDate = Calendar.getInstance();
        try {
            endDate.setTime(sdf.parse(enddate));
        } catch (ParseException e) {
            e.printStackTrace();*/
        /* starts before 1 month from now */

        /* ends after 1 month from now */
        getchecklistdata();
        checkchecklist();
        getpatientStatus();
        loadingdialog.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingdialog.disMiss();
            }
        }, 1000);
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragmentini.FragmentReplace(new checklistfragment(), getParentFragmentManager());
                bottomNavigationView.setSelectedItemId(R.id.checklist);
            }
        });
        img_statusinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus diaall = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(R.layout.statusinfo_popup))
                        .setExpanded(true, 1000)
                        .create();
                View v = diaall.getHolderView();
                ImageView img_closepwdfinfo = v.findViewById(R.id.img_closepwdinfo);
                diaall.show();
                img_closepwdfinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diaall.dismiss();
                    }
                });
            }
        });
    }
    public void getchecklistdata(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(date+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_checklistable.setVisibility(View.VISIBLE);
                            cough.setText(snapshot.child("cough").getValue().toString());
                            diarrhea.setText(snapshot.child("diarrhea").getValue().toString());
                            fatigue.setText(snapshot.child("fatigue").getValue().toString());
                            headache.setText(snapshot.child("headache").getValue().toString());
                            jointpain.setText(snapshot.child("jointpain").getValue().toString());
                            shortness.setText(snapshot.child("shortness").getValue().toString());
                            sorethroat.setText(snapshot.child("sorethroat").getValue().toString());
                            temp.setText(snapshot.child("temp").getValue().toString());
                            vomit.setText(snapshot.child("vomit").getValue().toString());
                            ll_nochecklistdata.setVisibility(View.INVISIBLE);
                        }else{
                            ll_nochecklistdata.setVisibility(View.VISIBLE);
                            ll_checklistable.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getchecklistdataonclick(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(selecteddate+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_checklistable.setVisibility(View.VISIBLE);
                            cough.setText(snapshot.child("cough").getValue().toString());
                            diarrhea.setText(snapshot.child("diarrhea").getValue().toString());
                            fatigue.setText(snapshot.child("fatigue").getValue().toString());
                            headache.setText(snapshot.child("headache").getValue().toString());
                            jointpain.setText(snapshot.child("jointpain").getValue().toString());
                            shortness.setText(snapshot.child("shortness").getValue().toString());
                            sorethroat.setText(snapshot.child("sorethroat").getValue().toString());
                            temp.setText(snapshot.child("temp").getValue().toString());
                            vomit.setText(snapshot.child("vomit").getValue().toString());
                            ll_nochecklistdata.setVisibility(View.INVISIBLE);
                            ll_notanschecklistdata.setVisibility(View.INVISIBLE);
                        }else{
                            if(!selecteddate.equals(date)){
                                ll_checklistable.setVisibility(View.INVISIBLE);
                                ll_nochecklistdata.setVisibility(View.INVISIBLE);
                                ll_notanschecklistdata.setVisibility(View.VISIBLE);
                            }else{
                                ll_nochecklistdata.setVisibility(View.VISIBLE);
                                ll_checklistable.setVisibility(View.INVISIBLE);
                                ll_notanschecklistdata.setVisibility(View.INVISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void checkchecklist(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(date+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_done.setVisibility(View.VISIBLE);
                            ll_answer.setVisibility(View.INVISIBLE);
                        }else{
                            ll_answer.setVisibility(View.VISIBLE);
                            ll_done.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void checkchecklistonclick(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(selecteddate+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_done.setVisibility(View.VISIBLE);
                            ll_answer.setVisibility(View.INVISIBLE);
                            ll_notans.setVisibility(View.INVISIBLE);
                        }else{
                            if (!selecteddate.equals(date)) {
                                ll_answer.setVisibility(View.INVISIBLE);
                                ll_done.setVisibility(View.INVISIBLE);
                                ll_notans.setVisibility(View.VISIBLE);
                            }else{
                                ll_answer.setVisibility(View.VISIBLE);
                                ll_done.setVisibility(View.INVISIBLE);
                                ll_notans.setVisibility(View.INVISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getstartDate(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        patientstartDate = snapshot.child("dateadded").getValue().toString();
                        patientendDate = snapshot.child("dateended").getValue().toString();
                        startDate = Calendar.getInstance();
                        endDate = Calendar.getInstance();

                        try {
                            startDate.setTime(sdf.parse(patientstartDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(patientendDate.equals("N/A")){
                            endDate.setTime(Calendar.getInstance().getTime());
                        }else{
                            try {
                                endDate.setTime(sdf.parse(patientendDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        horizontalCalendar = new HorizontalCalendar.Builder(rootview, R.id.calendarView)
                                .range(startDate, endDate)
                                .datesNumberOnScreen(3)
                                .build();
                        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                            @Override
                            public void onDateSelected(Calendar date, int position) {
                                selecteddate = sdf.format(date.getTime());
                                    getchecklistdataonclick();
                                    checkchecklistonclick();
                                    getpatientStatusonClick();


               /* FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(selecteddate+username)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    ll_checklistable.setVisibility(View.VISIBLE);
                                    cough.setText(snapshot.child("cough").getValue().toString());
                                    diarrhea.setText(snapshot.child("diarrhea").getValue().toString());
                                    fatigue.setText(snapshot.child("fatigue").getValue().toString());
                                    headache.setText(snapshot.child("headache").getValue().toString());
                                    jointpain.setText(snapshot.child("jointpain").getValue().toString());
                                    shortness.setText(snapshot.child("shortness").getValue().toString());
                                    sorethroat.setText(snapshot.child("sorethroat").getValue().toString());
                                    temp.setText(snapshot.child("temp").getValue().toString());
                                    vomit.setText(snapshot.child("vomit").getValue().toString());
                                    ll_nochecklistdata.setVisibility(View.INVISIBLE);
                                }else{
                                    ll_nochecklistdata.setVisibility(View.VISIBLE);
                                    ll_checklistable.setVisibility(View.INVISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });*/
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getstartDay(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pendDate = snapshot.child("dateended").getValue().toString();
                        if(pendDate.equals("N/A")){
                            ll_step.setVisibility(View.VISIBLE);
                            ll_step.bringToFront();
                            ll_progressdone.setVisibility(View.INVISIBLE);
                            FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Calendar cal = Calendar.getInstance();
                                            int today = cal.get(Calendar.DAY_OF_MONTH);
                                            String startday = snapshot.child("startday").getValue().toString();
                                            int cstartday = Integer.parseInt(startday);
                                            if(cstartday == 30 && today < 31){
                                                int dimstartday = cstartday - 30;
                                                int fnstep = today - dimstartday;
                                                stepView.go(fnstep, true);
                                            }else if(cstartday == 31 && today < 31){
                                                int dimstartday = cstartday - 31;
                                                int fnstep = today - dimstartday;
                                                stepView.go(fnstep, true);
                                            }
                                            else{
                                                int stepcount = today - cstartday;
                                                stepView.go(stepcount, true);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else{
                            ll_progressdone.bringToFront();
                            ll_progressdone.setVisibility(View.VISIBLE);
                            ll_step.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void getpatientStatusonClick(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(selecteddate+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String status = snapshot.child("status").getValue().toString();
                            if(status.equals("No Symptom")){
                                ll_nosymp.setVisibility(View.VISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_statusnotans.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if(status.equals("Mild")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.VISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_statusnotans.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Moderate")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.VISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_statusnotans.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Severe")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.VISIBLE);
                                ll_statusnotans.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            if(!selecteddate.equals(date)){
                                ll_statusnotans.setVisibility(View.VISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else{
                                ll_statusnotans.setVisibility(View.INVISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getpatientStatus(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(date+username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String status = snapshot.child("status").getValue().toString();
                            if(status.equals("No Symptom")){
                                ll_nosymp.setVisibility(View.VISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if(status.equals("Mild")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.VISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Moderate")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.VISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Severe")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.VISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            ll_nosymp.setVisibility(View.INVISIBLE);
                            ll_mild.setVisibility(View.INVISIBLE);
                            ll_moderate.setVisibility(View.INVISIBLE);
                            ll_severe.setVisibility(View.INVISIBLE);
                            ll_nostatus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}