package com.example.dashboard1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

public class Loadingdialog1 {
    private AlertDialog dialog;
    private Context context;

    public Loadingdialog1(Context context) {
        this.context = context;
    }


    public void showLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
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
