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


public class sorethroat extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgsorethroat = rootview.findViewById(R.id.rgsorethroat);
        btnnext = rootview.findViewById(R.id.sorethroat_btnnext);
        btnback = rootview.findViewById(R.id.sorethroat_btnback);
        if(checklistClass.sorethroat.equals("Yes")){
            rgsorethroat.check(R.id.rbsorethroatyes);
            checklistClass.sorescore = 12.5;
        }else{
            rgsorethroat.check(R.id.rbsorethroatno);
            checklistClass.sorescore = 0;
        };
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sssorethroat = rgsorethroat.getCheckedRadioButtonId();
                ssorethroat = rootview.findViewById(sssorethroat);
                checklistClass.sorethroat = ssorethroat.getText().toString();
                if(ssorethroat.getText().toString().equals("Yes")){
                    checklistClass.sorescore = 12.5;
                }else{
                    checklistClass.sorescore = 0;
                }
                Fragmentini.FragmentReplace(new vomit(), getParentFragmentManager());
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
                Fragmentini.FragmentReplace(new shortness(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_sorethroat, container, false);
        return  rootview;
    }
}