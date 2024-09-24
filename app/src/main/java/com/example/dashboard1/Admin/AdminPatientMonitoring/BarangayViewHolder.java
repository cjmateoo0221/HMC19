package com.example.dashboard1.Admin.AdminPatientMonitoring;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dashboard1.R;

public class BarangayViewHolder {
    ImageView itemImage;
    TextView programTitle;
    TextView count;
    BarangayViewHolder(View v){
        itemImage = v.findViewById(R.id.brgyicon);
        programTitle = v.findViewById(R.id.brgytxt);
        count = v.findViewById(R.id.tv_count);
    }
}
