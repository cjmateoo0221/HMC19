package com.example.dashboard1.Patient.patientedit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.Login;
import com.example.dashboard1.Patient.PatientActivity;
import com.example.dashboard1.Patient.patientdetails.PatientPersonalDetailsActivity;
import com.example.dashboard1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PatientEditDetails extends AppCompatActivity {
    EditText firstname, lastname, middlename, age, contact, email, address, occu, locofwork;
    RadioGroup sex;
    SharedPreferences sp;
    TextView barangay, password, contactnum;
    Button birhdate;
    DatePickerDialog birthday;
    Calendar calendar = Calendar.getInstance();
    int yeartoday = calendar.get(Calendar.YEAR);
    Handler handler = new Handler();
    Runnable runnable;
    androidx.appcompat.app.AlertDialog.Builder builder1;
    androidx.appcompat.app.AlertDialog alert11;
    boolean result;
    int delay = 2 * 1000;
    FloatingActionButton fab;
    String username;
    final Loadingdialog loadingdialog = new Loadingdialog(PatientEditDetails.this);
    RadioButton  selected;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_details);
        builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
       // initinternetdia();
       // result = isOnline();
        fab = findViewById(R.id.eifabconfirm);
        sp = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sp.getString("user","");
        firstname = findViewById(R.id.ei_firstname);
        contactnum = findViewById(R.id.tv_contactnum);
        barangay = findViewById(R.id.tv_barangay);
        password = findViewById(R.id.tv_password);
        lastname = findViewById(R.id.ei_lastname);
        middlename = findViewById(R.id.ei_middlename);
        age = findViewById(R.id.ei_age);
        contact = findViewById(R.id.ei_contact);
        email = findViewById(R.id.ei_email);
        address = findViewById(R.id.ei_address);
        occu = findViewById(R.id.ei_occu);
        locofwork = findViewById(R.id.ei_loc);
        sex = findViewById(R.id.eirg_sex);
        birhdate = findViewById(R.id.ei_bdaypick);
        getBarangay();
        fetchData();
        initDatePicker();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });

    }
    private void updateDetails(){
        String sexf;
        int selectedSex = sex.getCheckedRadioButtonId();
        selected = findViewById(selectedSex);
        sexf = selected.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("firstname",firstname.getText().toString());
        map.put("lastname",lastname.getText().toString());
        map.put("middlename",middlename.getText().toString());
        map.put("age", age.getText().toString());
        map.put("contactnum", "09" + contact.getText().toString());
        map.put("email", email.getText().toString());
        map.put("address", address.getText().toString());
        map.put("occupation", occu.getText().toString());
        map.put("locofwork", locofwork.getText().toString());
        map.put("birthday", birhdate.getText().toString());
        map.put("sex", sexf);

        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("action", "Edited Information");
                        map1.put("date",datewithtime);
                        FirebaseDatabase.getInstance().getReference().child("patientlogs").child(username).child(datetoday)
                                .push().setValue(map1);
                        FirebaseDatabase.getInstance().getReference().child("patientongoingall").child(username).updateChildren(map);
                        FirebaseDatabase.getInstance().getReference().child("patientongoing").child(barangay.getText().toString()).child(username).updateChildren(map);
                        Toast.makeText(PatientEditDetails.this, "Succesfully Updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PatientEditDetails.this, PatientPersonalDetailsActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientEditDetails.this, "Error in Updating", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void validations(){
        if(firstname.getText().toString().length() == 0){
            firstname.setError("Firstname is Required");
            Toast.makeText(this, "Firstname is required", Toast.LENGTH_SHORT).show();
        }else if(middlename.getText().toString().length() == 0){
            middlename.setError("Middlename is Required");
            Toast.makeText(this, "Middlename is required", Toast.LENGTH_SHORT).show();
        }else if(lastname.getText().toString().length() == 0){
            lastname.setError("Lastname is Required");
            Toast.makeText(this, "Lastname is required", Toast.LENGTH_SHORT).show();
        }
        else if(age.getText().toString().length() == 0){
            age.setError("Age is Required");
            Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
        }else if (address.getText().toString().length() ==0){
            address.setError("Complete Address is required");
            Toast.makeText(this, "Admin is required", Toast.LENGTH_SHORT).show();

        }else if(contact.getText().toString().length() == 0){
            contact.setError("Contact Number is Required");
            Toast.makeText(this, "Contact Number is required", Toast.LENGTH_SHORT).show();
        }else if(contact.getText().toString().length() < 11){
            contact.setError("Invalid contact number");
            Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show();
        }else if(occu.getText().toString().length() == 0){
            occu.setError("Occupation is required");
            Toast.makeText(this, "Occupation is required", Toast.LENGTH_SHORT).show();
        }else if(locofwork.getText().toString().length() == 0){
            locofwork.setError("Location of Work is required");
            Toast.makeText(this, "Location of work is required", Toast.LENGTH_SHORT).show();
        }else if (email.getText().toString().length() == 0){
            email.setError("Email is required");
            Toast.makeText(this, "Email is Required", Toast.LENGTH_SHORT).show();
        }else if (isValidEmail(email.getText().toString()) == false){
            email.setError("Please enter a valid email");
        }else{
            updateDetails();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void fetchData(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        password.setText(snapshot.child("pass").getValue().toString());
                        firstname.setText(snapshot.child("firstname").getValue().toString());
                        middlename.setText(snapshot.child("middlename").getValue().toString());
                        lastname.setText(snapshot.child("lastname").getValue().toString());
                        contact.setText("09"+snapshot.child("contactnum").getValue().toString());
                        address.setText(snapshot.child("address").getValue().toString());
                        birhdate.setText(snapshot.child("birthday").getValue().toString());
                        locofwork.setText(snapshot.child("locofwork").getValue().toString());
                        occu.setText(snapshot.child("occupation").getValue().toString());
                        email.setText(snapshot.child("email").getValue().toString());
                        age.setText(snapshot.child("age").getValue().toString());
                        String sex1 = snapshot.child("sex").getValue().toString();
                        if(sex1.equals("Male")){
                            sex.check(R.id.eirbmale);
                        }else{
                            sex.check(R.id.eirbfemale);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int getage;
                getage = yeartoday - year;
                month = month + 1;
                String date = makeDateString(month,day,year);
                birhdate.setText(date);
                age.setText(String.valueOf(getage));
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        birthday = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        birthday.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int month, int day, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JULY";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void eiopenDatePicker(View view) {
        birthday.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PatientEditDetails.this, PatientPersonalDetailsActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void getBarangay(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        barangay.setText(snapshot.child("barangay").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    protected void onResume() {
        //start handler as activity become visible

        /*handler.postDelayed( runnable = new Runnable() {
            public void run() {
                result = isOnline();
                if(result == false){
                    alert11.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }else{
                    alert11.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                handler.postDelayed(runnable, delay);
            }
        }, delay);*/

        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
    private void initinternetdia(){
        builder1.setTitle("Could not connect to the server");
        builder1.setMessage("Please check your internet connection and try again.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert11 = builder1.create();
    }
}
