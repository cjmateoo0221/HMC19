package com.example.dashboard1.Admin;

import static com.example.dashboard1.Admin.AdminPatientView.Admin_nvPatient.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.dashboard1.Loadingdialog;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Admin_AddPatientActivity extends AppCompatActivity {
    DatabaseReference PatientCountRef;
    int delay = 2 * 1000;
    boolean result;
    Handler handler = new Handler();
    Runnable runnable;
    Calendar cal = Calendar.getInstance();
    long today = cal.get(Calendar.DAY_OF_MONTH);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference login = database.getReference("login");
    DatabaseReference barangaydb = database.getReference("barangay");
    DatabaseReference adminsentnotif = database.getReference("adminsentnotif");
    DatabaseReference patientcount = database.getReference("patientcount");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference adminlogs = database.getReference("adminlogs");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
    AlertDialog.Builder builder1;
    AlertDialog alert11;
    private static Context context;
    Calendar calendar = Calendar.getInstance();
    int month = calendar.get(Calendar.MONTH);
    int yeartoday = calendar.get(Calendar.YEAR);
    TextInputLayout selectpwd;
    Button btnConfirm, dateButton, swabButton;
    String[] status = {"Vaccinated", "Not Vaccinated"};
    String[] barangay = {"Aldiano Olaes", "Benjamin Tirona", "Bernardo Pulido", "Col JP Elises", "Epifano Malia", "F De Castro", "F Reyes", "Fiorello Calimag", "Gavino Maderan", "Gregoria De Jesus", "Inocencio Salud", "Jacinto Lumbreras", "Kapitan Kua", "Macario Dacon", "Marcelino Memije", "Nicolasa Virata", "Pantaleon Granados", "Poblacion 1", "Poblacion 2", "Poblacion 3" , "Poblacion 4", "Poblacion 5", "Ramon Cruz", "San Gabriel", "San Jose", "Severino Delas Alas", "Tinente Tiago"};
    String[] disability = {"Psychosocial", "Chronic Illness", "Learning", "Mental", "Visual", "Orthopedic", "Communication"};
    FloatingActionButton floatingActionButton;
    final Loadingdialog loadingdialog = new Loadingdialog(Admin_AddPatientActivity.this);
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    String slstatus;
    String slbarangay;
    String sldisablity;
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1, actBrgy;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems1;
    ArrayAdapter<String> adapterItems2;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    RadioGroup rgsexgrp, rgpwdgrp, rgseniorgrp;
    private DatePickerDialog datePickerDialog, datePickerDialog2;
    RadioButton rgselectedSex, rgselectedDis, rgselectedSenior;
    TextView etpuser, etppass, etpref;
    EditText etname, etage, etemail, etaddress, etoccu, etlocofoccu, ethousehold, ethealtcond, etvaccine, etclcontact, etcontactnum , etfirstname ,etmiddlename, etlastname;
    long patientCount = 1;
    ImageView img_pwdinfo;
    long customkey = 0;
    String userstatus = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        context = getApplicationContext();
        init();
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        floatingActionButton = findViewById(R.id.apfabconfirm);
        dateButton = findViewById(R.id.bdaypicker);
        swabButton = findViewById(R.id.swabpicker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
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
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setText("Vaccinated",false);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                slstatus = parent.getItemAtPosition(i).toString();
            }
        });

        actBrgy.setAdapter(adapterItems2);
        actBrgy.setText("Aldiano Olaes",false);
        actBrgy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                slbarangay = parent.getItemAtPosition(i).toString();
            }
        });
        dateButton.setText(getTodaysDate());
        swabButton.setText(getTodaysDate());
        rgpwdgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rbpwdYes){
                adapterItems1 = new ArrayAdapter<String>(Admin_AddPatientActivity.this,R.layout.disability,disability);
                selectpwd.setEnabled(true);
                autoCompleteTextView1.setText("Psychosocial",false);
                autoCompleteTextView1.setAdapter(adapterItems1);
                }else if(i == R.id.rbpwdNo){
                    selectpwd.setEnabled(false);
                    autoCompleteTextView1.setText("N/A");
                }
            }
        });
        initDatePicker();
        initDatePicker2();
        etppass.setText(getRandomString(6));
        totalPatients();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
                /*insertData();
                addtologin();*/
                incrementGraph();
            }
        });

    img_pwdinfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final DialogPlus diaall = DialogPlus.newDialog(Admin_AddPatientActivity.this)
                    .setContentHolder(new ViewHolder(R.layout.pwdinfo_popup))
                    .setExpanded(true, 1000)
                    .create();
            View v = diaall.getHolderView();
            ImageView img_closepwdfinfo = v.findViewById(R.id.img_closepwdinfo);
            diaall.show();
            img_closepwdfinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diaall.dismiss();
                }
            });
        }
    });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(month, day, year);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }

    public void init(){
        img_pwdinfo = findViewById(R.id.img_pwdinfo);
        etfirstname = findViewById(R.id.et_firstname);
        etmiddlename = findViewById(R.id.et_middlename);
        etlastname = findViewById(R.id.et_lastname);
        etage = findViewById(R.id.et_age);
        etemail = findViewById(R.id.et_email);
        etaddress = findViewById(R.id.et_address);
        etoccu = findViewById(R.id.et_occupation);
        etlocofoccu = findViewById(R.id.et_locofwork);
        ethousehold = findViewById(R.id.et_household);
        ethealtcond = findViewById(R.id.et_health);
        etclcontact = findViewById(R.id.et_clcontact);
        etpuser = findViewById(R.id.et_puser);
        etppass = findViewById(R.id.et_ppass);
        etcontactnum = findViewById(R.id.et_contactnum);
        btnConfirm = findViewById(R.id.btnConfirm);
        rgsexgrp = findViewById(R.id.rg_sex);
        rgpwdgrp = findViewById(R.id.rg_pwd);
        etpref = findViewById(R.id.et_pref);
        selectpwd = findViewById(R.id.selectpwd);
        rgseniorgrp = findViewById(R.id.rg_senior);
    }

    public void insertData(){
        String household, sex, pwd, senior, healthcondition, closecontact;
        int selectedSenior = rgseniorgrp.getCheckedRadioButtonId();
        rgselectedSenior = findViewById(selectedSenior);
        senior = rgselectedSenior.getText().toString();
        int selectedPWD = rgpwdgrp.getCheckedRadioButtonId();
        rgselectedDis = findViewById(selectedPWD);
        pwd = rgselectedDis.getText().toString();
        int selectedSex = rgsexgrp.getCheckedRadioButtonId();
        rgselectedSex = findViewById(selectedSex);
        sex = rgselectedSex.getText().toString();
        String disabilityf, mname;
        if(autoCompleteTextView1.getText().toString().length() == 0){
            disabilityf = "N/A";
        }else{
            disabilityf = autoCompleteTextView1.getText().toString();
        }

        if(etmiddlename.getText().toString().length() == 0){
            mname = "N/A";
        }else{
            mname = etmiddlename.getText().toString();
        }
        if(ethealtcond.getText().toString().length() == 0){
            healthcondition = "None";
        }else{
            healthcondition = ethealtcond.getText().toString();
        }

        if(etclcontact.getText().toString().length() == 0){
            closecontact = "None";
        }else{
            closecontact = etclcontact.getText().toString();
        }

        if(ethousehold.getText().toString().length() == 0){
            household = "0";
        }else{
            household = ethousehold.getText().toString();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("firstname", etfirstname.getText().toString());
        map.put("middlename", mname);
        map.put("lastname", etlastname.getText().toString());
        map.put("age",etage.getText().toString());
        map.put("barangay",actBrgy.getText().toString());
        map.put("currentStatus", "N/A");
        map.put("contactnum", etcontactnum.getText().toString());
        map.put("sex",sex);
        map.put("birthday",dateButton.getText().toString());
        map.put("patientnum","P-"+Long.toString(patientCount));
        map.put("patienttoken","");
        map.put("dateadded", datewithtime);
        map.put("dateended", "N/A");
        map.put("startday", String.valueOf(today));
        map.put("email",etemail.getText().toString());
        map.put("address", etaddress.getText().toString());
        map.put("occupation", etoccu.getText().toString());
        map.put("locofwork", etlocofoccu.getText().toString());
        map.put("household", household);
        map.put("healthcond", healthcondition);
        map.put("swab", swabButton.getText().toString());
        map.put("status", "Active");
        map.put("noofinfect", "1");
        map.put("pwd", pwd);
        map.put("disability", disabilityf);
        map.put("senior", senior);
        map.put("vaccinestat", autoCompleteTextView.getText().toString());
        map.put("closecontact", closecontact);
        map.put("user","P-"+Long.toString(patientCount));
        map.put("pass", etppass.getText().toString());
        map.put("refnum", etpref.getText().toString());
        Map<String, Object> mapforbrgy = new HashMap<>();
        mapforbrgy.put("name", actBrgy.getText().toString());

        patients.child("P-"+Long.toString(patientCount))
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Admin_AddPatientActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                        incrementPatientcount();
                        logs();
                        barangaydb.child(actBrgy.getText().toString()).setValue(mapforbrgy);
                        patientongoing.child(actBrgy.getText().toString()).child("P-"+Long.toString(patientCount)).setValue(map);
                        patientongoingall.child("P-"+Long.toString(patientCount)).setValue(map);
                        senduserandpass();
                        startActivity(new Intent(Admin_AddPatientActivity.this, AdminActivity.class));
                        finish();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_AddPatientActivity.this, "Error While Inserting Data", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void addtologin(){
        Map<String, Object> usermap = new HashMap<>();
        usermap.put("password", etppass.getText().toString());
        usermap.put("username", etpuser.getText().toString());
        usermap.put("as",userstatus.toString());
        login.child(etpuser.getText().toString())
                .setValue(usermap);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void totalPatients(){
        patientcount.child("pvalue").child("pinserted").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               patientCount = (long) snapshot.getValue();
               patientCount = patientCount + 1;
               etpuser.setText("P-"+Long.toString(patientCount));
               etpref.setText(Long.toString(patientCount));
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }
    private void validations(){
        if(etfirstname.getText().toString().length() == 0){
            etfirstname.setError("Firstname is Required");
            etfirstname.requestFocus();
            Toast.makeText(this, "Firstname can't be empty", Toast.LENGTH_SHORT).show();
        }else if(etlastname.getText().toString().length() == 0){
            etlastname.requestFocus();
            etlastname.setError("Lastname is Required");
            Toast.makeText(this, "Lastname can't be empty", Toast.LENGTH_SHORT).show();
        } else if (etage.getText().toString().length() == 0){
            etage.setError("Age is Required");
            etage.requestFocus();
            Toast.makeText(this, "Age can't be empty (Input patient's date of birth)", Toast.LENGTH_SHORT).show();
        }else if (etcontactnum.getText().toString().length() == 0){
            etcontactnum.setError("Contact Number is Required");
            etcontactnum.requestFocus();
            Toast.makeText(this, "Contact Number can't be empty", Toast.LENGTH_SHORT).show();
        }else if (etemail.getText().toString().length() == 0){
            etemail.setError("Email is Required");
            etemail.requestFocus();
            Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show();
        }else if (isValidEmail(etemail.getText().toString()) == false){
            etemail.setError("Email Invalid");
            etemail.requestFocus();
            Toast.makeText(this, "Please input a valid Email", Toast.LENGTH_SHORT).show();
        }else if (etaddress.getText().toString().length() == 0){
            etaddress.requestFocus();
            etaddress.setError("Complete Address is Required");
            Toast.makeText(this, "Complete Address can't be empty", Toast.LENGTH_SHORT).show();
        }else if(etoccu.getText().toString().length() == 0){
            etoccu.requestFocus();
            etoccu.setError("Occupation is Required");
            Toast.makeText(this, "Occupation can't be empty", Toast.LENGTH_SHORT).show();
        }else if(etlocofoccu.getText().toString().length() == 0){
            etlocofoccu.requestFocus();
            etlocofoccu.setError("Location of Work is Required");
            Toast.makeText(this, "Location of work can't be empty", Toast.LENGTH_SHORT).show();
        }else{
            loadingdialog.showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertData();
                    addtologin();
                    loadingdialog.disMiss();
                }
            }, 1000);

        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void incrementPatientcount(){
        Map<String, Object> map1 = new HashMap<>();
        map1.put("pinserted",patientCount);
        patientcount.child("pvalue").setValue(map1);
    }

    public void openDatePicker(View view) {
            datePickerDialog.show();
    }
    public void openDatePicker2(View view) {
        datePickerDialog2.show();
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
                etage.setText(String.valueOf(age));
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
                return "January";
        if(month == 2)
                return "February";
        if(month == 3)
                return "March";
        if(month == 4)
                return "April";
        if(month == 5)
                return "May";
        if(month == 6)
                return "June";
        if(month == 7)
                return "July";
        if(month == 8)
                return "August";
        if(month == 9)
                return "September";
        if(month == 10)
                return "October";
        if(month == 11)
                return "November";
        if(month == 12)
                return "December";

        return "JAN";
        }
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public void logs(){
        Map<String, Object> map = new HashMap<>();
        map.put("date", datewithtime);
        map.put("action", "Added Patient " +"P-"+Long.toString(patientCount));
        adminlogs.child(datetoday).push().setValue(map);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void senduserandpass(){
        try {
            String stringSenderEmail = "gmamho43@gmail.com";
            String stringReceiverEmail = etemail.getText().toString();
            String stringPasswordSenderEmail = "tC!Fq57M";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Login Credentials for HMC19 Application");
            mimeMessage.setText("Hi "+etfirstname.getText().toString()+ " !" +" Your username is: " +etpuser.getText().toString()+ " and your password is: " +etppass.getText().toString()+ " Please save this account details, Thank you." + "\n" + "Here is the link for the apk file of our monitoring application" +"\n" + "https://drive.google.com/drive/folders/1mYqCePDd6GT3P8GfaVxhT_8epNWH-WVR?usp=sharing");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    private void incrementGraph(){
        ChartValues.child("des").child(String.valueOf(month)).child("count")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   int value = Integer.parseInt(snapshot.getValue().toString());
                   int incvalue = value + 1;
                   snapshot.getRef().setValue(incvalue);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
