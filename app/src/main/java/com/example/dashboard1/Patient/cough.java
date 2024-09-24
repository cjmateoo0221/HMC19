package com.example.dashboard1.Patient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class cough extends Fragment {

    private View rootview;
    ImageButton btnnext, btnskip;
    String cough, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, vomit;
    RadioButton scough, sdiarrhea, sfatigue, sheadache, sjointpain, sshortness, ssorethroat, svomit;
    RadioGroup rgcough, rgdiarrhea, rgfatigue, rgheadache, rgjointpain, rgshortness, rgsorethroat , rgvomit;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rgcough = rootview.findViewById(R.id.rgcough);
        btnnext = rootview.findViewById(R.id.cough_btnnext);
        btnskip = rootview.findViewById(R.id.cough_btnskip);
        if(checklistClass.cough.equals("Yes")){
            rgcough.check(R.id.rbcoughyes);
            checklistClass.coughscore = 12.5;
        }else{
            rgcough.check(R.id.rbcoughno);
            checklistClass.coughscore = 0;
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sscough = rgcough.getCheckedRadioButtonId();
                scough = rootview.findViewById(sscough);
                checklistClass.cough = scough.getText().toString();
                if(scough.getText().toString().equals("Yes")){
                    checklistClass.coughscore = 12.5;
                }else{
                    checklistClass.coughscore = 0;
                }
                Fragmentini.FragmentReplace(new diarrhea(), getParentFragmentManager());
            }
        });

        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to Quit?");
                builder.setMessage("You will have to answer the checklist again from the start");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), PatientActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cough, container, false);
        return rootview;
    }
}