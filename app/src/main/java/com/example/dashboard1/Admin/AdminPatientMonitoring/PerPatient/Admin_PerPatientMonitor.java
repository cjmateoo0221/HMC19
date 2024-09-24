package com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.barangay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Admin_PerPatientMonitor extends AppCompatActivity {
    RecyclerView recyclerView;
    int delay = 2 * 1000;
    boolean result;
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    public static Context context;
    String name, patientnum, barangay;
    TextView pname, nodata;
    Button btnSendFCM;
    ppAdapter mainAdapter;
    private static String serverkey = "AAAANWe06Ic:APA91bFiJzEsVjD9EZaYxytLPKPxI2S7CNhTKZPO9J9P_51RXWdxpQY-e5pc-70fvSQpgNqUW0wkLupvedA15lcRb1v8RoqagstPnm1iNLE-92fRWIswOUQ1ahnbClSSXgdRNpZPiz10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_per_patient_monitor);
        nodata = findViewById(R.id.nodatappm);
        context = getApplicationContext();
        getnameandpnum();
        pname = findViewById(R.id.M_pname);
        pname.setText(name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv_ppMonitor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ppModel> options =
                new FirebaseRecyclerOptions.Builder<ppModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(patientnum), ppModel.class)
                .build();
        mainAdapter = new ppAdapter(options);
        recyclerView.setAdapter(mainAdapter);

     ///   new Handler().postDelayed(new Runnable() {
      //      @Override
       //     public void run() {
               // if(mainAdapter.getItemCount() == 0){
                 //   Toast.makeText(Admin_PerPatientMonitor.this, "No records found", Toast.LENGTH_SHORT).show();
                    ///nodata.setVisibility(View.VISIBLE);
             //   }
                  //  nodata.setVisibility(View.INVISIBLE);
      //          }
        //    }
   //     }, 2500);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (Admin_PerPatientMonitor.this, Admin_PatientMonitoringActivity.class);
        intent.putExtra("barangay", barangay);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        mainAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null){
            mainAdapter.notifyDataSetChanged();
        }
    }

    public void getnameandpnum(){
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        patientnum = intent.getStringExtra("patientnum");
        barangay = intent.getStringExtra("barangay");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}