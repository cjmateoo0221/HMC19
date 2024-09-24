package com.example.dashboard1.Patient;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Login;
import com.example.dashboard1.Patient.home.homefragment;
import com.example.dashboard1.Patient.notif.PatientNotifView;
import com.example.dashboard1.Patient.patientdetails.PatientPersonalDetailsActivity;
import com.example.dashboard1.Patient.patientlogs.PatientLogs;
import com.example.dashboard1.donechecklist;
import com.example.dashboard1.preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.example.dashboard1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PatientActivity extends AppCompatActivity {
    MenuItem menuItem;
    boolean result;
    Handler handler = new Handler();
    private SharedPreferences sharedPreferenceObj;
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    int delay = 2 * 1000;
    private BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String patienttoken;
    FloatingActionButton fbedit;
    SharedPreferences usersp;
    String bday;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    String user, pass;
    public static String atoken;
    Fragment selectedfragment;
    public static String serverKey = "AAAANWe06Ic:APA91bFiJzEsVjD9EZaYxytLPKPxI2S7CNhTKZPO9J9P_51RXWdxpQY-e5pc-70fvSQpgNqUW0wkLupvedA15lcRb1v8RoqagstPnm1iNLE-92fRWIswOUQ1ahnbClSSXgdRNpZPiz10";
    String username;
    long not1, not2;
    homefragment fragment = new homefragment();
    String datecombine, datecombineapp;
    String datetoday = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    String firstname, lastname, contactnum, address, patientnum;
    FrameLayout click;
    String Firsttime;
    TextView notif, patientname, namenav, contactnav, addressnav, patientid, badgecounter;
    final Loadingdialog loadingdialog = new Loadingdialog(PatientActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_db);
        badgecounter = findViewById(R.id.badge);
        click = findViewById(R.id.clicknotif);
        usersp = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = usersp.getString("user", "");
        builder1 = new AlertDialog.Builder(this);
      //  initinternetdia();
      //  result = isOnline();
        getstartDate();
        getBarangay();
        patientname = findViewById(R.id.ppName);
        datecombineapp = datetoday + username;
        fetchpData();
        setnotif();
        createNotifChannel();
        setReminder();
        sendToken();
        getadmintoken();
        FCMSend.SetServerKey(serverKey);


       Fragmentini.FragmentReplace(new homefragment(), getSupportFragmentManager());


        new Handler().postDelayed(new Runnable() {
          @Override
         public void run() {
                notif.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            badgecounter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
          }
      },2000);

        bottomNavigationView = findViewById(R.id.btmnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        View header = navigationView.getHeaderView(0);
        contactnav = (TextView) header.findViewById(R.id.npContact);
        addressnav = (TextView) header.findViewById(R.id.npAddress);
        patientid = (TextView) header.findViewById(R.id.npID);
        namenav = (TextView) header.findViewById(R.id.npName);
        notif = findViewById(R.id.notif);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        drawerLayout.close();
                        Fragmentini.FragmentReplace(new homefragment(), getSupportFragmentManager());
                        break;

                    case R.id.nav_aboutus:
                        startActivity(new Intent(PatientActivity.this, AboutUs.class));
                        finish();
                        break;

                    case R.id.nav_privacy:
                        startActivity(new Intent(PatientActivity.this, PrivacyPolicy.class));
                        finish();
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
                        builder.setTitle("Confirm Logout?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(PatientActivity.this, Login.class));
                                preferences.clearData(PatientActivity.this);
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                                usersp = getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = usersp.edit();
                                editor.clear();
                                editor.commit();
                                logspatient();
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                        break;

                    case R.id.nav_details:
                        startActivity(new Intent(PatientActivity.this, PatientPersonalDetailsActivity.class));
                        finish();
                        break;
                    case R.id.nav_userpass:
                        AlertDialog.Builder askuserandbday = new AlertDialog.Builder(PatientActivity.this);
                        askuserandbday.setTitle("Please Enter your Birthday Ex. March 20 1999");
                        askuserandbday.setCancelable(false);
                        final EditText bdayinput = new EditText(PatientActivity.this);
                        bdayinput.setInputType(InputType.TYPE_CLASS_TEXT);
                        askuserandbday.setView(bdayinput);


                        askuserandbday.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String getbday = bdayinput.getText().toString();
                                    if(getbday.equals(bday)){
                                        new AlertDialog.Builder(PatientActivity.this)
                                                .setMessage("Username: " +user+ "\n" + "Password: " +pass)
                                                .setNeutralButton("Ok", null)
                                                .setCancelable(false)
                                                .show();
                                   }else{
                                        Toast.makeText(PatientActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }

                            }
                        });

                        askuserandbday.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();
                                }

                        });
                        askuserandbday.show();
                        break;
                    case R.id.nav_logs:
                        startActivity(new Intent(PatientActivity.this, PatientLogs.class));
                        finish();
                }
                return false;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectedfragment = null;
                    switch (item.getItemId()) {
                        case R.id.btm_home:
                            Fragmentini.FragmentReplace(new homefragment(), getSupportFragmentManager());
                            break;
                        case R.id.checklist:
                           // getlatestChecklist();
                            Fragmentini.FragmentReplace(new checklistfragment(), getSupportFragmentManager());
                            break;
                        case R.id.dosdonts:
                            Fragmentini.FragmentReplace(new dosdontsfragment(), getSupportFragmentManager());
                            break;
                        case R.id.hotlines:
                            Fragmentini.FragmentReplace(new hotlinesfragment(), getSupportFragmentManager());
                            break;

                    }
                    return true;
                }
            };


    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*loadingdialog.showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingdialog.disMiss();
            }
        },5000);*/
    }

    public void fetchpData() {
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String finalname;
                        String email = snapshot.child("email").getValue().toString();
                        String refnum = snapshot.child("refnum").getValue().toString();
                        firstname = snapshot.child("firstname").getValue().toString();
                        lastname = snapshot.child("lastname").getValue().toString();
                        contactnum = snapshot.child("contactnum").getValue().toString();
                        address = snapshot.child("address").getValue().toString();
                        patientnum = snapshot.child("patientnum").getValue().toString();
                        bday = snapshot.child("birthday").getValue().toString();
                        user = snapshot.child("user").getValue().toString();
                        pass = snapshot.child("pass").getValue().toString();
                        finalname = firstname + " " + lastname;
                        patientname.setText(finalname);
                        namenav.setText(finalname);
                        addressnav.setText(address);
                        contactnav.setText(contactnum);
                        patientid.setText(patientnum);

                        SharedPreferences.Editor editor = usersp.edit();
                        editor.putString("name", patientname.getText().toString());
                        editor.putString("refnum", refnum);
                        editor.putString("email", email);
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        } else {
            this.moveTaskToBack(true);
        }
    }

    private void getlatestChecklist() {
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dateended = snapshot.child("dateended").getValue().toString();
                if(dateended.equals("N/A")){
                    FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(username).child(datecombineapp)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Fragmentini.FragmentReplace(new donechecklist(), getSupportFragmentManager());
                            /*if(datecombineapp.equals(datecombine)){
                                Fragmentini.FragmentADD(new donechecklist(), getSupportFragmentManager());
                            }else if (!datecombineapp.equals(datecombine)){
                                Fragmentini.FragmentADD(new checklistfragment(), getSupportFragmentManager());
                            }*/
                                    } else {

                                        Fragmentini.FragmentReplace(new checklistfragment(), getSupportFragmentManager());
                                        Toast.makeText(PatientActivity.this, "Checklist is not Answered yet", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }else{
                    Fragmentini.FragmentReplace(new recovered(), getSupportFragmentManager());
                    Toast.makeText(PatientActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notif, menu);
        menuItem = menu.findItem(R.id.nav_notification);
            menuItem.setActionView(R.layout.notification_badge);
            View view = menuItem.getActionView();
            click = view.findViewById(R.id.clicknotif);
            badgecounter = view.findViewById(R.id.badge);
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    badgecounter.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(PatientActivity.this, PatientNotifView.class));
                     finish();
                }
            });
            //badgecounter.setOnClickListener(new View.OnClickListener() {
               // @Override
               // public void onClick(View view) {
                //    badgecounter.setVisibility(View.INVISIBLE);
                   // startActivity(new Intent(PatientActivity.this, PatientNotifView.class));
                //    finish();
             //   }
        //    });
            return super.onCreateOptionsMenu(menu);
    }

    public void setnotif() {
        FirebaseDatabase.getInstance().getReference().child("notifications").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long ns = (long) snapshot.getChildrenCount();
                        notif.setText(Long.toString(ns));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void setReminder(){
        Intent intent = new Intent(PatientActivity.this, ReminderBroadcast.class);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
       if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
       }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);

    }
    private void createNotifChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "testt";
            String description = "Channel for Lezgow";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("test", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService((NotificationManager.class));
            notificationManager.createNotificationChannel(channel);
        }
    }
        private void sendToken(){
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {

                                }
                                // Get new FCM registration token
                                String token = task.getResult();
                                // Log and toast
                                Map<String, Object> map = new HashMap<>();
                                map.put("patienttoken", token);
                                FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                                        .updateChildren(map);
                            }
                        });
            }

            public void checkStatus(){
                FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String getstatus = snapshot.child("status").getValue().toString();
                                if(getstatus.equals("Concluded")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
                                    builder.setMessage("Congratulations on your Recovery! Please check your registered email for the reference number and go to the Muncipal Health Office to get your Quarantine Certificate! You may now Uninstall this app. Thank you for using it!");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            preferences.clearData(PatientActivity.this);
                                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                                            startActivity(new Intent(PatientActivity.this, Login.class));
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }else{
                                    getlatestChecklist();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

    public void getadmintoken(){
        FirebaseDatabase.getInstance().getReference().child("login").child("mhoadmin")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        atoken = snapshot.child("atoken").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getstartDate(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String patientStartdate = snapshot.child("dateadded").getValue().toString();
                        SharedPreferences.Editor editor = usersp.edit();
                        editor.putString("startDate",patientStartdate);
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getendDate(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String patientendDate = snapshot.child("dateended").getValue().toString();
                        SharedPreferences.Editor editor = usersp.edit();
                        editor.putString("endDate",patientendDate);
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void logspatient(){
        Map<String, Object> map1 = new HashMap<>();
        map1.put("action", "Logged Out");
        map1.put("date",datewithtime);
        FirebaseDatabase.getInstance().getReference().child("patientlogs").child(username).child(datetoday)
                .push().setValue(map1);
    }

    private void getBarangay(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String barangay = snapshot.child("barangay").getValue().toString();
                        SharedPreferences.Editor editor = usersp.edit();
                        editor.putString("barangay",barangay);
                        editor.commit();
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

        /*handler.postDelayed( runnable = new Runnable() {
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