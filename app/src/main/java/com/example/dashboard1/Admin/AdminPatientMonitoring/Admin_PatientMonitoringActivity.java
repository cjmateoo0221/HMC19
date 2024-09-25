package com.example.dashboard1.Admin.AdminPatientMonitoring;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.Admin.AdminPatientView.Admin_newAdapter;
import com.example.dashboard1.Admin.AdminPatientView.Admin_newDataModel;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Admin_PatientMonitoringActivity extends AppCompatActivity {
    AmazingSpinner as_sort;
    ImageView img_clearsort;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    RecyclerView recyclerView;
    MonitorAdapter mainAdapter;
    notAnsAdapter notAnsAdapter;
    String mildstatus, moderatestatus, severestatus, asympstatus;
    public static String barangay, stat;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    int delay = 2 * 1000;
    boolean result;
    TextView statusText;
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    public static Context context;
    public static Activity activity;
    public static String serverKey = "Your server key here";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_monitoring);
        builder1 = new AlertDialog.Builder (this);


        initinternetdia();
        result = isOnline();
        getbarangayandstatus();
        statusText = findViewById(R.id.statusText);
        if (stat.equals("No Symptom")) {
            statusText.setText("Asymptomatic");
            statusText.setTextColor(Color.parseColor("#FF77FF00"));
        }else if (stat.equals("Mild")){
            statusText.setText("Mild");
            statusText.setTextColor(Color.parseColor("#FFEB3B"));
        }else if (stat.equals("Moderate")){
            statusText.setText("Moderate");
            statusText.setTextColor(Color.parseColor("#FF5722"));
        }else if (stat.equals("Severe")){
            statusText.setText("Severe");
            statusText.setTextColor(Color.parseColor("#F44336"));
        }else{
            statusText.setText("Not yet Answered");
            statusText.setTextColor(Color.parseColor("#FFFFFF"));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView= (RecyclerView) findViewById(R.id.rv_patientMonitor);
        img_clearsort = findViewById(R.id.img_clearsort);
        as_sort = findViewById(R.id.as_sort);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        as_sort.setAdapter(adapter);
        context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(stat.equals("Not Answered")){
            FirebaseRecyclerOptions<notAnsModel> options =
                    new FirebaseRecyclerOptions.Builder<notAnsModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), notAnsModel.class)
                            .build();
            notAnsAdapter = new notAnsAdapter(options);
            recyclerView.setAdapter(notAnsAdapter);
            img_clearsort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    as_sort.setText("A-Z",false);
                    FirebaseRecyclerOptions<notAnsModel> options =
                            new FirebaseRecyclerOptions.Builder<notAnsModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), notAnsModel.class)
                                    .build();
                    notAnsAdapter = new notAnsAdapter(options);
                    notAnsAdapter.startListening();
                    recyclerView.setAdapter(notAnsAdapter);
                    img_clearsort.setVisibility(View.INVISIBLE);
                }
            });

            as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String letter = adapterView.getItemAtPosition(i).toString();

                    img_clearsort.setVisibility(View.VISIBLE);
                    FirebaseRecyclerOptions<notAnsModel> options =
                            new FirebaseRecyclerOptions.Builder<notAnsModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(letter).endAt(letter+ "\uf8ff"), notAnsModel.class)
                                    .build();
                    notAnsAdapter = new notAnsAdapter(options);
                    notAnsAdapter.startListening();
                    recyclerView.setAdapter(notAnsAdapter);
                }
            });
        }else{
            FirebaseRecyclerOptions<MonitorModel> options =
                    new FirebaseRecyclerOptions.Builder<MonitorModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+stat).child(barangay), MonitorModel.class)
                            .build();
            mainAdapter = new MonitorAdapter(options);
            recyclerView.setAdapter(mainAdapter);
            img_clearsort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    as_sort.setText("A-Z",false);
                    FirebaseRecyclerOptions<MonitorModel> options =
                            new FirebaseRecyclerOptions.Builder<MonitorModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+stat).child(barangay), MonitorModel.class)
                                    .build();
                    mainAdapter = new MonitorAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                    img_clearsort.setVisibility(View.INVISIBLE);
                }
            });

            as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String letter = adapterView.getItemAtPosition(i).toString();

                    img_clearsort.setVisibility(View.VISIBLE);
                    FirebaseRecyclerOptions<MonitorModel> options =
                            new FirebaseRecyclerOptions.Builder<MonitorModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+stat).child(barangay).orderByChild("name").startAt(letter).endAt(letter+ "\uf8ff"), MonitorModel.class)
                                    .build();
                    mainAdapter = new MonitorAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                }
            });
        }

        FCMSend.SetServerKey(serverKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(stat.equals("Not Answered")){
            notAnsAdapter.startListening();
        }else{
            mainAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(stat.equals("Not Answered")){
            if (notAnsAdapter != null){
                notAnsAdapter.notifyDataSetChanged();
            }
        }else{
            if (mainAdapter != null){
                mainAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (Admin_PatientMonitoringActivity.this, Admin_finalSelectBarangy.class);
        intent.putExtra("barangay", barangay);
        intent.putExtra("status", stat);
        intent.putExtra("mild", mildstatus);
        intent.putExtra("moderate", moderatestatus);
        intent.putExtra("severe", severestatus);
        intent.putExtra("asymp", asympstatus);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search by Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }


    private void txtSearch(String str){
        if(stat.equals("Not Answered")){
            FirebaseRecyclerOptions<notAnsModel> options =
                    new FirebaseRecyclerOptions.Builder<notAnsModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(str).endAt(str+ "\uf8ff"), notAnsModel.class)
                            .build();
            notAnsAdapter = new notAnsAdapter(options);
            notAnsAdapter.startListening();
            recyclerView.setAdapter(notAnsAdapter);
        }
        else{
            FirebaseRecyclerOptions<MonitorModel> options =
                    new FirebaseRecyclerOptions.Builder<MonitorModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("dailystatus").child(date+stat).child(barangay).orderByChild("name").startAt(str).endAt(str+ "\uf8ff"), MonitorModel.class)
                            .build();
            mainAdapter = new MonitorAdapter(options);
            mainAdapter.startListening();
            recyclerView.setAdapter(mainAdapter);
        }
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