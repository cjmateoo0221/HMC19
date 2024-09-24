package com.example.dashboard1.Admin.AdminPatientMonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dashboard1.R;

public class Admin_notansweredactivity extends AppCompatActivity {
    private String asympstatus;
    private String severestatus;
    private String barangay;
    private String stat;
    private String mildstatus;
    public static Context context;
    private String moderatestatus;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notansweredactivity);
        getbarangayandstatus();
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView topText = (TextView) toolbar.findViewById(R.id.topText);
        if(stat.equals("Severe")){
            topText.setText("Severe");
            topText.setTextColor(Color.parseColor("#D50000"));
        }else if (stat.equals("Mild")){
            topText.setText("Mild");
            topText.setTextColor(Color.parseColor("#FFD600"));
        }else if (stat.equals("Moderate")){
            topText.setText("Mild");
            topText.setTextColor(Color.parseColor("#FF6D00"));
        }else if (stat.equals("No Symptom")){
            topText.setText("Asymptomatic");
            topText.setTextColor(Color.parseColor("#00C853"));
        }else{
            topText.setText("Not yet Answered");
            topText.setTextColor(Color.parseColor("#FFFFFF"));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView= (RecyclerView) findViewById(R.id.rv_notanswered);
        context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void getbarangayandstatus(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
        stat = intent.getStringExtra("status");
        mildstatus = intent.getStringExtra("mild");
        moderatestatus = intent.getStringExtra("moderate");
        severestatus = intent.getStringExtra("severe");
        asympstatus = intent.getStringExtra("asymp");
    }
}