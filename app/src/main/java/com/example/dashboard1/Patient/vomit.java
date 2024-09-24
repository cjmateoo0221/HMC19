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


public class vomit extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgvomit = rootview.findViewById(R.id.rgvomit);
        btnnext = rootview.findViewById(R.id.vomit_btnnext);
        btnback = rootview.findViewById(R.id.vomit_btnback);
        if(checklistClass.vomit.equals("Yes")){
            rgvomit.check(R.id.rbvomityes);
            checklistClass.vomitscore = 12.5;
        }else{
            rgvomit.check(R.id.rbvomitno);
            checklistClass.vomitscore = 0;
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ssvomit = rgvomit.getCheckedRadioButtonId();
                svomit = rootview.findViewById(ssvomit);
                checklistClass.vomit = svomit.getText().toString();
                if(svomit.getText().toString().equals("Yes")){
                    checklistClass.vomitscore = 12.5;
                }else{
                    checklistClass.vomitscore = 0;
                }
                Fragmentini.FragmentReplace(new temp(), getParentFragmentManager());
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
                Fragmentini.FragmentReplace(new sorethroat(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_vomit, container, false);
        return  rootview;
    }
}