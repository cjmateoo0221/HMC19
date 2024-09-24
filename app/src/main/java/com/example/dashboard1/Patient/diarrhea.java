package com.example.dashboard1.Patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dashboard1.R;


public class diarrhea extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgdiarrhea = rootview.findViewById(R.id.rgdiarrhea);
        btnnext = rootview.findViewById(R.id.diarrhea_btnnext);
        btnback = rootview.findViewById(R.id.diarrhea_btnback);
        if(checklistClass.diarrhea.equals("Yes")){
            rgdiarrhea.check(R.id.rbdiarrheayes);
            checklistClass.diarrheascore = 12.5;
        }else{
            rgdiarrhea.check(R.id.rbdiarrheano);
            checklistClass.diarrheascore = 0;
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ssdiarrhea = rgdiarrhea.getCheckedRadioButtonId();
                sdiarrhea = rootview.findViewById(ssdiarrhea);
                checklistClass.diarrhea = sdiarrhea.getText().toString();
                if(sdiarrhea.getText().toString().equals("Yes")){
                    checklistClass.diarrheascore = 12.5;
                }else{
                    checklistClass.diarrheascore = 0;
                }
                Fragmentini.FragmentReplace(new fatigue(), getParentFragmentManager());
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragmentini.FragmentReplace(new cough(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_diarrhea, container, false);
        return  rootview;
    }
}