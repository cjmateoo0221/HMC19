package com.example.dashboard1.Admin.AdminPatientMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientView.Admin_EditPatient;
import com.example.dashboard1.Admin.ProgramAdapter;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Admin_MonitoringSelectBarangayArea extends AppCompatActivity {
    ListView listView;

    TextView severecount, moderatecount, asympcount, mildcount;
    ArrayList<String> brgylist = new ArrayList<String>();
    ArrayList<String> pcount = new ArrayList<String>();
    String barangay, status;
    Long totalbrgyans;
    Long asympcount1, mildcount1, moderatecount1, severecount1;
    String mildstatus, moderatestatus, severestatus, asympstatus;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    ArrayList<Integer> brgyimage = new ArrayList<Integer>();
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_MonitoringSelectBarangayArea.this);
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_monitoring_select_barangay_area);
        init();
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        getbarangayandstatus();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView topText = (TextView) toolbar.findViewById(R.id.topText);

        if(status.equals("Severe")){
            topText.setText("Severe");
            topText.setTextColor(Color.parseColor("#D50000"));
        }else if (status.equals("Mild")){
            topText.setText("Mild");
            topText.setTextColor(Color.parseColor("#FFD600"));
        }else if (status.equals("Moderate")){
            topText.setText("Moderate");
            topText.setTextColor(Color.parseColor("#FF6D00"));
        }else if (status.equals("No Symptom")){
            topText.setText("Asymptomatic");
            topText.setTextColor(Color.parseColor("#FF77FF00"));
        }else{
            topText.setText("Not yet Answered");
            topText.setTextColor(Color.parseColor("#FFFFFF"));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = findViewById(R.id.lv_barangay);
        FirebaseDatabase.getInstance().getReference("patientongoing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    String brgy = myDataSnapshot.getKey();
                    brgylist.add(myDataSnapshot.getKey());
                    brgyimage.add(R.drawable.brgyicon);
                    if(status.equals("Not Answered")){
                        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+severestatus)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        severecount.setText(Long.toString(snapshot.getChildrenCount()));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+moderatestatus)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        moderatecount.setText(Long.toString(snapshot.getChildrenCount()));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+mildstatus)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mildcount.setText(Long.toString(snapshot.getChildrenCount()));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(brgy).orderByChild("currentStatus").equalTo(date+asympstatus)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        asympcount.setText(Long.toString(snapshot.getChildrenCount()));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                       //

                       //

//
                       // pcount.add(String.valueOf(totalbrgyans));

                        Long totalbrgypatient = myDataSnapshot.getChildrenCount();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                 asympcount1 = Long.parseLong(asympcount.getText().toString());
                                 mildcount1 = Long.parseLong(mildcount.getText().toString());
                                 moderatecount1 = Long.parseLong(moderatecount.getText().toString());
                                 severecount1 = Long.parseLong(severecount.getText().toString());
                                 totalbrgyans = totalbrgypatient - (asympcount1 + mildcount1 + moderatecount1 + severecount1);
                                 pcount.add(String.valueOf(totalbrgyans));
                            }
                        }, 500);



                    }else{
                        FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+status).child(myDataSnapshot.getKey())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pcount.add(String.valueOf(snapshot.getChildrenCount()));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadingdialog.showLoading();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent (Admin_MonitoringSelectBarangayArea.this, Admin_PatientMonitoringActivity.class);
                    intent.putExtra("barangay", brgylist.get(i));
                    intent.putExtra("status", status);
                    intent.putExtra("mild", mildstatus);
                    intent.putExtra("moderate", moderatestatus);
                    intent.putExtra("severe", severestatus);
                    intent.putExtra("asymp", asympstatus);
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Admin_finalMonitoring.class));
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void getbarangayandstatus(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
        status = intent.getStringExtra("status");
        mildstatus = intent.getStringExtra("mild");
        moderatestatus = intent.getStringExtra("moderate");
        severestatus = intent.getStringExtra("severe");
        asympstatus = intent.getStringExtra("asymp");
    }
    private void init(){
        severecount = findViewById(R.id.brgy_severecount);
        mildcount = findViewById(R.id.brgy_mildcount);
        moderatecount = findViewById(R.id.brgy_moderatecount);
        asympcount = findViewById(R.id.brgy_nosympcount);
    }
    private boolean isOnline() {
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
    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                result = isOnline();
                if(result == false){
                    alert11.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }else{
                    alert11.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
    private void initinternetdia(){
        builder1.setTitle("Could not connect to the server");
        builder1.setMessage("Please check your internet connection and try again.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert11 = builder1.create();
    }
}