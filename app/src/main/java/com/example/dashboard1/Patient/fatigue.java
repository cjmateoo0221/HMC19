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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fatigue#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fatigue extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgfatigue = rootview.findViewById(R.id.rgfatigue);
        btnnext = rootview.findViewById(R.id.fatigue_btnnext);
        btnback = rootview.findViewById(R.id.fatigue_btnback);
        if(checklistClass.fatigue.equals("Yes")){
            rgfatigue.check(R.id.rbfatigueyes);
            checklistClass.fatiguescore = 12.5;
        }else{
            rgfatigue.check(R.id.rbfatigueno);
            checklistClass.fatiguescore = 0;
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ssfatigue = rgfatigue.getCheckedRadioButtonId();
                sfatigue = rootview.findViewById(ssfatigue);
                checklistClass.fatigue = sfatigue.getText().toString();
                if(sfatigue.getText().toString().equals("Yes")){
                    checklistClass.fatiguescore = 12.5;
                }else{
                    checklistClass.fatiguescore = 0;
                }
                Fragmentini.FragmentReplace(new headache(), getParentFragmentManager());
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
                Fragmentini.FragmentReplace(new diarrhea(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_fatigue, container, false);
        return  rootview;
    }
}