package com.example.dashboard1;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.dashboard1.R;

public class Loadingdialog {
    private AlertDialog dialog;
    private Activity activity;

    public Loadingdialog(Activity activity) {
        this.activity = activity;
    }


    public void showLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(400,300);
    }

    public void disMiss(){
        dialog.dismiss();
    }

}
