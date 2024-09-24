package com.example.dashboard1.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dashboard1.Admin.AdminPatientView.EndedFragment;
import com.example.dashboard1.Admin.AdminPatientView.OngoingFragment;
import com.example.dashboard1.Admin.AdminPatientView.VPAdapter;
import com.example.dashboard1.DataPoint;
import com.example.dashboard1.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartExample extends AppCompatActivity {
    TextView yeartoday;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    BarChart barChart;
    ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_example);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        yeartoday = findViewById(R.id.tv_year);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        yeartoday.setText(String.valueOf(year));
        barChart = findViewById(R.id.barChart1);
        retrieveData();
        String[] xAxisLables = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(12);
        YAxis yAxis = barChart.getAxisRight();
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true);
        yAxis.setEnabled(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(12f);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        barChart.setFitBars(true);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.setDragEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setScaleEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);
    }

    private void retrieveData() {
        FirebaseDatabase.getInstance().getReference().child("ChartValues")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<BarEntry> dataVals = new ArrayList<>();
                        ArrayList<BarEntry> dataVals1 = new ArrayList<>();
                        for(DataSnapshot myDataSnapshot: snapshot.child("des").getChildren()){
                            GetDate gd = myDataSnapshot.getValue(GetDate.class);
                            dataVals.add(new BarEntry(gd.getDate(), gd.getCount()));
                        }
                        for(DataSnapshot myDataSnapshot: snapshot.child("con").getChildren()){
                            GetDate gd = myDataSnapshot.getValue(GetDate.class);
                            dataVals1.add(new BarEntry(gd.getDate(), gd.getCount()));
                        }
                        showChart(dataVals, dataVals1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showChart(ArrayList<BarEntry> dataVals, ArrayList<BarEntry> dataVals1 ) {
        BarDataSet barDataSet = new BarDataSet(dataVals, "Active Patients");
        BarDataSet barDataSet1 = new BarDataSet(dataVals1, "Concluded Patients");
        barDataSet.setColor(Color.MAGENTA);
        barDataSet1.setColor(Color.CYAN);
        iBarDataSets.clear();
        iBarDataSets.add(barDataSet);
        iBarDataSets.add(barDataSet1);
        BarData barData = new BarData(iBarDataSets);
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        };
        barData.setValueFormatter(vf);
        barData.setValueTextSize(13f);
        barData.setValueTextColor(Color.BLACK);
        float barSpace = 0.2f;
        float groupSpace = 0.3f;
        barData.setBarWidth(0.15f);
        barChart.clear();
        barChart.setData(barData);
        barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 12);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
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

}
