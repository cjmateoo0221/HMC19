package com.example.dashboard1.Admin.AdminPatientMonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay;
import com.example.dashboard1.Admin.Admin_AddPatientActivity;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Login;
import com.example.dashboard1.R;
import com.example.dashboard1.preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Admin_finalMonitoring extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String selecteddate;
    Handler handler = new Handler();
    Calendar selectedmonth = Calendar.getInstance();
    Calendar finaldate = Calendar.getInstance();
    int yeartoday = finaldate.get(Calendar.YEAR);
    TextView txt_year;
    HorizontalCalendar horizontalCalendar;
    private DatePickerDialog datePickerDialog;
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    ImageView img_datepicker;
    int delay = 2 * 1000;
    Button btn_viewpatient;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_finalMonitoring.this);
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference login = database.getReference("login");
    DatabaseReference adminsentnotif = database.getReference("adminsentnotif");
    DatabaseReference patientcount = database.getReference("patientcount");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference adminlogs = database.getReference("adminlogs");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
     TextView tv_totalpatients, tv_notanscount, tv_moderatecount, tv_severecount, tv_mildcount, tv_asympcount, tv_mdatettoday;
     String datetoday = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
     CardView btn_asymp, btn_mild, btn_moderate, btn_severe, btn_notanswered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_final_monitoring);
        init();
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        initDatePicker();
        result = isOnline();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        loadingdialog.showLoading();
        gettotalpatients();
        getmildCount();
        getasympcount();
        getsevereCount();
        getmoderateCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getnotanscount();
                loadingdialog.disMiss();
            }
        }, 1500);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selecteddate = sdf.format(date.getTime());
                if(selecteddate.equals(datetoday)){
                    getmildCount();
                    getasympcount();
                    getsevereCount();
                    getmoderateCount();
                    gettotalpatients();
                    btn_asymp.setEnabled(true);
                    btn_mild.setEnabled(true);
                    btn_moderate.setEnabled(true);
                    btn_severe.setEnabled(true);
                    btn_notanswered.setEnabled(true);
                    loadingdialog.showLoading();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getnotanscount();
                            loadingdialog.disMiss();
                        }
                    }, 1000);
                }else{

                    getnotanshistory();
                    getasymphistory();
                    getmildhistory();
                    getmoderatehistory();
                    getseverehistory();
                    btn_asymp.setEnabled(false);
                   btn_mild.setEnabled(false);
                   btn_moderate.setEnabled(false);
                   btn_severe.setEnabled(false);
                   btn_notanswered.setEnabled(false);
                   loadingdialog.showLoading();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gettotalpatienthistory();
                            loadingdialog.disMiss();
                        }
                    }, 1500);

                }
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
        btn_severe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_severecount.getText().toString().equals("0")){
                    Toast.makeText(Admin_finalMonitoring.this, "No Severe Patient Found.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent (Admin_finalMonitoring.this, Admin_finalSelectBarangy.class);
                    intent.putExtra("status", "Severe");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btn_asymp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_asympcount.getText().toString().equals("0")){
                    Toast.makeText(Admin_finalMonitoring.this, "No Asymptomatic Patient Found", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent (Admin_finalMonitoring.this, Admin_finalSelectBarangy.class);
                    intent.putExtra("status", "No Symptom");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btn_viewpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Admin_finalMonitoring.this, Admin_PatientViewSelectBarangay.class);
                intent.putExtra("from", "monitoring");
                //intent.putExtra("name", holder.name.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        img_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        btn_mild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_mildcount.getText().toString().equals("0")){
                    Toast.makeText(Admin_finalMonitoring.this, "No Mild Patient Found ", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent (Admin_finalMonitoring.this, Admin_finalSelectBarangy.class);
                    intent.putExtra("status", "Mild");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
            }
        });

        btn_moderate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_moderatecount.getText().toString().equals("0")){
                    Toast.makeText(Admin_finalMonitoring.this, "No Moderate Patient Found", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent (Admin_finalMonitoring.this, Admin_finalSelectBarangy.class);
                    intent.putExtra("status", "Moderate");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }

            }
        });
        
        btn_notanswered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_notanscount.getText().toString().equals("0")){
                    Toast.makeText(Admin_finalMonitoring.this, "All patients have answered for today", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent (Admin_finalMonitoring.this, Admin_finalSelectBarangy.class);
                    intent.putExtra("status", "Not Answered");
                    intent.putExtra("mild", "Mild");
                    intent.putExtra("moderate", "Moderate");
                    intent.putExtra("severe", "Severe");
                    intent.putExtra("asymp", "No Symptom");
                    //intent.putExtra("name", a.name.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
    private void init(){
        img_datepicker = findViewById(R.id.img_datepicker);
        txt_year = findViewById(R.id.txt_year);
        btn_viewpatient = findViewById(R.id.btn_viewpatient);
        tv_mildcount = findViewById(R.id.tv_mildcount);
        tv_totalpatients = findViewById(R.id.tv_totalpatients);
        tv_moderatecount = findViewById(R.id.tv_moderatecount);
        tv_severecount = findViewById(R.id.tv_severecount);
        tv_asympcount = findViewById(R.id.tv_asympcount);
        tv_notanscount = findViewById(R.id.tv_notanscount);
        btn_mild = findViewById(R.id.btn_mild);
        btn_asymp = findViewById(R.id.btn_asymp);
        btn_moderate = findViewById(R.id.btn_moderate);
        btn_severe = findViewById(R.id.btn_severe);
        btn_notanswered = findViewById(R.id.btn_notanswered);
    }
    private void gettotalpatients(){
        patientongoingall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_totalpatients.setText(String.valueOf(snapshot.getChildrenCount()));
                Map<String, Object> map = new HashMap<>();
                map.put("totalpatient",tv_totalpatients.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("totalpatient").setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getmoderateCount(){
        patientongoingall.orderByChild("currentStatus").equalTo(datetoday+"Moderate")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String moderatecount1 = Long.toString(snapshot.getChildrenCount());
                        tv_moderatecount.setText(moderatecount1);
                        Map<String, Object> map = new HashMap<>();
                        map.put("moderate",tv_moderatecount.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("moderate").setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getmildCount(){
        patientongoingall.orderByChild("currentStatus").equalTo(datetoday+"Mild")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String mildcount1 = Long.toString(snapshot.getChildrenCount());
                        tv_mildcount.setText(mildcount1);
                        Map<String, Object> mildmap = new HashMap<>();
                        mildmap.put("mild",tv_mildcount.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("mild").setValue(mildmap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getasympcount(){
        patientongoingall.orderByChild("currentStatus").equalTo(datetoday+"No Symptom")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String asympcount1 = Long.toString(snapshot.getChildrenCount());
                        tv_asympcount.setText(asympcount1);
                        Map<String, Object> map = new HashMap<>();
                        map.put("asymp",tv_asympcount.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("asymp").setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getsevereCount(){
        patientongoingall.orderByChild("currentStatus").equalTo(datetoday+"Severe")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String severecount1 = Long.toString(snapshot.getChildrenCount());
                        tv_severecount.setText(severecount1);
                        Map<String, Object> map = new HashMap<>();
                        map.put("severe",tv_severecount.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("severe").setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getnotanscount(){
        patientongoingall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long totalpatient = snapshot.getChildrenCount();
                Long asympcount = Long.parseLong(tv_asympcount.getText().toString());
                Long mildcount = Long.parseLong(tv_mildcount.getText().toString());
                Long moderatecount = Long.parseLong(tv_moderatecount.getText().toString());
                Long severecount = Long.parseLong(tv_severecount.getText().toString());

                Long totalanscount = totalpatient - (asympcount + mildcount + moderatecount + severecount);
                tv_notanscount.setText(String.valueOf(totalanscount));
                Map<String, Object> map = new HashMap<>();
                map.put("notans",tv_notanscount.getText().toString());
              FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(datetoday).child("notans").setValue(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private String makeDateString(int month, int day, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";

        return "January";
    }
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(month, day, year);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Admin_finalMonitoring.this, AdminActivity.class));
        finish();
    }
    private void getasymphistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("asymp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_asympcount.setText(snapshot.child("asymp").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getmildhistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("mild")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_mildcount.setText(snapshot.child("mild").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getmoderatehistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("moderate")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_moderatecount.setText(snapshot.child("moderate").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getseverehistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("severe")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_severecount.setText(snapshot.child("severe").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getnotanshistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("notans")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_notanscount.setText(snapshot.child("notans").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    private void gettotalpatienthistory(){
        FirebaseDatabase.getInstance().getReference().child("adminmonitoring").child(selecteddate).child("totalpatient")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            tv_totalpatients.setText(snapshot.child("totalpatient").getValue().toString());
                            Toast.makeText(Admin_finalMonitoring.this, "Data Loaded", Toast.LENGTH_SHORT).show();
                        }else{
                            tv_totalpatients.setText("");
                            tv_notanscount.setText("");
                            tv_severecount.setText("");
                            tv_moderatecount.setText("");
                            tv_mildcount.setText("");
                            tv_asympcount.setText("");
                            Toast.makeText(Admin_finalMonitoring.this, "No data found in the selected date", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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