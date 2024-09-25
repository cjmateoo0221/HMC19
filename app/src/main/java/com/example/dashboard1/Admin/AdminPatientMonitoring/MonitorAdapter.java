package com.example.dashboard1.Admin.AdminPatientMonitoring;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.stat;
import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.context;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient.Admin_newPerPatientMonitor;
import com.example.dashboard1.Loadingdialog1;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MonitorAdapter extends FirebaseRecyclerAdapter<MonitorModel, MonitorAdapter.myViewHolder> {

    String pToken;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    String patientnum1;
    float columnwidth[] = {100f, 100f, 100f, 100f, 100f,100f,100f,100f,100f,100f,100f,100f,100f};
    Table table = new Table(columnwidth);
    String pdfpnumm;
    String barangay;
    String status;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference patientdismissed = database.getReference("patientdismissed");
    DatabaseReference patientdismissedbrgy = database.getReference("patientdismissedbrgy");
    DatabaseReference adminsentnotif = database.getReference("adminsentnotif");
    DatabaseReference patients = database.getReference("patients");
    DatabaseReference patientongoingall = database.getReference("patientongoingall");
    DatabaseReference patientongoing = database.getReference("patientongoing");
    DatabaseReference patientlogs = database.getReference("patientlogs");
    DatabaseReference adminlogs = database.getReference("adminlogs");
    DatabaseReference dailystatus = database.getReference("dailystatus");
    DatabaseReference notifications = database.getReference("notifications");
    DatabaseReference patientmonitoring = database.getReference("patientmonitoring");
    DatabaseReference ChartValues = database.getReference("ChartValues");
    Calendar calendar = Calendar.getInstance();
    int month = calendar.get(Calendar.MONTH);
    final Loadingdialog1 loadingdialog1 = new Loadingdialog1(context);
    String prefnum, pemail;
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    public static String serverKey = "Your server key here";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MonitorAdapter(@NonNull FirebaseRecyclerOptions<MonitorModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MonitorModel model) {

        patientnum1 = model.getPatientnum();
        status = model.getCurrentStatus();
        barangay = model.getBarangay();
    if(status.equals(date+"Mild")){
            holder.rl_pitem.setBackgroundColor(Color.parseColor("#99FFEB3B"));
        }else if (status.equals(date+"Moderate")){
            holder.rl_pitem.setBackgroundColor(Color.parseColor("#99FF5722"));
        }else if (status.equals(date+"Severe")){
            holder.rl_pitem.setBackgroundColor(Color.parseColor("#99F44336"));
        } else if (status.equals(date+"No Symptom")) {
            holder.rl_pitem.setBackgroundColor(Color.parseColor("#994CAF50"));
        }else{
            holder.rl_pitem.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.patientnum.setText(model.getPatientnum());
        holder.name.setText(model.getName());
        holder.pref.setText(model.getRefnum());
        holder.status.setText(model.getStatus());
        FCMSend.SetServerKey(serverKey);

        holder.btnmPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.patientnum.getContext())
                        .setContentHolder(new ViewHolder(R.layout.pdfdialog))
                        .setExpanded(true,1700)
                        .create();
                View v = dialogPlus.getHolderView();
                TextView pdfpnum = v.findViewById(R.id.pdfpnum);
                TextView pdfname = v.findViewById(R.id.pdfname);
                EditText et_pdffullname = v.findViewById(R.id.et_pdffullname);
                Button btn_pdfstor = v.findViewById(R.id.btn_pdfstor);
                Button btn_pdfemail = v.findViewById(R.id.btn_pdfemail);
                pdfpnum.setText(holder.patientnum.getText().toString());
                pdfpnumm = holder.patientnum.getText().toString();
                pdfname.setText(holder.name.getText().toString());
                patientmonitoring.child(holder.patientnum.getText().toString())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    btn_pdfemail.setEnabled(false);
                                    btn_pdfstor.setEnabled(false);
                                }else{
                                    btn_pdfstor.setEnabled(true);
                                    btn_pdfemail.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                dialogPlus.show();
                btn_pdfstor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_pdffullname.getText().toString().length() == 0) {
                            et_pdffullname.setError("Fullname is Required");
                            et_pdffullname.requestFocus();
                        }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String fullname = et_pdffullname.getText().toString();
                                        try {
                                            createPDFmonitoring(fullname);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        dialogPlus.dismiss();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        dialogPlus.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }
                });
            }
        });

        holder.btnViewMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent (holder.patientnum.getContext(), Admin_PerPatientMonitor.class);
                intent.putExtra("patientnum", holder.patientnum.getText().toString());
                intent.putExtra("name", holder.name.getText().toString());
                intent.putExtra("barangay", barangay);
                holder.patientnum.getContext().startActivity(intent);*/
                Intent intent = new Intent (holder.patientnum.getContext(), Admin_newPerPatientMonitor.class);
                intent.putExtra("patientnum", holder.patientnum.getText().toString());
                intent.putExtra("name", holder.name.getText().toString());
                intent.putExtra("barangay", barangay);
                intent.putExtra("status", stat);
                holder.patientnum.getContext().startActivity(intent);
            }
        });
        holder.btndismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.patientnum.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dismiss_popup))
                        .setExpanded(true,2500)
                        .create();
                View v = dialogPlus.getHolderView();
                TextView dppnum = v.findViewById(R.id.dppnum);
                TextView dppref = v.findViewById(R.id.dprefnum);
                TextView dpname = v.findViewById(R.id.dpname);
                EditText et_dismissfullname = v.findViewById(R.id.et_dismissfullname);
                Button btnconfirm = v.findViewById(R.id.dpbtnconfirm);
                dppnum.setText(holder.patientnum.getText().toString());
                dpname.setText(holder.name.getText().toString());
                dppref.setText(holder.pref.getText().toString());

                dialogPlus.show();

                btnconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_dismissfullname.getText().toString().length() == 0) {
                            et_dismissfullname.setError("Fullname is Required");
                            et_dismissfullname.requestFocus();
                        }else{
                            loadingdialog1.showLoading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String fullname = et_dismissfullname.getText().toString();
                                    pemail = model.getEmail();
                                    pdfpnumm = dppnum.getText().toString();
                                    try {
                                        String stringSenderEmail = "gmamho43@gmail.com";
                                        String stringReceiverEmail = pemail;
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

                                        mimeMessage.setSubject("Subject: Reference Number for Quarantine Clearance");
                                        mimeMessage.setText("Congratulations on your Recovery! Your Reference number is " +dppref.getText().toString()+". Please go to the Municipal Health Office to process your Quarantine Certificate. Thank you.");
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
                                    barangay = model.getBarangay();
                                    patients.child(holder.patientnum.getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    pToken = snapshot.child("patienttoken").getValue().toString();
                                                    FCMSend.Builder build = new FCMSend.Builder(pToken)
                                                            .setTitle("MHO Admin")
                                                            .setBody("Congratulations on your Recovery! Your Reference number is "+dppref.getText().toString()+". Please go to the Municipal Health Office to process your Quarantine Certificate. Thank you.");
                                                    build.send().Result();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    incrementGraph();
                                    try {
                                        getMonitoringDataofendpatient(fullname);
                                        getPatientinfodismissed(fullname);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("date", date);
                                    map.put("message","Congratulations on your Recovery! Your Reference number is " +dppref.getText().toString()+". Please go to the Municipal Health Office to process your Quarantine Certificate. Thank you.");
                                    map.put("receiver",holder.patientnum.getText().toString());
                                    map.put("sender","mhoadmin");
                                    notifications.child(holder.patientnum.getText().toString()).child(date).push()
                                            .setValue(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(holder.patientnum.getContext(), "Error while Sending", Toast.LENGTH_SHORT).show();
                                                    dialogPlus.dismiss();
                                                }
                                            });
                                    adminsentnotif.push()
                                            .setValue(map);
                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("status", "Concluded");
                                    patients.child(holder.patientnum.getText().toString())
                                            .updateChildren(map1);
                                    Map<String, Object> map2 = new HashMap<>();
                                    map2.put("dateended", date);
                                    patients.child(holder.patientnum.getText().toString())
                                            .updateChildren(map2);
                                    logs();
                                    patients.child(holder.patientnum.getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    FirebaseDatabase.getInstance().getReference("patientdismissed").child(holder.patientnum.getText().toString()).setValue(snapshot.getValue());
                                                    FirebaseDatabase.getInstance().getReference("patientdismissedbrgy").child(barangay).child(holder.patientnum.getText().toString()).setValue(snapshot.getValue());
                                                    dailystatus.child(status).child(barangay).child(holder.patientnum.getText().toString()).removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    patientdismissed.child(holder.patientnum.getText().toString())
                                            .updateChildren(map2);
                                    patientdismissedbrgy.child(barangay).child(holder.patientnum.getText().toString())
                                            .updateChildren(map2);
                                    patientongoingall.child(holder.patientnum.getText().toString())
                                            .removeValue();
                                    patientongoing.child(barangay).child(holder.patientnum.getText().toString())
                                            .removeValue();
                                    Toast.makeText(holder.patientnum.getContext(), "Patient Successfuly Dismissed", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                    loadingdialog1.disMiss();
                                }
                            }, 1000);
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_patientitem, parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_pitem;
        TextView patientnum, name, pref, status;
        Button btnViewMonitoring, btndismiss, btnmPDF;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_pitem = (RelativeLayout) itemView.findViewById(R.id.rl_pitem);
            patientnum = (TextView) itemView.findViewById(R.id.pID);
            pref = (TextView) itemView.findViewById(R.id.pRef);
            name = (TextView) itemView.findViewById(R.id.pName);
            status = (TextView) itemView.findViewById(R.id.pStatus);
            btndismiss = (Button) itemView.findViewById(R.id.btndismiss);
            btnViewMonitoring = (Button) itemView.findViewById(R.id.btnviewMonitor);
            btnmPDF = (Button) itemView.findViewById(R.id.btnmPDF);

        }
    }
    public void logs(){
        Map<String, Object> map = new HashMap<>();
        map.put("date", datewithtime);
        map.put("action", "Dismissed Patient " +patientnum1);
        adminlogs.child(datetoday).push().setValue(map);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0){
            Toast.makeText(context, "No Patients Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void putintodismiss(){
        FirebaseDatabase.getInstance().getReference("patients").child(patientnum1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase.getInstance().getReference("patientdismissed").child(patientnum1).setValue(snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void createPDFmonitoring(String fullname) throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, patientnum1+" monitoringdata.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1400, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pmdata = new Paragraph("Patient Monitoring Data").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(context, R.drawable.pdfbanner);
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

        patientmonitoring.child(patientnum1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                            Paragraph dater = new Paragraph(myDataSnapshot.child("dateofmonitor").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell date1 = new Cell();
                            date1.add(dater).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(date1);

                            Paragraph statusr = new Paragraph(myDataSnapshot.child("status").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell status1 = new Cell();
                            status1.add(statusr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(status1);

                            Paragraph pnumr = new Paragraph(myDataSnapshot.child("patientnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pnum1 = new Cell();
                            pnum1.add(pnumr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pnum1);


                            Paragraph namer = new Paragraph(myDataSnapshot.child("name").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell name1 = new Cell();
                            name1.add(namer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(name1);

                            Paragraph tempr = new Paragraph(myDataSnapshot.child("temp").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell temp1 = new Cell();
                            temp1.add(tempr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(temp1);

                            Paragraph coughr = new Paragraph(myDataSnapshot.child("cough").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell cough1 = new Cell();
                            cough1.add(coughr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(cough1);


                            Paragraph sorethroatr = new Paragraph(myDataSnapshot.child("sorethroat").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell sorethroat1 = new Cell();
                            sorethroat1.add(sorethroatr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(sorethroat1);

                            Paragraph shortnessr = new Paragraph(myDataSnapshot.child("shortness").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell shortness1 = new Cell();
                            shortness1.add(shortnessr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(shortness1);

                            Paragraph vommitr = new Paragraph(myDataSnapshot.child("vomit").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell vommit1 = new Cell();
                            vommit1.add(vommitr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(vommit1);

                            Paragraph diarrhear = new Paragraph(myDataSnapshot.child("diarrhea").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell diarrhea1 = new Cell();
                            diarrhea1.add(diarrhear).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(diarrhea1);

                            Paragraph fatiguer = new Paragraph(myDataSnapshot.child("fatigue").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell fatigue1 = new Cell();
                            fatigue1.add(fatiguer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(fatigue1);

                            Paragraph headacher = new Paragraph(myDataSnapshot.child("headache").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell headache1 = new Cell();
                            headache1.add(headacher).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(headache1);

                            Paragraph jointpainsr = new Paragraph(myDataSnapshot.child("jointpain").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell jointpains1 = new Cell();
                            jointpains1.add(jointpainsr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(jointpains1);
                        }
                        document.add(table);
                        Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        document.add(preparedby);
                        document.add(prepname);
                        document.add(date);
                        document.close();
                        Toast.makeText(context, "PDF of Monitoring Data and Patient Information created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void incrementGraph(){
        ChartValues.child("con").child(String.valueOf(month)).child("count")
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
    private void getMonitoringDataofendpatient(String fullname) throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, patientnum1+" monitoringdata.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1400, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pmdata = new Paragraph("Patient Monitoring Data").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(context, R.drawable.pdfbanner);
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

        patientmonitoring.child(patientnum1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                            Paragraph dater = new Paragraph(myDataSnapshot.child("dateofmonitor").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell date1 = new Cell();
                            date1.add(dater).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(date1);

                            Paragraph statusr = new Paragraph(myDataSnapshot.child("status").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell status1 = new Cell();
                            status1.add(statusr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(status1);

                            Paragraph pnumr = new Paragraph(myDataSnapshot.child("patientnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pnum1 = new Cell();
                            pnum1.add(pnumr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pnum1);


                            Paragraph namer = new Paragraph(myDataSnapshot.child("name").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell name1 = new Cell();
                            name1.add(namer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(name1);

                            Paragraph tempr = new Paragraph(myDataSnapshot.child("temp").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell temp1 = new Cell();
                            temp1.add(tempr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(temp1);

                            Paragraph coughr = new Paragraph(myDataSnapshot.child("cough").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell cough1 = new Cell();
                            cough1.add(coughr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(cough1);


                            Paragraph sorethroatr = new Paragraph(myDataSnapshot.child("sorethroat").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell sorethroat1 = new Cell();
                            sorethroat1.add(sorethroatr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(sorethroat1);

                            Paragraph shortnessr = new Paragraph(myDataSnapshot.child("shortness").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell shortness1 = new Cell();
                            shortness1.add(shortnessr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(shortness1);

                            Paragraph vommitr = new Paragraph(myDataSnapshot.child("vomit").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell vommit1 = new Cell();
                            vommit1.add(vommitr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(vommit1);

                            Paragraph diarrhear = new Paragraph(myDataSnapshot.child("diarrhea").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell diarrhea1 = new Cell();
                            diarrhea1.add(diarrhear).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(diarrhea1);

                            Paragraph fatiguer = new Paragraph(myDataSnapshot.child("fatigue").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell fatigue1 = new Cell();
                            fatigue1.add(fatiguer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(fatigue1);

                            Paragraph headacher = new Paragraph(myDataSnapshot.child("headache").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell headache1 = new Cell();
                            headache1.add(headacher).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(headache1);

                            Paragraph jointpainsr = new Paragraph(myDataSnapshot.child("jointpain").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell jointpains1 = new Cell();
                            jointpains1.add(jointpainsr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(jointpains1);
                        }
                        document.add(table);
                        Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        document.add(preparedby);
                        document.add(prepname);
                        document.add(date);
                        document.close();
                        Toast.makeText(context, "PDF of Monitoring Data and Patient Information created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getPatientinfodismissed(String fullname) throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath,  patientnum1+ "Information.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1800, 1100);
        Document documentdismissed = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pdata = new Paragraph("Patient Information").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(context, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        documentdismissed.add(image);

        documentdismissed.add(pdata);
        float columnwidth[] = {200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
        Table table = new Table(columnwidth);
        Paragraph pnumh = new Paragraph("Patient Number").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph fnameh = new Paragraph("Firstname").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph mnameh = new Paragraph("Middlename").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph lnameh = new Paragraph("Lastname").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph ageh = new Paragraph("Age").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph birthdayh = new Paragraph("Birthday").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph sexh = new Paragraph("Sex").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph contactnumh = new Paragraph("Contact Number").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph emailh = new Paragraph("Email").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph barangayh = new Paragraph("Barangay").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph addressh = new Paragraph("Complete Address").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph occupationh = new Paragraph("Occupation").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph locofworkh = new Paragraph("Location of Work").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph householdh = new Paragraph("No. of Person In Household").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph healthcondh = new Paragraph("Other Health Condition").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph swabh = new Paragraph("Date of Positive Swab Test").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph pwdh = new Paragraph("Is Patient a PWD?").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph disabilityh = new Paragraph("Type of Disability").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph seniorh = new Paragraph("Is Patient a Senior Citizen?").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph vaxstath = new Paragraph("Vaccine Status").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph clcontacth = new Paragraph("Close Contacts").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph statush = new Paragraph("Status").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph puserh = new Paragraph("Patient Username").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph ppassh = new Paragraph("Patient Password").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph prefh = new Paragraph("Patient Reference Number").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph dateaddedh = new Paragraph("Date Added").setBold().setTextAlignment(TextAlignment.CENTER);
        Paragraph dateendedh = new Paragraph("Date Ended").setBold().setTextAlignment(TextAlignment.CENTER);

        Cell pnum = new Cell();
        pnum.add(pnumh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell fname = new Cell();
        fname.add(fnameh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell mname = new Cell();
        mname.add(mnameh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell lname = new Cell();
        lname.add(lnameh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell age = new Cell();
        age.add(ageh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell birthday = new Cell();
        birthday.add(birthdayh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell sex = new Cell();
        sex.add(sexh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell contactnumber = new Cell();
        contactnumber.add(contactnumh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell email = new Cell();
        email.add(emailh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell barangay = new Cell();
        barangay.add(barangayh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell address = new Cell();
        address.add(addressh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell occupation = new Cell();
        occupation.add(occupationh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell locofwork = new Cell();
        locofwork.add(locofworkh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell household = new Cell();
        household.add(householdh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell healthcond = new Cell();
        healthcond.add(healthcondh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell swab = new Cell();
        swab.add(swabh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell pwd = new Cell();
        pwd.add(pwdh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell disability = new Cell();
        disability.add(disabilityh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell senior = new Cell();
        senior.add(seniorh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell vaxstat = new Cell();
        vaxstat.add(vaxstath).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell clcontact = new Cell();
        clcontact.add(clcontacth).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell status = new Cell();
        status.add(statush).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell puser = new Cell();
        puser.add(puserh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell ppass = new Cell();
        ppass.add(ppassh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell pref = new Cell();
        pref.add(prefh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell dateadded = new Cell();
        dateadded.add(dateaddedh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell dateended = new Cell();
        dateended.add(dateendedh).setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.addCell(pnum);
        table.addCell(fname);
        table.addCell(mname);
        table.addCell(lname);
        table.addCell(age);
        table.addCell(birthday);
        table.addCell(sex);
        table.addCell(contactnumber);
        table.addCell(email);
        table.addCell(barangay);
        table.addCell(address);
        table.addCell(occupation);
        table.addCell(locofwork);
        table.addCell(household);
        table.addCell(healthcond);
        table.addCell(swab);
        table.addCell(pwd);
        table.addCell(disability);
        table.addCell(senior);
        table.addCell(vaxstat);
        table.addCell(clcontact);
        table.addCell(status);
        table.addCell(puser);
        table.addCell(ppass);
        table.addCell(pref);
        table.addCell(dateadded);
        table.addCell(dateended);


        /*table.addCell("Raja Ram");
        table.addCell("32");

        table.addCell("Cj Mateo");
        table.addCell("32");*/
        patients.orderByChild("patientnum").equalTo(patientnum1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                            /*.value(String.valueOf(myDataSnapshot.child("invoiceNo").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("customerName").getValue()))
                           .value(datePatternFormat.format(myDataSnapshot.child("date").getValue()))
                           .value(timePatternFormat.format(myDataSnapshot.child("date").getValue()))
                           .value(String.valueOf(myDataSnapshot.child("amount").getValue()))*/
                            Paragraph pnumr = new Paragraph(myDataSnapshot.child("patientnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pnum1 = new Cell();
                            pnum1.add(pnumr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pnum1);


                            Paragraph fnamer = new Paragraph(myDataSnapshot.child("firstname").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell fname1 = new Cell();
                            fname1.add(fnamer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(fname1);

                            Paragraph mnamer = new Paragraph(myDataSnapshot.child("middlename").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell mname1 = new Cell();
                            mname1.add(mnamer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(mname1);

                            Paragraph lnamer = new Paragraph(myDataSnapshot.child("lastname").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell lname1 = new Cell();
                            lname1.add(lnamer).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(lname1);

                            Paragraph ager = new Paragraph(myDataSnapshot.child("age").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell age1 = new Cell();
                            age1.add(ager).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(age1);


                            Paragraph birthdayr = new Paragraph(myDataSnapshot.child("birthday").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell birthday1 = new Cell();
                            birthday1.add(birthdayr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(birthday1);

                            Paragraph sexr = new Paragraph(myDataSnapshot.child("sex").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell sex1 = new Cell();
                            sex1.add(sexr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(sex1);

                            Paragraph contactr = new Paragraph(myDataSnapshot.child("contactnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell contact1 = new Cell();
                            contact1.add(contactr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(contact1);

                            Paragraph emailr = new Paragraph(myDataSnapshot.child("email").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell email1 = new Cell();
                            email1.add(emailr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(email1);

                            Paragraph barangayr = new Paragraph(myDataSnapshot.child("barangay").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell barangay1 = new Cell();
                            barangay1.add(barangayr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(barangay1);

                            Paragraph addressr = new Paragraph(myDataSnapshot.child("address").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell address1 = new Cell();
                            address1.add(addressr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(address1);


                            Paragraph occupationr = new Paragraph(myDataSnapshot.child("occupation").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell occupation1 = new Cell();
                            occupation1.add(occupationr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(occupation1);

                            Paragraph locofworkr = new Paragraph(myDataSnapshot.child("locofwork").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell locofwork1 = new Cell();
                            locofwork1.add(locofworkr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(locofwork1);

                            Paragraph householdr = new Paragraph(myDataSnapshot.child("household").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell household1 = new Cell();
                            household1.add(householdr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(household1);

                            Paragraph healthcondr = new Paragraph(myDataSnapshot.child("healthcond").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell healthcond1 = new Cell();
                            healthcond1.add(healthcondr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(healthcond1);

                            Paragraph swabr = new Paragraph(myDataSnapshot.child("swab").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell swab1 = new Cell();
                            swab1.add(swabr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(swab1);

                            Paragraph pwdr = new Paragraph(myDataSnapshot.child("pwd").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pwd1 = new Cell();
                            pwd1.add(pwdr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pwd1);

                            Paragraph disablityr = new Paragraph(myDataSnapshot.child("disability").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell disablity1 = new Cell();
                            disablity1.add(disablityr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(disablity1);

                            Paragraph seniorr = new Paragraph(myDataSnapshot.child("senior").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell senior1 = new Cell();
                            senior1.add(seniorr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(senior1);

                            Paragraph vaccinestatr = new Paragraph(myDataSnapshot.child("vaccinestat").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell vaccinestat1 = new Cell();
                            vaccinestat1.add(vaccinestatr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(vaccinestat1);

                            Paragraph closecontactr = new Paragraph(myDataSnapshot.child("closecontact").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell closecontact1 = new Cell();
                            closecontact1.add(closecontactr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(closecontact1);

                            Paragraph statusr = new Paragraph(myDataSnapshot.child("status").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell status1 = new Cell();
                            status1.add(statusr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(status1);

                            Paragraph userr = new Paragraph(myDataSnapshot.child("user").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell user1 = new Cell();
                            user1.add(userr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(user1);

                            Paragraph passr = new Paragraph(myDataSnapshot.child("pass").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pass1 = new Cell();
                            pass1.add(passr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pass1);

                            Paragraph prefr = new Paragraph(myDataSnapshot.child("refnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell pref1 = new Cell();
                            pref1.add(prefr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(pref1);

                            Paragraph dateaddedr = new Paragraph(myDataSnapshot.child("dateadded").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell dateadded1 = new Cell();
                            dateadded1.add(dateaddedr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(dateadded1);

                            Paragraph dateendedr = new Paragraph(myDataSnapshot.child("dateended").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell dateended1 = new Cell();
                            dateended1.add(dateendedr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(dateended1);
                        }
                        documentdismissed.add(table);
                        Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                        documentdismissed.add(preparedby);
                        documentdismissed.add(prepname);
                        documentdismissed.add(date);
                        documentdismissed.close();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
