package com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient;

import static com.example.dashboard1.Admin.AdminPatientView.OngoingFragment.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Admin_newPerPatientMonitor extends AppCompatActivity {
    TextView pname , tv_selecteddate;
    String patientstartDate, patientendDate;
    Button btnexportPDF;
    ImageView img_editbutton, img_delete;
    TextInputEditText tie_remark;
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    float columnwidth[] = {100f, 100f, 100f, 100f, 100f,100f,100f,100f,100f,100f,100f,100f,100f};
    Table table = new Table(columnwidth);
    TextInputLayout til_remark;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference adminsentnotif = database.getReference("adminsentnotif");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference adminlogs = database.getReference("adminlogs");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
    String name, patientnum, barangay, status;
    HorizontalCalendar horizontalCalendar;
    Calendar startDate, endDate;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    LinearLayout ll_notanschecklistdata, ll_notans, ll_done, ll_nostatus, ll_answer, ll_checklistable, ll_nochecklistdata, ll_nosymp, ll_mild, ll_moderate, ll_severe;
    TextView htv_healthcon, txt_refremark, cough, temp, sorethroat, shortness, vomit, diarrhea, fatigue, headache, jointpain;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    String selecteddate = date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_per_patient_monitor);
        init();
        getnameandpnumbrgystatus();
        getstartDate();
        pname = findViewById(R.id.M_pname);
        pname.setText(name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /* starts before 1 month from now */

        /* ends after 1 month from now */

        getchecklistdata();
        getpatientStatus();
        getremark();

        btnexportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus diaall = DialogPlus.newDialog(Admin_newPerPatientMonitor.this)
                        .setContentHolder(new ViewHolder(R.layout.ppmonitoring_popup))
                        .setExpanded(true, 1000)
                        .create();
                View v = diaall.getHolderView();
                EditText et_fullname = v.findViewById(R.id.et_fullname);
                Button btn_exportstor = v.findViewById(R.id.btn_exportstor);
                Button btn_exportemail = v.findViewById(R.id.btn_exportemail);
                diaall.show();

                btn_exportstor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_fullname.getText().toString().length() == 0) {
                            et_fullname.setError("Fullname is Required");
                            et_fullname.requestFocus();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_newPerPatientMonitor.this);
                            builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String fullname = et_fullname.getText().toString();
                                    try {
                                        exportthisMonitoringData(fullname);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    diaall.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    diaall.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                });

                btn_exportemail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fullname = et_fullname.getText().toString();
                        if(et_fullname.getText().toString().length() == 0) {
                            et_fullname.setError("Fullname is Required");
                            et_fullname.requestFocus();
                        }else{
                            try {
                                exportthisMonitoringData(fullname);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_newPerPatientMonitor.this);
                            builder.setTitle("Confirm Export?");
                            builder.setMessage("Data will be saved in InternalStorage/Downloads and it will be sent to the admin email.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    diaall.dismiss();
                                    //String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+patientnum+ " "+ selecteddate +" monitoringdata.pdf";
                                    emailthismonitoring();
                                    Toast.makeText(Admin_newPerPatientMonitor.this, "Sucessfully Sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    diaall.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }
                });
            }
        });
        img_editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("remark", tie_remark.getText().toString());
                patientmonitoring.child(patientnum).child(selecteddate+patientnum)
                        .updateChildren(map1);
                tie_remark.clearFocus();
                Toast.makeText(Admin_newPerPatientMonitor.this, "Remark saved!", Toast.LENGTH_SHORT).show();
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tie_remark.setText(null);
                Map<String, Object> map1 = new HashMap<>();
                map1.put("remark", " ");
                patientmonitoring.child(patientnum).child(selecteddate+patientnum)
                        .updateChildren(map1);
                tie_remark.clearFocus();
                Toast.makeText(Admin_newPerPatientMonitor.this, "Remark Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getnameandpnumbrgystatus(){
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        patientnum = intent.getStringExtra("patientnum");
        status = intent.getStringExtra("status");
        barangay = intent.getStringExtra("barangay");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (Admin_newPerPatientMonitor.this, Admin_PatientMonitoringActivity.class);
        intent.putExtra("barangay", barangay);
        intent.putExtra("status", status);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void getchecklistdata(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(patientnum).child(date+patientnum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_checklistable.setVisibility(View.VISIBLE);
                            cough.setText(snapshot.child("cough").getValue().toString());
                            diarrhea.setText(snapshot.child("diarrhea").getValue().toString());
                            fatigue.setText(snapshot.child("fatigue").getValue().toString());
                            headache.setText(snapshot.child("headache").getValue().toString());
                            jointpain.setText(snapshot.child("jointpain").getValue().toString());
                            shortness.setText(snapshot.child("shortness").getValue().toString());
                            sorethroat.setText(snapshot.child("sorethroat").getValue().toString());
                            temp.setText(snapshot.child("temp").getValue().toString());
                            vomit.setText(snapshot.child("vomit").getValue().toString());
                            htv_healthcon.setText(snapshot.child("healthcon").getValue().toString());
                            ll_nochecklistdata.setVisibility(View.INVISIBLE);
                        }else{
                            ll_nochecklistdata.setVisibility(View.VISIBLE);
                            ll_checklistable.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void emailthismonitoring(){
        try {
            String stringSenderEmail = "gmamho43@gmail.com";
            String stringReceiverEmail = "gmamho43@gmail.com";
            String stringPasswordSenderEmail = "tpmbrdhjhciowwnq";

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


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(stringSenderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            message.setSubject("Attachment: Patient Information");
            MimeMultipart multipart = new MimeMultipart("related");


            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<img src=\"cid:image\"><p><br><br>Attach to this email is a PDF file of the Monitoring data of Patient "+patientnum+". Thank you! <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+patientnum+ " "+ selecteddate +" monitoringdata.pdf";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(patientnum+ " "+ selecteddate +" monitoringdata.pdf");

            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(messageBodyPart2);
            // put everything together
            message.setContent(multipart);


            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
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
    public void getchecklistdataonclick(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(patientnum).child(selecteddate+patientnum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ll_checklistable.setVisibility(View.VISIBLE);
                            cough.setText(snapshot.child("cough").getValue().toString());
                            diarrhea.setText(snapshot.child("diarrhea").getValue().toString());
                            fatigue.setText(snapshot.child("fatigue").getValue().toString());
                            headache.setText(snapshot.child("headache").getValue().toString());
                            jointpain.setText(snapshot.child("jointpain").getValue().toString());
                            shortness.setText(snapshot.child("shortness").getValue().toString());
                            sorethroat.setText(snapshot.child("sorethroat").getValue().toString());
                            temp.setText(snapshot.child("temp").getValue().toString());
                            vomit.setText(snapshot.child("vomit").getValue().toString());
                            htv_healthcon.setText(snapshot.child("healthcon").getValue().toString());
                            ll_nochecklistdata.setVisibility(View.INVISIBLE);
                            ll_notanschecklistdata.setVisibility(View.INVISIBLE);
                        }else{
                            if(!selecteddate.equals(date)){
                                ll_checklistable.setVisibility(View.INVISIBLE);
                                ll_nochecklistdata.setVisibility(View.INVISIBLE);
                                ll_notanschecklistdata.setVisibility(View.VISIBLE);
                            }else{
                                ll_nochecklistdata.setVisibility(View.VISIBLE);
                                ll_checklistable.setVisibility(View.INVISIBLE);
                                ll_notanschecklistdata.setVisibility(View.INVISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void init(){
        htv_healthcon = findViewById(R.id.htv_healthcon);
        img_editbutton = findViewById(R.id.img_editbutton);
        img_delete = findViewById(R.id.img_deletebutton);
        til_remark = findViewById(R.id.til_remark);
        tie_remark = findViewById(R.id.tie_remark);
        txt_refremark = findViewById(R.id.txt_refremark);
        cough = findViewById(R.id.htv_cough);
        diarrhea = findViewById(R.id.htv_diarrhea);
        btnexportPDF = findViewById(R.id.btnexportPDF);
        ll_done = findViewById(R.id.ll_done);
        ll_answer = findViewById(R.id.ll_answer);
        ll_nostatus = findViewById(R.id.ll_nostatus);
        ll_checklistable = findViewById(R.id.ll_checklistable);
        ll_nochecklistdata = findViewById(R.id.ll_nochecklistdata);
        ll_notanschecklistdata = findViewById(R.id.ll_notanschecklistdata);
        ll_notans = findViewById(R.id.ll_notans);
        ll_nosymp = findViewById(R.id.ll_nosymp);
        ll_mild = findViewById(R.id.ll_mild);
        ll_moderate = findViewById(R.id.ll_moderate);
        ll_severe = findViewById(R.id.ll_severe);
        fatigue = findViewById(R.id.htv_fatigue);
        headache = findViewById(R.id.htv_headache);
        jointpain = findViewById(R.id.htv_jointpain);
        shortness = findViewById(R.id.htv_shortness);
        sorethroat = findViewById(R.id.htv_sorethroat);
        temp = findViewById(R.id.htv_temp);
        vomit = findViewById(R.id.htv_vomit);
    }

    private void getremark(){
        patientmonitoring.child(patientnum).child(date+patientnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String remark = snapshot.getValue().toString();
                txt_refremark.setText(remark);
                if(snapshot.exists()){
                    if (remark.equals(" ")) {
                        tie_remark.setText(null);
                    }else{
                        tie_remark.setText(snapshot.child("remark").getValue().toString());
                    }
                }else{
                    tie_remark.setText(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getremarkonclick(){
        patientmonitoring.child(patientnum).child(selecteddate+patientnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String remark = snapshot.getValue().toString();
                    txt_refremark.setText(remark);
                    if (remark.equals(" ")) {
                        tie_remark.setText(null);
                    }else{
                        tie_remark.setText(snapshot.child("remark").getValue().toString());
                    }
                }else{
                    tie_remark.setText(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getpatientStatus(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(patientnum).child(date+patientnum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String status = snapshot.child("status").getValue().toString();

                            if(status.equals("No Symptom")){
                                ll_nosymp.setVisibility(View.VISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                            }else if(status.equals("Mild")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.VISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Moderate")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.VISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Severe")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.VISIBLE);
                            }
                        }else{
                            ll_nosymp.setVisibility(View.INVISIBLE);
                            ll_mild.setVisibility(View.INVISIBLE);
                            ll_moderate.setVisibility(View.INVISIBLE);
                            ll_severe.setVisibility(View.INVISIBLE);
                            ll_nostatus.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getpatientStatusonClick(){
        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(patientnum).child(selecteddate+patientnum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String status = snapshot.child("status").getValue().toString();
                            if(status.equals("No Symptom")){
                                ll_notans.setVisibility(View.INVISIBLE);
                                ll_nosymp.setVisibility(View.VISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if(status.equals("Mild")){
                                ll_notans.setVisibility(View.INVISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.VISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Moderate")){
                                ll_notans.setVisibility(View.INVISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.VISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else if (status.equals("Severe")){
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_notans.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.VISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            if(!selecteddate.equals(date)){
                                ll_notans.setVisibility(View.VISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.INVISIBLE);
                            }else{
                                ll_notans.setVisibility(View.INVISIBLE);
                                ll_nosymp.setVisibility(View.INVISIBLE);
                                ll_mild.setVisibility(View.INVISIBLE);
                                ll_moderate.setVisibility(View.INVISIBLE);
                                ll_severe.setVisibility(View.INVISIBLE);
                                ll_nostatus.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void getstartDate(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(patientnum)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        patientstartDate = snapshot.child("dateadded").getValue().toString();
                        patientendDate = snapshot.child("dateended").getValue().toString();
                        startDate = Calendar.getInstance();
                        endDate = Calendar.getInstance();

                        try {
                            startDate.setTime(sdf.parse(patientstartDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(patientendDate.equals("N/A")){
                            endDate.setTime(Calendar.getInstance().getTime());
                        }else{
                            try {
                                endDate.setTime(sdf.parse(patientendDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        horizontalCalendar = new HorizontalCalendar.Builder(Admin_newPerPatientMonitor.this, R.id.calendarView)
                                .range(startDate, endDate)
                                .datesNumberOnScreen(5)
                                .build();
                        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                            @Override
                            public void onDateSelected(Calendar date, int position) {
                                selecteddate = sdf.format(date.getTime());
                                getchecklistdataonclick();
                                getpatientStatusonClick();
                                getremarkonclick();
                                if(!selecteddate.equals(datetoday)){
                                    til_remark.setHint("No Data");
                                    tie_remark.setEnabled(false);
                                    img_editbutton.setVisibility(View.INVISIBLE);
                                    img_delete.setVisibility(View.INVISIBLE);
                                }else{
                                    til_remark.setHint("Remark");
                                    tie_remark.setEnabled(true);
                                    img_editbutton.setVisibility(View.VISIBLE);
                                    img_delete.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void exportthisMonitoringData(String fullname) throws FileNotFoundException {
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, patientnum+ " "+ selecteddate +" monitoringdata.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1400, 1100);
        Paragraph pmdata = new Paragraph("Patient Monitoring Data - " +selecteddate).setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));

        Drawable d = ContextCompat.getDrawable(this, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        document.add(image);

        document.add(pmdata);
        Paragraph dateh = new Paragraph("Date of Monitoring").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph statush = new Paragraph("Status").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph pnumh = new Paragraph("Patient Number").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph nameh = new Paragraph("Name").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph temph = new Paragraph("Temperature").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph coughh = new Paragraph("Cough").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph sorethroath = new Paragraph("Sorethroat").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph shortnessh = new Paragraph("Shortness of Breath").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph vommith = new Paragraph("Vommiting").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph diarrheah = new Paragraph("Diarrhea").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph fatigueh = new Paragraph("Fatigue/Chills").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph headacheh = new Paragraph("Headache").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph jointpainsh = new Paragraph("Joint Pains").setBold().setTextAlignment(TextAlignment.CENTER);

        Cell date = new Cell();
        date.add(dateh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell status = new Cell();
        status.add(statush).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell pnum = new Cell();
        pnum.add(pnumh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell name = new Cell();
        name.add(nameh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell temp = new Cell();
        temp.add(temph).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell cough = new Cell();
        cough.add(coughh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell sorethroat = new Cell();
        sorethroat.add(sorethroath).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell shortness = new Cell();
        shortness.add(shortnessh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell vommit = new Cell();
        vommit.add(vommith).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell diarrhea = new Cell();
        diarrhea.add(diarrheah).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell fatigue = new Cell();
        fatigue.add(fatigueh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell headache = new Cell();
        headache.add(headacheh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell jointpains = new Cell();
        jointpains.add(jointpainsh).setVerticalAlignment(VerticalAlignment.MIDDLE);


        table.addCell(date);
        table.addCell(status);
        table.addCell(pnum);
        table.addCell(name);
        table.addCell(temp);
        table.addCell(cough);
        table.addCell(sorethroat);
        table.addCell(shortness);
        table.addCell(vommit);
        table.addCell(diarrhea);
        table.addCell(fatigue);
        table.addCell(headache);
        table.addCell(jointpains);

        patientmonitoring.child(patientnum).child(selecteddate+patientnum)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Paragraph dater = new Paragraph(snapshot.child("dateofmonitor").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell date1 = new Cell();
                            date1.add(dater).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(date1);


                            Paragraph statusr = new Paragraph(snapshot.child("status").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell status1 = new Cell();
                            status1.add(statusr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(status1);



                            Paragraph pnumr = new Paragraph(snapshot.child("patientnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pnum1 = new Cell();
                            pnum1.add(pnumr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pnum1);


                            Paragraph namer = new Paragraph(snapshot.child("name").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell name1 = new Cell();
                            name1.add(namer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(name1);

                            Paragraph tempr = new Paragraph(snapshot.child("temp").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell temp1 = new Cell();
                            temp1.add(tempr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(temp1);

                            Paragraph coughr = new Paragraph(snapshot.child("cough").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell cough1 = new Cell();
                            cough1.add(coughr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(cough1);


                            Paragraph sorethroatr = new Paragraph(snapshot.child("sorethroat").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell sorethroat1 = new Cell();
                            sorethroat1.add(sorethroatr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(sorethroat1);

                            Paragraph shortnessr = new Paragraph(snapshot.child("shortness").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell shortness1 = new Cell();
                            shortness1.add(shortnessr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(shortness1);

                            Paragraph vommitr = new Paragraph(snapshot.child("vomit").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell vommit1 = new Cell();
                            vommit1.add(vommitr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(vommit1);

                            Paragraph diarrhear = new Paragraph(snapshot.child("diarrhea").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell diarrhea1 = new Cell();
                            diarrhea1.add(diarrhear).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(diarrhea1);

                            Paragraph fatiguer = new Paragraph(snapshot.child("fatigue").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell fatigue1 = new Cell();
                            fatigue1.add(fatiguer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(fatigue1);

                            Paragraph headacher = new Paragraph(snapshot.child("headache").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell headache1 = new Cell();
                            headache1.add(headacher).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(headache1);

                            Paragraph jointpainsr = new Paragraph(snapshot.child("jointpain").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell jointpains1 = new Cell();
                            jointpains1.add(jointpainsr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(jointpains1);
                        document.add(table);
                        Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        document.add(preparedby);
                        document.add(prepname);
                        document.add(date);
                        document.close();
                        Toast.makeText(Admin_newPerPatientMonitor.this, "PDF of Monitoring Data and Patient Information created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}