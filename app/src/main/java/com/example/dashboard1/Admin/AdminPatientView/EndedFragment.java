package com.example.dashboard1.Admin.AdminPatientView;

import android.content.Context;
import android.os.Bundle;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.time.temporal.Temporal;

public class EndedFragment extends Fragment {
    RecyclerView recyclerView;
    Admin_conAdapter mainAdapter;
    public static Context context;
    public static TextView nodata;
    private View rootview;
    AmazingSpinner as_sort;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    ImageView img_clearsort;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        nodata = (TextView) rootview.findViewById(R.id.nodataed);
        img_clearsort = (ImageView) rootview.findViewById(R.id.img_clearsort);
        img_clearsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_sort.setText("A-Z",false);
                FirebaseRecyclerOptions<Admin_newDataModel> options =
                        new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay), Admin_newDataModel.class)
                                .build();
                mainAdapter = new Admin_conAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
                img_clearsort.setVisibility(View.INVISIBLE);
            }
        });
        as_sort = (AmazingSpinner) rootview.findViewById(R.id.as_sort);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sort_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        as_sort.setAdapter(adapter);
       recyclerView = (RecyclerView) rootview.findViewById(R.id.rv_patientData1);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<Admin_newDataModel> options =
                new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay), Admin_newDataModel.class)
                        .build();
       mainAdapter = new Admin_conAdapter(options);
       recyclerView.setAdapter(mainAdapter);
        as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String letter = adapterView.getItemAtPosition(i).toString();

                img_clearsort.setVisibility(View.VISIBLE);
                FirebaseRecyclerOptions<Admin_newDataModel> options =
                        new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay).orderByChild("firstname").startAt(letter).endAt(letter+ "\uf8ff"), Admin_newDataModel.class)
                                .build();
                mainAdapter = new Admin_conAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootview = inflater.inflate(R.layout.fragment_ended, container, false);
        nodata = rootview.findViewById(R.id.nodataed);
        return rootview;
    }


    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
        mainAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search by Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void txtSearch (String str){
        FirebaseRecyclerOptions<Admin_newDataModel> options =
                new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientdismissedbrgy").child(barangay).orderByChild("firstname").startAt(str).endAt(str+ "\uf8ff"), Admin_newDataModel.class)
                        .build();
        mainAdapter = new Admin_conAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

}