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


public class headache extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgheadache = rootview.findViewById(R.id.rgheadache);
        btnnext = rootview.findViewById(R.id.headache_btnnext);
        btnback = rootview.findViewById(R.id.headache_btnback);
        if(checklistClass.headache.equals("Yes")){
            rgheadache.check(R.id.rbheadacheyes);
            checklistClass.headachescore = 12.5;
        }else{
            rgheadache.check(R.id.rbheadacheno);
            checklistClass.headachescore = 0;
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ssheadache = rgheadache.getCheckedRadioButtonId();
                sheadache = rootview.findViewById(ssheadache);
                checklistClass.headache = sheadache.getText().toString();
                if(sheadache.getText().toString().equals("Yes")){
                    checklistClass.headachescore = 12.5;
                }else{
                    checklistClass.headachescore = 0;
                }
                Fragmentini.FragmentReplace(new jointpain(), getParentFragmentManager());
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checklistClass.score <= 0){
                    checklistClass.score = 0;
                }else{
                    checklistClass.score = checklistClass.score - 12.5;
                }
                Fragmentini.FragmentReplace(new fatigue(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_headache, container, false);
        return  rootview;
    }
}