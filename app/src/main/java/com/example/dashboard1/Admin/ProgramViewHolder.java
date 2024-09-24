package com.example.dashboard1.Admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dashboard1.R;

import org.w3c.dom.Text;

public class ProgramViewHolder {
    ImageView itemImage;
    TextView programTitle;
    ProgramViewHolder(View v){
        itemImage = v.findViewById(R.id.iconn);
        programTitle = v.findViewById(R.id.menutext);
    }
}
