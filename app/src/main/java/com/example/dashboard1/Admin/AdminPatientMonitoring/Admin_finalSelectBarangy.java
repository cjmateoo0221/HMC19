package com.example.dashboard1.Admin.AdminPatientMonitoring;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientView.sbrgyAdapter;
import com.example.dashboard1.Admin.AdminPatientView.sbrgyModel;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Admin_finalSelectBarangy extends AppCompatActivity {
   public static String barangay, status;
    RecyclerView recyclerView;
    TextView topText1;
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_finalSelectBarangy.this);
    brgyAdapter adapter;
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_final_select_barangy);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        recyclerView = findViewById(R.id.rv_brgy);
        topText1 = findViewById(R.id.topText1);
        getbarangayandstatus();
        Toolbar toolbar = findViewById(R.id.toolbar);

      if(status.equals("Severe")){
          topText1.setText("Severe");
          topText1.setTextColor(Color.parseColor("#D50000"));
        }else if (status.equals("Mild")){
          topText1.setText("Mild");
          topText1.setTextColor(Color.parseColor("#FFD600"));
        }else if (status.equals("Moderate")){
          topText1.setText("Moderate");
          topText1.setTextColor(Color.parseColor("#FF6D00"));
        }else if (status.equals("No Symptom")){
          topText1.setText("Asymptomatic");
          topText1.setTextColor(Color.parseColor("#FF77FF00"));
        }else{
          topText1.setText("Not yet Answered");
          topText1.setTextColor(Color.parseColor("#FFFFFF"));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<brgyModel> options =
                new FirebaseRecyclerOptions.Builder<brgyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barangay"), brgyModel.class)
                        .build();
        adapter = new brgyAdapter(options);
        recyclerView.setAdapter(adapter);
        loadingdialog.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingdialog.disMiss();
            }
        }, 1500);
    }

    public void getbarangayandstatus(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
        status = intent.getStringExtra("status");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint("Search by Barangay Name");

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

    private void txtSearch (String str){
        FirebaseRecyclerOptions<brgyModel> options =
                new FirebaseRecyclerOptions.Builder<brgyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barangay").orderByChild("name").startAt(str).endAt(str+ "\uf8ff"), brgyModel.class)
                        .build();
        adapter = new brgyAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
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