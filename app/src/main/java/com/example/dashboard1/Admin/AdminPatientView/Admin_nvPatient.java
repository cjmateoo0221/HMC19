package com.example.dashboard1.Admin.AdminPatientView;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_nvPatient extends AppCompatActivity {
    RecyclerView recyclerView;
    String barangay, pnum, act;
    Admin_PatientViewMainAdapter mainAdapter;
    public static Context context;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nv_patient);
        getbarangaypnum();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.rv_nvpdata);
        context = this;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
       FirebaseRecyclerOptions<Admin_PatientViewDataModel> options =
                new FirebaseRecyclerOptions.Builder<Admin_PatientViewDataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("patientnum").equalTo(pnum), Admin_PatientViewDataModel.class)
                        .build();
        mainAdapter = new Admin_PatientViewMainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainAdapter != null){
            mainAdapter.notifyDataSetChanged();
        }
    }

    public void getbarangaypnum(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
        pnum = intent.getStringExtra("patientnum");
        act = intent.getStringExtra("from");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (this, Admin_PatientViewActivity.class);
        intent.putExtra("barangay", barangay);
        intent.putExtra("from", act);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}