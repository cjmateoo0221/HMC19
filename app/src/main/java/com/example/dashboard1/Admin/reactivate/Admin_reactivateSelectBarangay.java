package com.example.dashboard1.Admin.reactivate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.BarangayAdapter;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.R;

public class Admin_reactivateSelectBarangay extends AppCompatActivity {
    ListView listView;
    String[] barangay = {"Aldiano Olaes", "Benjamin Tirona", "Bernardo Pulido", "Col JP Elises", "Epifano Malia", "F De Castro", "F Reyes", "Fiorello Calimag", "Gavino Maderan", "Gregoria De Jesus", "Inocencio Salud","Jacinto Lumbreras", "Kapitan Kua", "Macario Dacon", "Marcelino Memije", "Nicolasa Virata", "Pantaleon Granados", "Poblacion 1", "Poblacion 2", "Poblacion 3" , "Poblacion 4", "Poblacion 5", "Ramon Cruz", "San Gabriel", "San Jose", "Severino Delas Alas", "Tinente Tiago"};
    int[] image = {R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon,R.drawable.brgyicon};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reactivate_select_barangay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = findViewById(R.id.lv_barangay);
       // BarangayAdapter barangayAdapter = new BarangayAdapter(this, barangay, image);
      //  listView.setAdapter(barangayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Aldiano Olaes");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                } else if (i == 1){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Benjamin Tirona");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 2){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Bernardo Pulido");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 3){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Col JP Elises");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);

                }else if (i == 4){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Epifano Malia");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
                else if (i == 5){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "F De Castro");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
                else if (i == 6){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "F Reyes");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 7){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Fiorello Calimag");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 8){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Gavino Maderan");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 9){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Gregoria De Jesus");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 10){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Inocencio Salud");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 11) {
                    Intent intent = new Intent(Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Jacinto Lumbreras");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 12){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Kapitan Kua");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 13){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Macario Dacon");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 14){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Marcelino Memije");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 15){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Nicolasa Virata");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 16){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Panteleon Granados");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 17){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Poblacion 1");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 18){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Poblacion 2");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 19){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Poblacion 3");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 20){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Poblacion 4");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 21){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Poblacion 5");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 22){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Ramon Cruz");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 23){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "San Gabriel");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 24){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "San Jose");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 25){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Severino Delas Alas");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }else if (i == 26){
                    Intent intent = new Intent (Admin_reactivateSelectBarangay.this, Admin_reactivatepAccount.class);
                    intent.putExtra("barangay", "Tinente Tiago");
                    //intent.putExtra("name", holder.name.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AdminSettings.class));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}