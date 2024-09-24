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
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalMonitoring;
import com.example.dashboard1.Admin.AdminPatientMonitoring.brgyAdapter;
import com.example.dashboard1.Admin.AdminPatientMonitoring.brgyModel;
import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay;
import com.example.dashboard1.Admin.AdminPatientView.sbrgyAdapter;
import com.example.dashboard1.Admin.AdminPatientView.sbrgyModel;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Admin_finalreactSelectBrgy extends AppCompatActivity {
    RecyclerView recyclerView;
    reactbrgyAdapter adapter;
    public static Context context;
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_finalreactSelectBrgy.this);
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_finalreact_select_brgy);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        context = getApplicationContext();
        recyclerView = findViewById(R.id.rv_preactbrgy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<reactbrgyModel> options =
                new FirebaseRecyclerOptions.Builder<reactbrgyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barangay"), reactbrgyModel.class)
                        .build();
        adapter = new reactbrgyAdapter(options);
        recyclerView.setAdapter(adapter);
        loadingdialog.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingdialog.disMiss();
            }
        }, 1500);
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
        startActivity(new Intent(this, AdminSettings.class));
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
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
        FirebaseRecyclerOptions<reactbrgyModel> options =
                new FirebaseRecyclerOptions.Builder<reactbrgyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("barangay").orderByChild("name").startAt(str).endAt(str+ "\uf8ff"), reactbrgyModel.class)
                        .build();
        adapter = new reactbrgyAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
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