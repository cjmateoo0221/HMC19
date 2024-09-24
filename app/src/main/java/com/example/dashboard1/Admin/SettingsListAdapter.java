package com.example.dashboard1.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dashboard1.R;

import java.util.ArrayList;

public class SettingsListAdapter extends ArrayAdapter<Menu> {

    public SettingsListAdapter(Context context, ArrayList<Menu> menuArrayList){
        super(context, R.layout.tools_listitem, menuArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Menu menu = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tools_listitem, parent, false);
        }

        ImageView imageview = convertView.findViewById(R.id.iconn);
        TextView menutext = convertView.findViewById(R.id.menutext);

        imageview.setImageResource(menu.iconId);
        menutext.setText(menu.menuname);
        return super.getView(position, convertView, parent);
    }
}
