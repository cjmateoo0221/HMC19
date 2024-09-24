package com.example.dashboard1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Patient.PatientActivity;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

import java.io.IOException;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragment;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends TutorialActivity {
    private SharedPreference sharedPreferenceObj;
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder1 = new AlertDialog.Builder(this);
        //initinternetdia();
       // result = isOnline();
        sharedPreferenceObj= new SharedPreference(IntroActivity.this);
        if(sharedPreferenceObj.getApp_runFirst().equals("FIRST"))
        {
            sharedPreferenceObj.setApp_runFirst("NO");
        }else{
            startActivity(new Intent(IntroActivity.this, PatientActivity.class));
        }
        addFragment(new Step.Builder().setTitle("HMC19")
                .setContent("A monitoring application by the Municipal Health Office of GMA Cavite")
                .setBackgroundColor(Color.parseColor("#0099ff")) // int background color
                .setDrawable(R.drawable.logo) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Answer the checklist everyday")
                .setContent("Log your signs and symptoms everyday using the application. Click on the choices to answer the corresponding signs and symptoms criteria. ")
                .setBackgroundColor(Color.parseColor("#0099ff")) // int background color
                .setDrawable(R.drawable.checklist) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Notification at 8 AM")
                .setContent("This app will notify you to answer your checklist everyday at 8 AM")
                .setBackgroundColor(Color.parseColor("#0099ff")) // int background color
                .setDrawable(R.drawable.notif) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Set your date and time to Automatic")
                .setContent("For best experience, as this app heavily relies on this data. For accurate and realtime monitoring. ")
                .setBackgroundColor(Color.parseColor("#0099ff")) // int background color
                .setDrawable(R.drawable.dateintro) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("You're all set!")
                .setContent("Thank you for using this app.")
                .setBackgroundColor(Color.parseColor("#0099ff")) // int background color
                .setDrawable(R.drawable.check) // int top drawable
                .build());

        setCancelText("Skip");
        setPrevText("Back");

    }
    @Override
    public void finishTutorial() {
        startActivity(new Intent(IntroActivity.this, PatientActivity.class));
    }

    @Override
    public void currentFragmentPosition(int position) {

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    public void setCancelText(String text) {
        super.setCancelText(text);
    }

    @Override
    public void setPrevText(String text) {
        super.setPrevText(text);
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
