package com.example.dashboard1.Admin;

import static com.example.dashboard1.Admin.AdminPatientView.OngoingFragment.context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.adminlogs.Admin_Logs;
import com.example.dashboard1.Admin.adminviewplogs.Admin_selectbrgyplogs;
import com.example.dashboard1.Admin.adminviewplogs.Admin_selectpforlogs;
import com.example.dashboard1.Admin.reactivate.Admin_finalreactSelectBrgy;
import com.example.dashboard1.Loadingdialog;
import com.example.dashboard1.R;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
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

public class AdminSettings extends AppCompatActivity {
    ListView listView;
    final Loadingdialog loadingdialog = new Loadingdialog(AdminSettings.this);
    String pass;
    ImageView img_export, img_logs;
    int delay = 2 * 1000;
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    boolean result;
    ArrayAdapter<String> exportItems;
    ArrayAdapter<String> logItems;
    String slexport, sllogs;
    String[] logs = {"Admin Logs", "Patient Logs"};
    String[] export = {"Export all Patient Data to PDF", "Export all Active Patient Data to PDF", "Export all Concluded Patient Data to PDF"};
    String programName[] = {"Edit Password", "Export all Patient Data to PDF", "Export all Active Patient Data to PDF", "Export all Concluded Patient Data to PDF", "View Monthly Graphical Data of Active and Concluded Patients", "Admin Logs", "Patient Logs", "Reactivate Account"};
    int programImages[] = {R.drawable.ic_lock, R.drawable.pdficon, R.drawable.pdficon, R.drawable.pdficon, R.drawable.bargraph, R.drawable.logsadmin, R.drawable.logsadmin, R.drawable.reactivate_icon};
    Handler handler = new Handler();
    Runnable runnable;
    AlertDialog.Builder builder1;
    AutoCompleteTextView act_export, act_logs;
    LinearLayout ll_export, ll_logs, ll_viewgraph, ll_editpass, ll_reactivate;
    TextInputLayout input_export, input_logs;
    AlertDialog alert11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        builder1 = new AlertDialog.Builder(this);
        initinternetdia();
        result = isOnline();
        listView = findViewById(R.id.lv_settings);
        ProgramAdapter programAdapter = new ProgramAdapter(this, programName, programImages);
        listView.setAdapter(programAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseDatabase.getInstance().getReference().child("login").child("mhoadmin")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pass = snapshot.child("password").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        ll_export = findViewById(R.id.ll_export);
        ll_reactivate = findViewById(R.id.ll_reactivate);
        ll_editpass = findViewById(R.id.ll_editpass);
        ll_viewgraph = findViewById(R.id.ll_viewgraph);
        act_export = findViewById(R.id.act_exportpdf);
        act_logs = findViewById(R.id.act_logs);
        ll_logs = findViewById(R.id.ll_logs);
        input_export = findViewById(R.id.input_exportpdf);
        input_logs   = findViewById(R.id.input_logs);
        img_logs = findViewById(R.id.img_logs);
        img_export = findViewById(R.id.img_export);


        exportItems = new ArrayAdapter<String>(this,R.layout.export_item,export);
        logItems = new ArrayAdapter<String>(this,R.layout.log_item,logs);

        act_export.setAdapter(exportItems);
        act_export.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                slexport = parent.getItemAtPosition(i).toString();

                if(slexport.equals("Export all Patient Data to PDF")){
                    act_export.setText("Select",false);
                    input_export.setVisibility(View.GONE);
                    img_export.setImageResource(R.drawable.arrow_down);
                    final DialogPlus diaall = DialogPlus.newDialog(AdminSettings.this)
                            .setContentHolder(new ViewHolder(R.layout.export_popup))
                            .setExpanded(true, 1000)
                            .create();
                    View v = diaall.getHolderView();
                    TextView txt_exportop = v.findViewById(R.id.txt_exportop);
                    EditText et_fullname = v.findViewById(R.id.et_fullname);
                    Button btn_exportstor = v.findViewById(R.id.btn_exportstor);
                    Button btn_exportemail = v.findViewById(R.id.btn_exportemail);
                    txt_exportop.setText(slexport);
                    diaall.show();
                    btn_exportstor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(et_fullname.getText().toString().length() == 0) {
                                et_fullname.setError("Fullname is Required");
                                et_fullname.requestFocus();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String fullname = et_fullname.getText().toString();
                                        try {
                                            createPDFallpatient(fullname);
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
                                    createPDFallpatient(fullname);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export?");
                                builder.setMessage("Data will be saved in InternalStorage/Downloads and will be emailed to the admin email.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        diaall.dismiss();
                                        loadingdialog.showLoading();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailallpdf();
                                                loadingdialog.disMiss();
                                                Toast.makeText(AdminSettings.this, "Successfully Sent!", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 1000);
                                       /* String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/PatientInformation.pdf";
                                        File file = new File(pdfpath);

                                        Intent share = new Intent();

                                        share.setAction(Intent.ACTION_SEND);
                                        share.putExtra(Intent.EXTRA_SUBJECT, "Patient Information");
                                        share.putExtra(Intent.EXTRA_TEXT, "Attach to this email is a PDF file containing Patient Information");
                                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                        share.setType("application/pdf");
                                        startActivity(share);*/
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

                }else if (slexport.equals("Export all Active Patient Data to PDF")){
                    act_export.setText("Select",false);
                    input_export.setVisibility(View.GONE);
                    img_export.setImageResource(R.drawable.arrow_down);
                    final DialogPlus diaactive = DialogPlus.newDialog(AdminSettings.this)
                            .setContentHolder(new ViewHolder(R.layout.export_popup))
                            .setExpanded(true, 1000)
                            .create();
                    View v = diaactive.getHolderView();
                    TextView txt_exportop = v.findViewById(R.id.txt_exportop);
                    EditText et_fullname = v.findViewById(R.id.et_fullname);
                    Button btn_exportstor = v.findViewById(R.id.btn_exportstor);
                    Button btn_exportemail = v.findViewById(R.id.btn_exportemail);
                    txt_exportop.setText(slexport);
                    diaactive.show();
                    btn_exportstor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(et_fullname.getText().toString().length() == 0) {
                                et_fullname.setError("Fullname is Required");
                                et_fullname.requestFocus();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String fullname = et_fullname.getText().toString();
                                        try {
                                            createPDFactivePatient(fullname);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        diaactive.dismiss();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        diaactive.dismiss();
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
                                    createPDFactivePatient(fullname);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export?");
                                builder.setMessage("Data will be saved in InternalStorage/Downloads and will be emailed to the admin email.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        diaactive.dismiss();
                                        loadingdialog.showLoading();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailactivepdf();
                                                loadingdialog.disMiss();
                                                Toast.makeText(AdminSettings.this, "Successfully Sent!", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 1000);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        diaactive.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }
                    });
                }else if (slexport.equals("Export all Concluded Patient Data to PDF")){
                    act_export.setText("Select",false);
                    input_export.setVisibility(View.GONE);
                    img_export.setImageResource(R.drawable.arrow_down);
                    final DialogPlus diacon = DialogPlus.newDialog(AdminSettings.this)
                            .setContentHolder(new ViewHolder(R.layout.export_popup))
                            .setExpanded(true, 1000)
                            .create();
                    View v = diacon.getHolderView();
                    TextView txt_exportop = v.findViewById(R.id.txt_exportop);
                    EditText et_fullname = v.findViewById(R.id.et_fullname);
                    Button btn_exportstor = v.findViewById(R.id.btn_exportstor);
                    Button btn_exportemail = v.findViewById(R.id.btn_exportemail);
                    txt_exportop.setText(slexport);
                    diacon.show();
                    btn_exportstor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(et_fullname.getText().toString().length() == 0) {
                                et_fullname.setError("Fullname is Required");
                                et_fullname.requestFocus();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String fullname = et_fullname.getText().toString();
                                        try {
                                            createPDFconcludedPatient(fullname);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        diacon.dismiss();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        diacon.dismiss();
                                    }
                                });
                                builder.show();
                            }

                        }
                    });
                    btn_exportemail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(et_fullname.getText().toString().length() == 0) {
                                et_fullname.setError("Fullname is Required");
                                et_fullname.requestFocus();
                            }else{
                                String fullname = et_fullname.getText().toString();
                                try {
                                    createPDFconcludedPatient(fullname);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                                builder.setTitle("Confirm Export?");
                                builder.setMessage("Data will be saved in InternalStorage/Downloads and will be emailed to the admin email.");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        diacon.dismiss();
                                        loadingdialog.showLoading();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                emailconcludedpdf();
                                                loadingdialog.disMiss();
                                                Toast.makeText(AdminSettings.this, "Successfully Sent!", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 1000);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        diacon.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }
                    });
                }
            }
        });



        act_logs.setAdapter(logItems);
        act_logs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                sllogs = parent.getItemAtPosition(i).toString();
                if(sllogs.equals("Admin Logs")){
                    startActivity(new Intent(AdminSettings.this, Admin_Logs.class));
                }else{
                    startActivity(new Intent(AdminSettings.this, Admin_selectbrgyplogs.class));
                }
                finish();
            }
        });

        ll_editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(AdminSettings.this)
                        .setContentHolder(new ViewHolder(R.layout.edit_password))
                        .setExpanded(true, 1000)
                        .create();
                View v = dialogPlus.getHolderView();
                EditText curpass = v.findViewById(R.id.curpass);
                EditText newpass = v.findViewById(R.id.newpass);
                Button btncf = v.findViewById(R.id.btncf);
                dialogPlus.show();
                btncf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!curpass.getText().toString().trim().equals(pass)){
                            Toast.makeText(AdminSettings.this, "Incorrect Password. Please try Again", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        }else{
                            Toast.makeText(AdminSettings.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            Map<String, Object> map = new HashMap<>();
                            map.put("password", newpass.getText().toString());
                            FirebaseDatabase.getInstance().getReference("login").child("mhoadmin")
                                    .updateChildren(map);
                            dialogPlus.dismiss();
                        }
                    }
                });
            }
        });

        ll_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_export.getVisibility() == View.GONE){
                    input_export.setVisibility(View.VISIBLE);
                    img_export.setImageResource(R.drawable.arrow_up);
                }else{
                    input_export.setVisibility(View.GONE);
                    img_export.setImageResource(R.drawable.arrow_down);
                }
            }
        });

        ll_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_logs.getVisibility() == View.GONE){
                    input_logs.setVisibility(View.VISIBLE);
                    img_logs.setImageResource(R.drawable.arrow_up);
                }else{
                    input_logs.setVisibility(View.GONE);
                    img_logs.setImageResource(R.drawable.arrow_down);
                }
            }
        });

        ll_viewgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSettings.this , ChartExample.class));
                finish();
            }
        });

        ll_reactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminSettings.this , Admin_finalreactSelectBrgy.class));
                finish();
            }
        });
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0 ){

                    final DialogPlus dialogPlus = DialogPlus.newDialog(AdminSettings.this)
                            .setContentHolder(new ViewHolder(R.layout.edit_password))
                            .setExpanded(true, 700)
                            .create();
                    View v = dialogPlus.getHolderView();
                    EditText curpass = v.findViewById(R.id.curpass);
                    EditText newpass = v.findViewById(R.id.newpass);
                    Button btncf = v.findViewById(R.id.btncf);
                    dialogPlus.show();
                    btncf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!curpass.getText().toString().trim().equals(pass)){
                                Toast.makeText(AdminSettings.this, "Incorrect Password. Please try Again", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }else{
                                Toast.makeText(AdminSettings.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                Map<String, Object> map = new HashMap<>();
                                map.put("password", newpass.getText().toString());
                                FirebaseDatabase.getInstance().getReference("login").child("mhoadmin")
                                        .updateChildren(map);
                                dialogPlus.dismiss();
                            }
                        }
                    });
                }else if(i == 5){
                    startActivity(new Intent(AdminSettings.this, Admin_Logs.class));
                    finish();
                }else if (i == 1){

                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                    builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    createPDFallpatient();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }else if (i == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                    builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                createPDFactivePatient();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }else if (i == 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminSettings.this);
                    builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                createPDFconcludedPatient();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }else if (i == 4){
                    startActivity(new Intent(AdminSettings.this , ChartExample.class));
                    finish();
                }else if (i == 7){
                    startActivity(new Intent(AdminSettings.this , Admin_finalreactSelectBrgy.class));
                    finish();
                }else if (i == 6){
                    startActivity(new Intent(AdminSettings.this, Admin_selectbrgyplogs.class));
                    finish();

                }
            }
        });*/
    }

    private void createPDFconcludedPatient(String fullname) throws FileNotFoundException {
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, "ConcludedPatientInformation.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1800, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pdata = new Paragraph("Patient Information").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(this, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        document.add(image);
        document.add(pdata);

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
        FirebaseDatabase.getInstance().getReference().child("patientdismissed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
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
                            document.add(table);
                            Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            document.add(preparedby);
                            document.add(prepname);
                            document.add(date);
                            document.close();
                            Toast.makeText(AdminSettings.this, "PDF Created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }else{
                            Paragraph pdata = new Paragraph("No Data Found").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);
                            document.add(pdata);
                            document.close();
                            Toast.makeText(AdminSettings.this, "PDF Created with no Data: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createPDFactivePatient(String fullname) throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, "ActivePatientInformation.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1800, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pdata = new Paragraph("Patient Information").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(this, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        document.add(image);
        document.add(pdata);

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
        FirebaseDatabase.getInstance().getReference().child("patientongoingall")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
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
                            document.add(table);
                            Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            document.add(preparedby);
                            document.add(prepname);
                            document.add(date);
                            document.close();
                            Toast.makeText(AdminSettings.this, "PDF Created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }else{
                            Paragraph pdata = new Paragraph("No Data Found").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);
                            document.add(pdata);
                            document.close();
                            Toast.makeText(AdminSettings.this, "PDF Created with no Data: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }

    private void createPDFallpatient(String fullname) throws FileNotFoundException {
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, "PatientInformation.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1800, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pdata = new Paragraph("Patient Information").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(this, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        document.add(image);
        document.add(pdata);
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
        FirebaseDatabase.getInstance().getReference().child("patients")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
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
                            document.add(table);
                            Paragraph preparedby = new Paragraph("\n"+"\n"+"\n"+"\n"+"Prepared By:" + "\n").setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph prepname = new Paragraph(fullname).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            Paragraph date = new Paragraph(datetoday).setFontSize(20).setTextAlignment(TextAlignment.LEFT);
                            document.add(preparedby);
                            document.add(prepname);
                            document.add(date);
                            document.close();
                            Toast.makeText(AdminSettings.this, "PDF Created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }else{
                            Paragraph pdata = new Paragraph("No Data Found").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);
                            document.add(pdata);
                            Toast.makeText(AdminSettings.this, "PDF Created with no Data: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                        }

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
    private String getHtmlBody() {
        return "<h1><img width=\"100\" src=\"https://www.facebook.com/photo.php?fbid=1058926111382697&set=pb.100017959542600.-2207520000..&type=3\">"+"</h1>";
    }
    private void emailallpdf(){
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
            String htmlText = "<img src=\"cid:image\"><p><br><br>Attach to this email is a PDF file of the Patient Information. Thank you! <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


                MimeBodyPart messageBodyPart2 = new MimeBodyPart();

                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/PatientInformation.pdf";//change accordingly
                DataSource source = new FileDataSource(filename);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName("PatientInformation.pdf");

            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(messageBodyPart2);
            // put everything together
            message.setContent(multipart);
            
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            Toast.makeText(AdminSettings.this, "Successfully Sent!", Toast.LENGTH_SHORT).show();
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

    private void emailactivepdf(){
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
            String htmlText = "<img src=\"cid:image\"><p><br><br>Attach to this email is a PDF file of the Active Patient Information. Thank you! <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/ActivePatientInformation.pdf";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName("ActivePatientInformation.pdf");

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

    private void emailconcludedpdf(){
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
            String htmlText = "<img src=\"cid:image\"><p><br><br>Attach to this email is a PDF file of the Concluded Patient Information. Thank you! <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/ConcludedPatientInformation.pdf";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName("ConcludedPatientInformation.pdf");

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

}