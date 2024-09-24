package com.example.dashboard1.Admin.AdminPatientView;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewSelectBarangay.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Login;
import com.example.dashboard1.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.IOException;
import java.lang.reflect.Array;

public class Admin_PatientViewActivity extends AppCompatActivity {
    public static Context context;
    int delay = 2 * 1000;
    public static String barangay, act;
    AmazingSpinner as_sort;
    TextView brgytoptext, tv_select;
    AutoCompleteTextView actsort;
    boolean result;
    Handler handler = new Handler();
    Runnable runnable;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    AlertDialog.Builder builder1;
    ArrayAdapter<String> sortitems;
    AlertDialog alert11;
    Admin_PatientViewMainAdapter mainAdapter;
    private TabLayout tabLayout;
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_PatientViewActivity.this);
    private ViewPager viewPager;
    private TabItem ongoing, ended;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        getbarangayandstatus();
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        brgytoptext = findViewById(R.id.brgytoptext);
        context = getApplicationContext();
        brgytoptext.setText(barangay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tabLayout = findViewById(R.id.tabstatus);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new OngoingFragment(), "Active");
        vpAdapter.addFragment(new EndedFragment(), "Concluded");
        viewPager.setAdapter(vpAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent intent = new Intent (this, Admin_PatientViewSelectBarangay.class);
            intent.putExtra("from", act);
            startActivity(intent);
            finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void getbarangayandstatus(){
        Intent intent = getIntent();
        barangay = intent.getStringExtra("barangay");
        act = intent.getStringExtra("from");
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