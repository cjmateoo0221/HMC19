package com.example.dashboard1.Patient.notif;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Patient.PatientActivity;
import com.example.dashboard1.Patient.patientlogs.Adapter;
import com.example.dashboard1.Patient.patientlogs.Model;
import com.example.dashboard1.Patient.patientlogs.PatientLogs;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class PatientNotifView extends AppCompatActivity {
    RecyclerView recyclerView;
    pnadapter mainAdapter;
    public static Context context;
    String selecteddate;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    int delay = 2 * 1000;
    boolean result;
    Handler handler = new Handler();
    DayScrollDatePicker daypick;
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    TextView nodata;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_notif_view);
        context = getApplicationContext();
        nodata = findViewById(R.id.nodatapnotif);
        builder1 = new AlertDialog.Builder(this);
       // initinternetdia();
      //  result = isOnline();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(Calendar.getInstance().getTime());
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(3)
                .build();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("user","");
        recyclerView = (RecyclerView) findViewById(R.id.rv_pNotif);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<pnmodel> options =
                new FirebaseRecyclerOptions.Builder<pnmodel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("notifications").child(username).child(date), pnmodel.class)
                .build();
        mainAdapter = new pnadapter((options));
        recyclerView.setAdapter(mainAdapter);


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selecteddate = sdf.format(date.getTime());
                FirebaseRecyclerOptions<pnmodel> options =
                        new FirebaseRecyclerOptions.Builder<pnmodel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("notifications").child(username).child(selecteddate), pnmodel.class)
                                .build();
                mainAdapter = new pnadapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
            }
        });
    }

    @Override
    protected void onStart() {
        mainAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mainAdapter.stopListening();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, PatientActivity.class));
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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

       /* handler.postDelayed( runnable = new Runnable() {
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
        }, delay);*/

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