package com.example.dashboard1.Admin.AdminPatientMonitoring;

import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.context;
import static com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity.stat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deeplabstudio.fcmsend.FCMSend;
import com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient.Admin_newPerPatientMonitor;
import com.example.dashboard1.Admin.AdminSettings;
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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
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

public class notAnsAdapter extends FirebaseRecyclerAdapter<notAnsModel, notAnsAdapter.myViewHolder> {
    CountDownTimer cTimer = null;
    String patientnum1;
    float columnwidth[] = {100f, 100f, 100f, 100f, 100f,100f,100f,100f,100f,100f,100f,100f};
    Table table = new Table(columnwidth);
    String pdfpnumm;
    String barangay;
    String status;
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
    Calendar calendar = Calendar.getInstance();
    int month = calendar.get(Calendar.MONTH);
    String patienttoken;
    final Loadingdialog1 loadingdialog1 = new Loadingdialog1(context);
    String prefnum, pemail;
    DateFormat df = new SimpleDateFormat("MM.dd.yyyy 'at' hh:mm:ss aa");
    String date1 = df.format(Calendar.getInstance().getTime());
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    public static String serverKey = "AAAANWe06Ic:APA91bFiJzEsVjD9EZaYxytLPKPxI2S7CNhTKZPO9J9P_51RXWdxpQY-e5pc-70fvSQpgNqUW0wkLupvedA15lcRb1v8RoqagstPnm1iNLE-92fRWIswOUQ1ahnbClSSXgdRNpZPiz10";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public notAnsAdapter(@NonNull FirebaseRecyclerOptions<notAnsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull notAnsModel model) {
        String fullname = model.getFirstname() + " " +model.getLastname();
        holder.not_pnum.setText(model.getPatientnum());
        holder.not_pname.setText(fullname);
        holder.not_contactnum.setText("09"+model.getContactnum());
        status = model.getCurrentStatus();
        if(status.equals(date+"Mild") || status.equals(date+"Severe") || status.equals(date+"No Symptom") || status.equals(date+"Moderate")){
            holder.img_answered.setImageResource(R.drawable.not_done);
            holder.btn_notify.setEnabled(false);
        }else{
            holder.img_answered.setImageResource(R.drawable.not_not);
            holder.btn_notify.setEnabled(true);
        }
        holder.btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.not_pnum.getContext());
                builder.setTitle("Send a notification to " + holder.not_pname.getText().toString() + " ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        patients.child(holder.not_pnum.getText().toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        patienttoken = snapshot.child("patienttoken").getValue().toString();
                                        FCMSend.Builder build = new FCMSend.Builder(patienttoken)
                                                .setTitle("MHO Admin")
                                                .setBody("Please answer the checklist for today. Thank you so much!");
                                        build.send().Result();
                                        Toast.makeText(holder.not_pname.getContext(), "A notification has been sent to the patient.", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notans_item, parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView not_pnum, not_pname, not_contactnum;
        ImageView img_answered;
        Button btn_notify;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            not_contactnum = itemView.findViewById(R.id.not_contactnum);
            not_pnum = itemView.findViewById(R.id.not_pnum);
            not_pname = itemView.findViewById(R.id.not_pname);
            img_answered = itemView.findViewById(R.id.img_answered);
            btn_notify = itemView.findViewById(R.id.btn_notify);

        }
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
}
