package com.example.dashboard1.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dashboard1.R;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String [] title;
    public ProgramAdapter(@NonNull Context context, String[] title, int[] images) {
        super(context, R.layout.tools_listitem, R.id.menutext, title);
        this.context = context;
        this.images = images;
        this.title = title;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder = null;
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.tools_listitem, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }else{
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.itemImage.setImageResource(images[position]);
        holder.programTitle.setText(title[position]);
        return singleItem;
    }
}
