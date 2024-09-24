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


public class shortness extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgshortness = rootview.findViewById(R.id.rgshortness);
        btnnext = rootview.findViewById(R.id.shortness_btnnext);
        btnback = rootview.findViewById(R.id.shortness_btnback);
        if(checklistClass.shortness.equals("Yes")){
            rgshortness.check(R.id.rbshortnessyes);
            checklistClass.shortscore = 12.5;

        }else{
            rgshortness.check(R.id.rbshortnessno);
            checklistClass.shortscore = 0;
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ssshortness = rgshortness.getCheckedRadioButtonId();
                sshortness = rootview.findViewById(ssshortness);
                checklistClass.shortness = sshortness.getText().toString();
                if(sshortness.getText().toString().equals("Yes")){
                    checklistClass.shortscore = 12.5;
                }else{
                    checklistClass.shortscore = 0;
                }
                Fragmentini.FragmentReplace(new sorethroat(), getParentFragmentManager());
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
                Fragmentini.FragmentReplace(new jointpain(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_shortness, container, false);
        return  rootview;
    }
}