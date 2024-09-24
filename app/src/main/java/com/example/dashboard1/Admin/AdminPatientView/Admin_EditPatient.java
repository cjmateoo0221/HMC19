package com.example.dashboard1.Admin.AdminPatientView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.Admin_AddPatientActivity;
import com.example.dashboard1.Patient.patientedit.PatientEditDetails;
import com.example.dashboard1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Admin_EditPatient extends AppCompatActivity {
    int delay = 2 * 1000;
    boolean result;
    Handler handler = new Handler();
    Calendar calendar = Calendar.getInstance();
    int yeartoday = calendar.get(Calendar.YEAR);
    Runnable runnable;
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    String patientnum;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference barangaydb = database.getReference("barangay");
    Button btnConfirm;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    FloatingActionButton epfabconfirm;
    Button dateButton;
    Button swabButton;
    String barangayintent, act, pnum;
    TextInputLayout selectpwd;
    String[] status = {"Vaccinated", "Not Vaccinated"};
    String[] barangay = {"Aldiano Olaes", "Benjamin Tirona", "Bernardo Pulido", "Col JP Elises", "Epifano Malia", "F De Castro", "F Reyes", "Fiorello Calimag", "Gavino Maderan", "Gregoria De Jesus", "Inocencio Salud", "Kapitan Kua", "Macario Dacon", "Marcelino Memije", "Nicolasa Virata", "Pantaleon Granados", "Poblacion 1", "Poblacion 2", "Poblacion 3" , "Poblacion 4", "Poblacion 5", "Ramon Cruz", "San Gabriel", "San Jose", "Severino Delas Alas", "Tinente Tiago"};
    String[] disability = {"Psychosocial", "Chronic Illness", "Learning", "Mental", "Visual", "Orthopedic", "Communication"};
    String slstatus;
    String slbarangay;
    String sldisablity;
    TextView noofinfect, prevBarangay, currentStatus, puser, statustv, patientnumtv, refnum, patienttoken, pass, dateadded, dateended;
    AutoCompleteTextView epvac, autoCompleteTextView1, actBrgy;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems1;
    ArrayAdapter<String> adapterItems2;
    RadioGroup epsex, rgpwdgrp, rgseniorgrp;
    private DatePickerDialog datePickerDialog, datePickerDialog2;
    EditText epfirstname, eplastname, epmiddlename, epage, epemail, epaddress, epoccu, eploc, ephouse, ephealth, epvacc, epclcont, epcontact;
    RadioButton rbsex, rgselectedDis, rgselectedSenior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_patient);
        init();
        getbarangayandstatus();
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        prevBarangay = findViewById(R.id.prevBarangay);
        noofinfect = findViewById(R.id.noofinfecttv);
        currentStatus = findViewById(R.id.currentStatus);
        puser = findViewById(R.id.puser);
        statustv = findViewById(R.id.statustv);
        pass = findViewById(R.id.pass);
        dateadded = findViewById(R.id.dateadded);
        dateended = findViewById(R.id.dateended);
        patientnumtv = findViewById(R.id.patientnumtv);
        refnum = findViewById(R.id.refnum);
        patienttoken = findViewById(R.id.patienttoken);
        epfabconfirm = findViewById(R.id.epfabconfirm);
        getpnum();
        dateButton = findViewById(R.id.epbdaypick);
        swabButton = findViewById(R.id.epswabpicker);
        epvac = findViewById(R.id.epvac);
        autoCompleteTextView1 = findViewById(R.id.auto_complete_txt1);
        actBrgy = findViewById(R.id.actBrgy);
        adapterItems = new ArrayAdapter<String>(this,R.layout.vac_stat,status);
        adapterItems1 = new ArrayAdapter<String>(this,R.layout.disability,disability);
        adapterItems2 = new ArrayAdapter<String>(this,R.layout.barangay,barangay);
        autoCompleteTextView1.setAdapter(adapterItems1);
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                sldisablity = parent.getItemAtPosition(i).toString();
            }
        });
        rgpwdgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rbpwdYes){
                    selectpwd.setEnabled(true);
                    adapterItems1 = new ArrayAdapter<String>(Admin_EditPatient.this,R.layout.disability,disability);
                    autoCompleteTextView1.setText("Psychosocial",false);
                    autoCompleteTextView1.setAdapter(adapterItems1);
                }else if(i == R.id.rbpwdNo){
                    selectpwd.setEnabled(false);
                    autoCompleteTextView1.setText("N/A");
                }
            }
        });
        epvac.setAdapter(adapterItems);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        epvac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                slstatus = parent.getItemAtPosition(i).toString();
            }
        });
        actBrgy.setAdapter(adapterItems2);
        actBrgy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                slbarangay = parent.getItemAtPosition(i).toString();
            }
        });
        fetchData();
        initDatePicker();
        initDatePicker2();
        epfabconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });
    }

    public void init(){
        epage = findViewById(R.id.epage);
        epfirstname = findViewById(R.id.epfirstname);
        eplastname = findViewById(R.id.eplastname);
        epmiddlename = findViewById(R.id.epmiddlname);
        epemail = findViewById(R.id.epemail);
        epaddress = findViewById(R.id.epaddress);
        epoccu = findViewById(R.id.epoccu);
        eploc    = findViewById(R.id.eploc);
        ephouse = findViewById(R.id.ephouse);
        ephealth = findViewById(R.id.ephealth);
        epclcont = findViewById(R.id.epclcont);
        epcontact = findViewById(R.id.epcontact);
        btnConfirm = findViewById(R.id.btnConfirm);
        epsex = findViewById(R.id.epsex);
        selectpwd = findViewById(R.id.selectpwd);
        rgpwdgrp = findViewById(R.id.rg_pwd);
        rgseniorgrp = findViewById(R.id.rg_senior);
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
    public void openDatePicker2(View view) {
        datePickerDialog2.show();
    }
    public void getpnum(){
        Intent intent = getIntent();
        patientnum = intent.getStringExtra("patientnum");
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int age;
                age = yeartoday - year;
                month = month + 1;
                String date = makeDateString(month,day,year);
                dateButton.setText(date);
                epage.setText(String.valueOf(age));
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private void initDatePicker2(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(month,day,year);
                swabButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
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
    private void fetchData(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(patientnum)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        epfirstname.setText(snapshot.child("firstname").getValue().toString());
                        epmiddlename.setText(snapshot.child("middlename").getValue().toString());
                        eplastname.setText(snapshot.child("lastname").getValue().toString());
                        epage.setText(snapshot.child("age").getValue().toString());
                        String rsex = snapshot.child("sex").getValue().toString();
                        String rpwd = snapshot.child("pwd").getValue().toString();
                        String rsenior = snapshot.child("senior").getValue().toString();
                        if (rsenior.equals("Yes")) {
                            rgseniorgrp.check(R.id.rbseniorYes);
                        }else{
                            rgseniorgrp.check(R.id.rbseniorNo);
                        }
                        autoCompleteTextView1.setText(snapshot.child("disability").getValue().toString(), false);
                        if(rpwd.equals("Yes")){
                            rgpwdgrp.check(R.id.rbpwdYes);
                        }else{
                            rgpwdgrp.check(R.id.rbpwdNo);
                        }
                        if(rsex.equals("Male")){
                            epsex.check(R.id.epmale);
                        }else{
                            epsex.check(R.id.epfemale);
                        }
                        dateButton.setText(snapshot.child("birthday").getValue().toString());
                        epcontact.setText(snapshot.child("contactnum").getValue().toString());
                        epemail.setText(snapshot.child("email").getValue().toString());
                        epaddress.setText(snapshot.child("address").getValue().toString());
                        epoccu.setText(snapshot.child("occupation").getValue().toString());
                        eploc.setText(snapshot.child("locofwork").getValue().toString());
                        ephouse.setText(snapshot.child("household").getValue().toString());
                        ephealth.setText(snapshot.child("healthcond").getValue().toString());
                        swabButton.setText(snapshot.child("swab").getValue().toString());
                        epclcont.setText(snapshot.child("closecontact").getValue().toString());
                        epvac.setText(snapshot.child("vaccinestat").getValue().toString(), false);
                        actBrgy.setText(snapshot.child("barangay").getValue().toString(), false);
                        prevBarangay.setText(snapshot.child("barangay").getValue().toString());
                        currentStatus.setText(snapshot.child("currentStatus").getValue().toString());
                        puser.setText(snapshot.child("user").getValue().toString());
                        statustv.setText(snapshot.child("status").getValue().toString());
                        pass.setText(snapshot.child("pass").getValue().toString());
                        dateadded.setText(snapshot.child("dateadded").getValue().toString());
                        dateended .setText(snapshot.child("dateended").getValue().toString());
                        patientnumtv.setText(snapshot.child("patientnum").getValue().toString());
                        noofinfect.setText(snapshot.child("noofinfect").getValue().toString());
                        refnum.setText(snapshot.child("refnum").getValue().toString());
                        patienttoken.setText(snapshot.child("patienttoken").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (Admin_EditPatient.this, Admin_nvPatient.class);
        intent.putExtra("barangay", barangayintent);
        intent.putExtra("patientnum", patientnum);
        intent.putExtra("from", act);
        startActivity(intent);
        finish();
        //intent.putExtra("name", holder.name.getText().toString());holder.patientnum.getContext().startActivity(intent);

    }
    public void updateData(){
        String sexf, pwd, senior;
        String disabilityf, mname;
        String household, healthcondition, closecontact;
        int selectedSenior = rgseniorgrp.getCheckedRadioButtonId();
        rgselectedSenior = findViewById(selectedSenior);
        senior = rgselectedSenior.getText().toString();
        int selectedPWD = rgpwdgrp.getCheckedRadioButtonId();
        rgselectedDis = findViewById(selectedPWD);
        pwd = rgselectedDis.getText().toString();
        int ssex = epsex.getCheckedRadioButtonId();
        rbsex = findViewById(ssex);
        sexf = rbsex.getText().toString();
        if(autoCompleteTextView1.getText().toString().length() == 0){
            disabilityf = "N/A";
        }else{
            disabilityf = autoCompleteTextView1.getText().toString();
        }

        if(epmiddlename.getText().toString().length() == 0){
            mname = "N/A";
        }else{
            mname = epmiddlename.getText().toString();
        }
        if(ephealth.getText().toString().length() == 0){
            healthcondition = "None";
        }else{
            healthcondition = ephealth.getText().toString();
        }

        if(epclcont.getText().toString().length() == 0){
            closecontact = "None";
        }else{
            closecontact = epclcont.getText().toString();
        }

        if(ephouse.getText().toString().length() == 0){
            household = "0";
        }else{
            household = ephouse.getText().toString();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("firstname",epfirstname.getText().toString());
        map.put("middlename",mname);
        map.put("lastname",eplastname.getText().toString());
        map.put("currentStatus",currentStatus.getText().toString());
        map.put("user",puser.getText().toString());
        map.put("status",statustv.getText().toString());
        map.put("pass",pass.getText().toString());
        map.put("dateadded",dateadded.getText().toString());
        map.put("dateended",dateended.getText().toString());
        map.put("patientnum",patientnumtv.getText().toString());
        map.put("refnum",refnum.getText().toString());
        map.put("patienttoken",patienttoken.getText().toString());
        map.put("pwd", pwd);
        map.put("disability", disabilityf);
        map.put("barangay", actBrgy.getText().toString());
        map.put("noofinfect", noofinfect.getText().toString());
        map.put("senior", senior);
        map.put("age",epage.getText().toString());
        map.put("contactnum", epcontact.getText().toString());
        map.put("sex",sexf);
        map.put("birthday",dateButton.getText().toString());
        map.put("email",epemail.getText().toString());
        map.put("address",epaddress.getText().toString());
        map.put("occupation",epoccu.getText().toString());
        map.put("locofwork",eploc.getText().toString());
        map.put("household",household);
        map.put("healthcond",healthcondition);
        map.put("swab",swabButton.getText().toString());
        map.put("vaccinestat",epvac.getText().toString());
        map.put("closecontact",closecontact);
        Map<String, Object> mapforbrgy = new HashMap<>();
        mapforbrgy.put("name", actBrgy.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("patients").child(patientnum)
                .updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Admin_EditPatient.this, "Succesfully Updated!", Toast.LENGTH_SHORT).show();
                        logs();
                        if (prevBarangay.getText().toString().equals(actBrgy.getText().toString())) {
                            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(actBrgy.getText().toString()).child(patientnum).updateChildren(map);
                        }else{
                            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(prevBarangay.getText().toString()).child(patientnum).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("patientongoing").child(actBrgy.getText().toString()).child(patientnum).updateChildren(map);
                        }
                        FirebaseDatabase.getInstance().getReference().child("patientongoingall").child(patientnum).updateChildren(map);
                        Intent intent = new Intent (Admin_EditPatient.this, Admin_PatientViewActivity.class);
                        intent.putExtra("barangay", barangayintent);
                        intent.putExtra("from", act);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_EditPatient.this, "Error in Updating", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void logs(){
        Map<String, Object> map = new HashMap<>();
        map.put("date", datewithtime);
        map.put("action", "Edited Patient " +epfirstname.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("adminlogs").child(datetoday).push().setValue(map);
    }

    private void validations(){
        if(epfirstname.getText().toString().length() == 0){
            epfirstname.setError("Firstname is Required");
            epfirstname.requestFocus();
            Toast.makeText(this, "Firstname can't be empty", Toast.LENGTH_SHORT).show();
        }else if (eplastname.getText().toString().length() == 0){
            eplastname.setError("Lastname is Required");
            eplastname.requestFocus();
            Toast.makeText(this, "Lastname can't be empty", Toast.LENGTH_SHORT).show();
        }else if (epage.getText().toString().length() == 0){
            epage.setError("Age is Required");
            epage.requestFocus();
            Toast.makeText(this, "Age can't be empty", Toast.LENGTH_SHORT).show();
        }else if (epcontact.getText().toString().length() == 0){
            epcontact.setError("Contact Number is Required");
            epcontact.requestFocus();
            Toast.makeText(this, "Contact Number can't be empty", Toast.LENGTH_SHORT).show();
        }else if (epemail.getText().toString().length() == 0){
            epemail.setError("Email is Required");
            epemail.requestFocus();
            Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show();
        }else if(epcontact.getText().toString().length() < 9){
            epcontact.setError("Invalid contact number");
            epcontact.requestFocus();
            Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show();
        }else if (isValidEmail(epemail.getText().toString()) == false){
            epemail.setError("Email Invalid");
            epemail.requestFocus();
            Toast.makeText(this, "Please input a valid Email", Toast.LENGTH_SHORT).show();
        }else if (epaddress.getText().toString().length() == 0){
            epaddress.setError("Complete Address is Required");
            epaddress.requestFocus();
            Toast.makeText(this, "Complete Address can't be empty", Toast.LENGTH_SHORT).show();
        }else if(epoccu.getText().toString().length() == 0){
            epoccu.setError("Occupation is Required");
            epoccu.requestFocus();
            Toast.makeText(this, "Occupation can't be empty", Toast.LENGTH_SHORT).show();
        }else if(eploc.getText().toString().length() == 0){
            eploc.setText("Location of Work is Required");
            eploc.requestFocus();
            Toast.makeText(this, "Location of work can't be empty", Toast.LENGTH_SHORT).show();
        }else{
            updateData();
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getbarangayandstatus(){
        Intent intent = getIntent();
        barangayintent = intent.getStringExtra("barangay");
        act = intent.getStringExtra("from");
        pnum = intent.getStringExtra("pnum");
    }
    private boolean isOnline() {
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
    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
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
        }, delay);

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