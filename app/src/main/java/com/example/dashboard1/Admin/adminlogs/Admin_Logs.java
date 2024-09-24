package com.example.dashboard1.Admin.adminlogs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Admin_Logs extends AppCompatActivity {
    RecyclerView recyclerView;

    private DatePickerDialog datePickerDialog;
    Calendar selectedmonth = Calendar.getInstance();
    Calendar finaldate = Calendar.getInstance();
    int yeartoday = finaldate.get(Calendar.YEAR);
    TextView txt_year;
    ImageView img_datepicker;
    HorizontalCalendar horizontalCalendar;

    public static Context context;
    String selecteddate;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    Adapter adapter;
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logs);
        txt_year = findViewById(R.id.txt_year);
        txt_year.setText(Integer.toString(yeartoday));
        img_datepicker = findViewById(R.id.img_datepicker);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        initDatePicker();
        result = isOnline();
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(Calendar.getInstance().getTime());
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(3)
                .build();
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.rvlogs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("adminlogs").child(date), Model.class)
                .build();
        adapter = new Adapter(options);
        recyclerView.setAdapter(adapter);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selecteddate = sdf.format(date.getTime());
                FirebaseRecyclerOptions<Model> options =
                        new FirebaseRecyclerOptions.Builder<Model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("adminlogs").child(selecteddate), Model.class)
                                .build();
                adapter = new Adapter(options);
                adapter.startListening();
                recyclerView.setAdapter(adapter);

            }
            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                Calendar startDate = Calendar.getInstance();
                startDate.add(Calendar.MONTH, -1);
                /* ends after 1 month from now */
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(Calendar.getInstance().getTime());
                txt_year.setText(Integer.toString(yeartoday));
                horizontalCalendar.setRange(startDate, endDate);
                horizontalCalendar.goToday(true);
                horizontalCalendar.refresh();
                return true;
            }
        });
        img_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter.notifyDataSetChanged();
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

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String makedate = Integer.toString(month) + "-" + Integer.toString(day) + "-" + Integer.toString(year);

                String finaldate1 = Integer.toString(month-1) + "-" + Integer.toString(day) + "-" + Integer.toString(year);
                try {
                    selectedmonth.setTime(sdf.parse(makedate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    finaldate.setTime(sdf.parse(finaldate1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Calendar endDate = Calendar.getInstance();
                endDate.setTime(Calendar.getInstance().getTime());
                horizontalCalendar.setRange(finaldate, endDate);
                horizontalCalendar.selectDate(selectedmonth,true);
                txt_year.setText(Integer.toString(year));
                horizontalCalendar.refresh();
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
}