package com.example.dashboard1.Patient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dashboard1.R;

public class Fragmentini {

    public static void FragmentADD(Fragment fragment, FragmentManager fm){
        fm.beginTransaction().add(R.id.frame_container, fragment).commit();
    }

    public static void FragmentReplace(Fragment fragment, FragmentManager fm){
        fm.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}
