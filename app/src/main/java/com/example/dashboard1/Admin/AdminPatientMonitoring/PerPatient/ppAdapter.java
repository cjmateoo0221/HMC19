package com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import static com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient.Admin_PerPatientMonitor.context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.deeplabstudio.fcmsend.FCMSend;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientView.EndedFragment;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
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

public class ppAdapter extends FirebaseRecyclerAdapter <ppModel, ppAdapter.myViewHolder>{
    String pToken;
    String patientnum;
    String monitoringdate;
    DateFormat df = new SimpleDateFormat("MM.dd.yyyy 'at' hh:mm:ss aa");
    String datef= df.format(Calendar.getInstance().getTime());
    String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    public static String serverKey = "AAAANWe06Ic:APA91bFiJzEsVjD9EZaYxytLPKPxI2S7CNhTKZPO9J9P_51RXWdxpQY-e5pc-70fvSQpgNqUW0wkLupvedA15lcRb1v8RoqagstPnm1iNLE-92fRWIswOUQ1ahnbClSSXgdRNpZPiz10";
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ppAdapter(@NonNull FirebaseRecyclerOptions<ppModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull ppModel model) {
        String answered = model.getAnswered();
        patientnum = model.getPatientnum();
        monitoringdate = model.getDateofmonitor();
        holder.cough.setText(model.getCough());
        holder.dateofmonitor.setText(model.getDateofmonitor());
        holder.diarrhea.setText(model.getDiarrhea());
        holder.fatigue.setText(model.getFatigue());
        holder.headache.setText(model.getHeadache());
        holder.jointpain.setText(model.getJointpain());
        holder.shortness.setText(model.getShortness());
        holder.sorethroat.setText(model.getSorethroat());
        holder.temp.setText(model.getTemp());
        holder.vomitting.setText(model.getVomit());
        //Get Token
        FirebaseDatabase.getInstance().getReference().child("patients").child(patientnum)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pToken = snapshot.child("patienttoken").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.btnsmtoPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_monitoring_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView cough, dateofmonitor, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, temp, vomitting;
        Button btnsmtoPDF;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cough = (TextView) itemView.findViewById(R.id.tv_cough);
            dateofmonitor = (TextView) itemView.findViewById(R.id.tv_dateofmonitor);
            diarrhea = (TextView) itemView.findViewById(R.id.tv_diarrhea);
            fatigue = (TextView) itemView.findViewById(R.id.tv_fatigue);
            headache = (TextView) itemView.findViewById(R.id.tv_headache);
            jointpain = (TextView) itemView.findViewById(R.id.tv_jointpain);
            shortness = (TextView) itemView.findViewById(R.id.tv_shortness);
            sorethroat = (TextView) itemView.findViewById(R.id.tv_sorethroat);
            temp = (TextView) itemView.findViewById(R.id.tv_temp);
            vomitting = (TextView) itemView.findViewById(R.id.tv_vomit);
            btnsmtoPDF = (Button) itemView.findViewById(R.id.btnsmtoPDF);
        }
    }
    public void logs(){
        Map<String, Object> map = new HashMap<>();
        map.put("date", datef);
        map.put("action", "Sent a Notification to Patient" +patientnum);
        FirebaseDatabase.getInstance().getReference().child("adminlogs").push().setValue(map);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0){
            Toast.makeText(context, "No records found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    private void createSMtoPDF() throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath, patientnum+"Monitoring Data" +monitoringdate+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1400, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pmdata = new Paragraph("Patient Monitoring Data").setBold().setFontSize(30).setTextAlignment(TextAlignment.CENTER);
        document.add(pmdata);
        float columnwidth[] = {100f, 100f, 100f, 100f, 100f, 100f,100f,100f,100f,100f,100f,100f,100f};
        Table table = new Table(columnwidth);
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

        FirebaseDatabase.getInstance().getReference().child("patientmonitoring").child(monitoringdate+patientnum)
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
                        document.close();
                        Toast.makeText(context, "Pdf Created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
