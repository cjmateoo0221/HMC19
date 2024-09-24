package com.example.dashboard1.Admin.AdminPatientView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.barangay;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.rosemaryapp.amazingspinner.AmazingSpinner;


public class OngoingFragment extends Fragment {
    public static Context context;
    RecyclerView recyclerView;
    Admin_newAdapter mainAdapter;

    public static TextView nodata;
    AmazingSpinner as_sort;
    ImageView img_clearsort;
    String[] sort_data = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    public static int index = -1;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    private View rootview;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        img_clearsort = (ImageView) rootview.findViewById(R.id.img_clearsort);
        img_clearsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                as_sort.setText("A-Z",false);
                FirebaseRecyclerOptions<Admin_newDataModel> options =
                        new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), Admin_newDataModel.class)
                                .build();
                mainAdapter = new Admin_newAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
                img_clearsort.setVisibility(View.INVISIBLE);
            }
        });
        as_sort = (AmazingSpinner) rootview.findViewById(R.id.as_sort);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sort_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        as_sort.setAdapter(adapter);
        recyclerView= (RecyclerView) rootview.findViewById(R.id.rv_patientData);
        recyclerView.setHasFixedSize(true);
        nodata =(TextView) rootview.findViewById(R.id.nodataog);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        as_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String letter = adapterView.getItemAtPosition(i).toString();

                    img_clearsort.setVisibility(View.VISIBLE);
                FirebaseRecyclerOptions<Admin_newDataModel> options =
                        new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(letter).endAt(letter+ "\uf8ff"), Admin_newDataModel.class)
                                .build();
                mainAdapter = new Admin_newAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
            }
        });

               /* String letter = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(context, letter, Toast.LENGTH_SHORT).show();
                FirebaseRecyclerOptions<Admin_newDataModel> options =
                        new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(letter), Admin_newDataModel.class)
                                .build();
                mainAdapter = new Admin_newAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);*/

        FirebaseRecyclerOptions<Admin_newDataModel> options =
                new FirebaseRecyclerOptions.Builder<Admin_newDataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay), Admin_newDataModel.class)
                        .build();
        mainAdapter = new Admin_newAdapter(options);
        recyclerView.setAdapter(mainAdapter);
   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mainAdapter.getItemCount() ==0){
                    nodata.setVisibility(View.VISIBLE);
                }else{
                    nodata.setVisibility(View.INVISIBLE);
                }

            }
        }, 500);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_ongoing, container, false);
        setHasOptionsMenu(true);
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
        if (mainAdapter != null){
            mainAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search by Firstname");

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay).orderByChild("firstname").startAt(str).endAt(str+ "\uf8ff"), Admin_newDataModel.class)
                        .build();
        mainAdapter = new Admin_newAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 50);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBundleRecyclerViewState = new Bundle();
        mListState = null;
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }
}