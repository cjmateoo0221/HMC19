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


public class jointpain extends Fragment {

    private View rootview;
    ImageButton btnnext, btnback;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgjointpain = rootview.findViewById(R.id.rgjointpain);
        btnnext = rootview.findViewById(R.id.jointpain_btnnext);
        btnback = rootview.findViewById(R.id.jointpain_btnback);
        if(checklistClass.jointpain.equals("Yes")){
            rgjointpain.check(R.id.rbjointpainyes);
            checklistClass.jointpainscore = 12.5;
        }else{
            rgjointpain.check(R.id.rbjointpainno);
            checklistClass.jointpainscore = 0;
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ssjointpain = rgjointpain.getCheckedRadioButtonId();
                sjointpain = rootview.findViewById(ssjointpain);
                checklistClass.jointpain = sjointpain.getText().toString();
                if(sjointpain.getText().toString().equals("Yes")){
                    checklistClass.jointpainscore = 12.5;
                }else{
                    checklistClass.jointpainscore = 0;
                }
                Fragmentini.FragmentReplace(new shortness(), getParentFragmentManager());
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
                Fragmentini.FragmentReplace(new headache(), getParentFragmentManager());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_jointpain, container, false);
        return  rootview;
    }
}