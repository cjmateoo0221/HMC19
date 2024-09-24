package com.example.dashboard1.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_MonitoringSelectBarangayArea;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalMonitoring;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_finalSelectBarangy;
import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity;
import com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay;
import com.example.dashboard1.Admin.newnotif.AdminNotif;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Login;
import com.example.dashboard1.R;
import com.example.dashboard1.preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    CardView btnLogout, btnAddPatient, btnviewPatient, btnSettings, btnMonitor, btnNotification;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    private int STORAGE_PERMISSION_CODE = 1;
    Handler handler = new Handler();

    SharedPreferences usersp;
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    final Loadingdialog loadingdialog = new Loadingdialog(AdminActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
            init();
            sendToken();
            builder1 = new AlertDialog.Builder(AdminActivity.this);
            initinternetdia();
            result = isOnline();

        if(ContextCompat.checkSelfPermission(AdminActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        }else{
            requestStoragePermission();
        }
           /* SharedPreferences sp = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = sp.getString("user","");
            txtDashboard.setText(username);*/
        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, Admin_AddPatientActivity.class));
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             logout();

            }
        });
        btnviewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminActivity.this, Admin_PatientViewSelectBarangay.class);
                intent.putExtra("from", "admin");
                //intent.putExtra("name", holder.name.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        btnMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, Admin_finalMonitoring.class));
                finish();
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminNotif.class));
                finish();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminSettings.class));
                finish();
            }
        });

    }
    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setTitle("Confirm Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(AdminActivity.this, Login.class));
                preferences.clearData(AdminActivity.this);
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                usersp = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = usersp.edit();
                editor.clear();
                editor.commit();
                logsadmin();
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
    }
    private void logsadmin(){
        Map<String, Object> map = new HashMap<>();
        map.put("date", datewithtime);
        map.put("action", "Logged out");
        FirebaseDatabase.getInstance().getReference().child("adminlogs").child(datetoday).push().setValue(map);
    }

    @Override

    public void onBackPressed() {
        finish();
    }
    public void init(){
        btnLogout = (CardView) findViewById(R.id.btnLogout);
        btnAddPatient = (CardView)findViewById(R.id.btnaddPatient);
        btnviewPatient = (CardView) findViewById(R.id.btnviewPatient);
        btnMonitor = (CardView) findViewById(R.id.btnMonitor);
        btnNotification = (CardView) findViewById(R.id.btnNotification);
        btnSettings = (CardView) findViewById(R.id.btnSettings);
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
                        map.put("atoken", token);
                        FirebaseDatabase.getInstance().getReference().child("login").child("mhoadmin")
                                .updateChildren(map);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("This permission is needed to export data to your Storage")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(AdminActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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