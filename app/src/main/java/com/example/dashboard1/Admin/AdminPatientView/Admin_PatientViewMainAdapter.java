package com.example.dashboard1.Admin.AdminPatientView;


import android.app.Activity;
import android.app.AlertDialog;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_PatientViewActivity.act;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import static com.example.dashboard1.Admin.AdminPatientView.Admin_nvPatient.context;
import static com.example.dashboard1.Admin.AdminPatientView.OngoingFragment.nodata;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dashboard1.Admin.AdminPatientMonitoring.Admin_PatientMonitoringActivity;
import com.example.dashboard1.Admin.AdminPatientMonitoring.MonitorAdapter;
import com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient.Admin_PerPatientMonitor;
import com.example.dashboard1.Admin.AdminSettings;
import com.example.dashboard1.Loadingdialog1;
import com.example.dashboard1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.itextpdf.layout.property.HorizontalAlignment;
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
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
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

public class Admin_PatientViewMainAdapter extends FirebaseRecyclerAdapter<Admin_PatientViewDataModel, Admin_PatientViewMainAdapter.myViewholder>{
    RadioGroup sex;
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    Button birthdate, swab;
    String[] status = {"Vaccinated", "Not Vaccinated"};
    String slstatus;
    final Loadingdialog1 loadingdialog1 = new Loadingdialog1(context);
    String patientnum;
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextView;
    DatePickerDialog birthday, swabp;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Admin_PatientViewMainAdapter(@NonNull FirebaseRecyclerOptions<Admin_PatientViewDataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, final int position, @NonNull Admin_PatientViewDataModel model) {
        holder.patientnum.setText(model.getPatientnum());
        holder.barangay.setText(model.getBarangay());
        holder.firstname.setText(model.getFirstname());
        holder.infected.setText(model.getNoofinfect());
        holder.lastname.setText(model.getLastname());
        holder.middlename.setText(model.getMiddlename());
        holder.disability.setText(model.getDisability());
        holder.pwd.setText(model.getPwd());
        holder.senior.setText(model.getSenior());
        holder.refnum.setText(model.getRefnum());
        holder.age.setText(model.getAge());
        holder.sex.setText(model.getSex());
        holder.dateadded.setText(model.getDateadded());
        holder.dateended.setText(model.getDateended());
        holder.contactnum.setText("09"+model.getContactnum());
        holder.email.setText(model.getEmail());
        holder.address.setText(model.getAddress());
        holder.occupation.setText(model.getOccupation());
        holder.locofwork.setText(model.getLocofwork());
        holder.household.setText(model.getHousehold());
        holder.healthcond.setText(model.getHealthcond());
        holder.swab.setText(model.getSwab());
        holder.vaccinestat.setText(model.getVaccinestat());
        holder.closecontact.setText(model.getClosecontact());
        holder.birthday.setText(model.getBirthday());
        holder.pass.setText(model.getPass());
        holder.user.setText(model.getUser());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (holder.patientnum.getContext(), Admin_EditPatient.class);
                intent.putExtra("pnum", holder.patientnum.getText().toString());
                intent.putExtra("patientnum", holder.patientnum.getText().toString());
                intent.putExtra("barangay", holder.barangay.getText().toString());
                intent.putExtra("from", act);
                //intent.putExtra("name", holder.name.getText().toString());
                holder.patientnum.getContext().startActivity(intent);
            }
        });

        holder.btnexportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientnum = holder.patientnum.getText().toString();
                final DialogPlus diaall = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.export_patientpopup))
                        .setExpanded(true, 1000)
                        .create();
                View v = diaall.getHolderView();
                EditText et_fullname = v.findViewById(R.id.et_fullname);
                Button btn_exportstor = v.findViewById(R.id.btn_exportstor);
                Button btn_exportemail = v.findViewById(R.id.btn_exportemail);;
                diaall.show();
                btn_exportstor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_fullname.getText().toString().length() == 0) {
                            et_fullname.setError("Fullname is Required");
                            et_fullname.requestFocus();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirm Export? Data will be saved in InternalStorage/Downloads.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String fullname = et_fullname.getText().toString();
                                    try {
                                        createPDFthisPatient(fullname);
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
                                createPDFthisPatient(fullname);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirm Export?");
                            builder.setMessage("Data will be saved in InternalStorage/Downloads and you will be sent to the admin email.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    diaall.dismiss();
                                    loadingdialog1.showLoading();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            emailthispdf(patientnum);
                                            loadingdialog1.disMiss();
                                            Toast.makeText(context, "Successfully Sent!", Toast.LENGTH_SHORT).show();
                                        }
                                    }, 1000);

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
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_viewing_item, parent,false);
        return new Admin_PatientViewMainAdapter.myViewholder(view);
    }

    class myViewholder extends RecyclerView.ViewHolder{
        TextView infected, firstname , middlename, lastname, barangay, pwd, disability, senior, refnum, dateadded, dateended, address, age, closecontact, email, healthcond, household, locofwork, name, occupation, pass, patientnum, sex, swab, user, vaccinestat, contactnum, birthday;
        Button btnEdit, btnexportPDF;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            firstname = (TextView)  itemView.findViewById(R.id.tvf_firstname);
            infected = (TextView)  itemView.findViewById(R.id.tvf_infected);
            lastname = (TextView)  itemView.findViewById(R.id.tvf_lastname);
            middlename = (TextView)  itemView.findViewById(R.id.tvf_middlename);
            barangay = (TextView)  itemView.findViewById(R.id.tvf_barangay);
            pwd = (TextView) itemView.findViewById(R.id.tvf_pwd);
            disability = (TextView) itemView.findViewById(R.id.tvf_disability);
            senior = (TextView) itemView.findViewById(R.id.tvf_senior);
            refnum = (TextView) itemView.findViewById(R.id.tvf_ref);
            dateadded = (TextView) itemView.findViewById(R.id.tvf_dateadd);
            dateended = (TextView) itemView.findViewById(R.id.tvf_dateend);
            address = (TextView) itemView.findViewById(R.id.tvf_address);
            age = (TextView) itemView.findViewById(R.id.tvf_age);
            closecontact = (TextView) itemView.findViewById(R.id.tvf_clcontact);
            email = (TextView) itemView.findViewById(R.id.tvf_email);
            healthcond = (TextView) itemView.findViewById(R.id.tvf_healthcond);
            household = (TextView) itemView.findViewById(R.id.tvf_household);
            locofwork = (TextView) itemView.findViewById(R.id.tvf_locofwork);
            occupation = (TextView) itemView.findViewById(R.id.tvf_occupation);
            pass = (TextView) itemView.findViewById(R.id.tvf_ppass);
            patientnum = (TextView) itemView.findViewById(R.id.tvf_patient);
            sex = (TextView) itemView.findViewById(R.id.tvf_sex);
            swab = (TextView) itemView.findViewById(R.id.tvf_swab);
            user = (TextView) itemView.findViewById(R.id.tvf_puser);
            vaccinestat = (TextView) itemView.findViewById(R.id.tvf_vaccine);
            contactnum = (TextView) itemView.findViewById(R.id.tvf_contact);
            birthday = (TextView) itemView.findViewById(R.id.tvf_birthday);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnexportPDF = (Button) itemView.findViewById(R.id.btnePDF);
        }
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(month,day,year);
                birthdate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        birthday = new DatePickerDialog(birthdate.getContext(), style, dateSetListener, year, month, day);
        birthday.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initDatePicker2(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(month,day,year);
                swab.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;

        swabp = new DatePickerDialog(swab.getContext(), style, dateSetListener, year, month, day);
        swabp.getDatePicker().setMaxDate(System.currentTimeMillis());
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

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(getItemCount() == 0){
          nodata.setVisibility(View.VISIBLE);
        }else{
          nodata.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    private void createPDFthisPatient(String fullname) throws FileNotFoundException{
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath,  patientnum+ "Information.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Rectangle myPagesize = new Rectangle(1800, 1100);
        Document document = new Document(pdfDocument, new PageSize(myPagesize));
        Paragraph pdata = new Paragraph("Patient Information").setBold().setFontSize(50).setTextAlignment(TextAlignment.CENTER);

        Drawable d = ContextCompat.getDrawable(context, R.drawable.pdfbanner);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        document.add(image);
        document.add(pdata);
        float columnwidth[] = {200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
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
        Paragraph noofinfecth = new Paragraph("No. of times Infected").setBold().setTextAlignment(TextAlignment.CENTER);
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

        Cell noofinfect = new Cell();
        noofinfect.add(noofinfecth).setVerticalAlignment(VerticalAlignment.MIDDLE);

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
        table.addCell(noofinfect);
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
        FirebaseDatabase.getInstance().getReference().child("patients").orderByChild("patientnum").equalTo(patientnum)
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

                            Paragraph contactr = new Paragraph("09"+myDataSnapshot.child("contactnum").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
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

                            Paragraph noofinfectr = new Paragraph(myDataSnapshot.child("noofinfect").getValue().toString()).setTextAlignment(TextAlignment.CENTER);
                            Cell noofinfectr1 = new Cell();
                            noofinfectr1.add(noofinfectr).setVerticalAlignment(VerticalAlignment.MIDDLE);
                            table.addCell(noofinfectr1);

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
                        Toast.makeText(context, "PDF Created: InternalStorage/Downloads", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void emailthispdf(String patientnum){
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
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
            String htmlText = "<img src=\"cid:image\"><p><br><br>Attach to this email is a PDF file of "+patientnum+" Patient Information. Thank you! <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+patientnum+"Information.pdf";//change accordingly
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(patientnum + "Information.pdf");

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
