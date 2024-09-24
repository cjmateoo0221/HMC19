package com.example.dashboard1.Admin.adminviewplogs;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalSelectBarangy;
import com.example.dashboard1.Admin.AdminPatientView.Admin_newAdapter;
import com.example.dashboard1.Admin.AdminPatientView.Admin_newDataModel;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;

public class Admin_selectpforlogs extends AppCompatActivity {
    RecyclerView recyclerView;
    selectpAdapter adapter;
    TextView plogsbrgyname;
    public static String barangay;
    AmazingSpinner as_sort;
    ImageView img_clearsort;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selectpforlogs);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        img_clearsort = findViewById(R.id.img_clearsort);
        img_clearsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_sort.setText("A-Z",false);
                FirebaseRecyclerOptions<selectpModel> options =
                        new FirebaseRecyclerOptions.Builder<selectpModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), selectpModel.class)
                                .build();
                adapter = new selectpAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
                img_clearsort.setVisibility(View.INVISIBLE);
            }
        });
        as_sort = findViewById(R.id.as_sort);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Admin_selectpforlogs.this, android.R.layout.simple_spinner_item, sort_data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        as_sort.setAdapter(adapter1);
        as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String letter = adapterView.getItemAtPosition(i).toString();

                img_clearsort.setVisibility(View.VISIBLE);
                FirebaseRecyclerOptions<selectpModel> options =
                        new FirebaseRecyclerOptions.Builder<selectpModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(letter).endAt(letter+ "\uf8ff"), selectpModel.class)
                                .build();
                adapter = new selectpAdapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }
        });
        getbarangay();
        plogsbrgyname = findViewById(R.id.plogsbrgyname);
        plogsbrgyname.setText(barangay);
        recyclerView = findViewById(R.id.rv_selectpatientlogs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<selectpModel> options =
                new FirebaseRecyclerOptions.Builder<selectpModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), selectpModel.class)
                        .build();
        adapter = new selectpAdapter(options);
        recyclerView.setAdapter(adapter);
    }
    public void getbarangay(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Admin_selectbrgyplogs.class));
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
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