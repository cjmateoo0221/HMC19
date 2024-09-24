package com.example.dashboard1.Admin.AdminPatientMonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.dashboard1.Admin.ProgramViewHolder;
import com.example.dashboard1.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BarangayAdapter extends ArrayAdapter<String> {
    ArrayList<Integer> brgyimage = new ArrayList<Integer>();
    ArrayList<String> brgylist = new ArrayList<String>();
    ArrayList<String> pcount = new ArrayList<String>();
    Context context;

    public BarangayAdapter(@NonNull Context context, ArrayList<String> brgylist, ArrayList<Integer> brgyimage, ArrayList<String> pcount) {
        super(context, R.layout.monitoring_barangayitem, R.id.brgytxt, brgylist);
        this.context = context;
        this.brgyimage = brgyimage;
        this.pcount = pcount;
        this.brgylist = brgylist;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        BarangayViewHolder holder = null;
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.monitoring_barangayitem, parent, false);
            holder = new BarangayViewHolder(singleItem);
            singleItem.setTag(holder);
        }else{
            holder = (BarangayViewHolder) singleItem.getTag();
        }
        holder.itemImage.setImageResource(brgyimage.get(position));
        holder.programTitle.setText(brgylist.get(position));
        holder.count.setText(pcount.get(position));
        return singleItem;
    }

}
