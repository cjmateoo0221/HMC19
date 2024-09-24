package com.example.dashboard1.Admin.reactivate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.Admin.adminlogs.Model;
import com.example.dashboard1.Admin.adminviewplogs.Admin_selectpforlogs;
import com.example.dashboard1.Admin.adminviewplogs.selectpAdapter;
import com.example.dashboard1.Admin.adminviewplogs.selectpModel;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;

public class Admin_reactivatepAccount extends AppCompatActivity {
    RecyclerView recyclerView;
    public static Context context;
    c_Adapter cAdapter;
    String barangay;
    Handler handler = new Handler();
    AmazingSpinner as_sort;
    ImageView img_clearsort;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reactivatep_account);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        context = getApplicationContext();
        getbarangay();
        img_clearsort = findViewById(R.id.img_clearsort);
        img_clearsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_sort.setText("A-Z",false);
                FirebaseRecyclerOptions<c_Model> options =
                        new FirebaseRecyclerOptions.Builder<c_Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay), c_Model.class)
                                .build();
                cAdapter = new c_Adapter(options);
                cAdapter.startListening();
                recyclerView.setAdapter(cAdapter);
                img_clearsort.setVisibility(View.INVISIBLE);
            }
        });
        as_sort = findViewById(R.id.as_sort);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Admin_reactivatepAccount.this, android.R.layout.simple_spinner_item, sort_data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        as_sort.setAdapter(adapter1);
        as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String letter = adapterView.getItemAtPosition(i).toString();

                img_clearsort.setVisibility(View.VISIBLE);
                FirebaseRecyclerOptions<c_Model> options =
                        new FirebaseRecyclerOptions.Builder<c_Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay).orderByChild("firstname").startAt(letter).endAt(letter+ "\uf8ff"), c_Model.class)
                                .build();
                cAdapter = new c_Adapter(options);
                cAdapter.startListening();
                recyclerView.setAdapter(cAdapter);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_concludedpatients);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<c_Model> options =
                new FirebaseRecyclerOptions.Builder<c_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay), c_Model.class)
                        .build();
        cAdapter = new c_Adapter(options);
        recyclerView.setAdapter(cAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Admin_finalreactSelectBrgy.class));
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        cAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cAdapter != null){
            cAdapter.notifyDataSetChanged();
        }
    }
    public void getbarangay(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
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
