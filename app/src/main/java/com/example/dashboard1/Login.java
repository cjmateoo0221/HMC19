package com.example.dashboard1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dashboard1.Admin.AdminActivity;
import com.example.dashboard1.Patient.PatientActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
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
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import khttp.KHttp;

public class  Login extends AppCompatActivity {
    EditText edtUser,edtPass;
    Button btnLogin;
    int delay = 2 * 1000;
    boolean result;
    LinearLayout ll_user;
    DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss aa");
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String datetoday = df.format(Calendar.getInstance().getTime());
    String datewithtime = df1.format(Calendar.getInstance().getTime());
    Handler handler = new Handler();
    Runnable runnable;
    android.app.AlertDialog.Builder builder1;
    AlertDialog alert11;
    TextView forgotpass, ppass, pemail1, txt_username;
    ImageView img_usericon;
    String username;
    ImageView logo;
    final Loadingdialog loadingdialog = new Loadingdialog(Login.this);
    SharedPreferences usersp;
    String email;
    String pemail;
    String repass;
    String adminpass;
    String adminemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        ll_user = findViewById(R.id.ll_user);
        ppass = findViewById(R.id.ppass);
        pemail1 = findViewById(R.id.pemail1);
        logo = findViewById(R.id.logoofapp);
        txt_username = findViewById(R.id.txt_username);
        img_usericon = findViewById(R.id.img_usericon);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        forgotpass = findViewById(R.id.forgotPass);
        builder1 = new AlertDialog.Builder(this);
        //initinternetdia();
       // result = isOnline();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        builder.detectFileUriExposure();


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Confirm?");
                builder.setMessage("This will send the admin password data to the admin email associated with the admin account.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadingdialog.showLoading();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getadminpass();
                                loadingdialog.disMiss();
                            }
                        }, 1000);

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
              /*  final DialogPlus dialogPlus1 = DialogPlus.newDialog(Login.this)
                        .setContentHolder(new ViewHolder(R.layout.activity_forgot_admin_pass))
                        .setExpanded(true, 700)
                        .create();
                dialogPlus1.show();
                View v1 = dialogPlus1.getHolderView();
                EditText adminem = v1.findViewById(R.id.faemail);
                Button faconfirm = v1.findViewById(R.id.fabtnconfirm);

                faconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                    if(adminem.getText().toString(). length() == 0){
                                        adminem.setError("Email data is needed");
                                        Toast.makeText(Login.this, "Email data is needed", Toast.LENGTH_SHORT).show();
                                    }else if (isValidEmail(adminem.getText().toString()) == false){
                                        adminem.setError("Please enter a valid email");
                                        Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                    }else{
                                        adminemail = adminem.getText().toString();
                                        getadminpass();
                                        dialogPlus1.dismiss();
                                    }
                    }
                });
            }*/
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            final DialogPlus dialogPlus = DialogPlus.newDialog(Login.this)
                                    .setContentHolder(new ViewHolder(R.layout.activity_forgot_password))
                                    .setExpanded(true,700)
                                    .create();
                            dialogPlus.show();
                            View v = dialogPlus.getHolderView();
                            EditText name = v.findViewById(R.id.feuser);
                            Button btnconfirm = v.findViewById(R.id.btnconfirm);

                            btnconfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                                if(name.getText().toString().length() == 0){
                                                   name.setError("Username can't be empty");
                                                   Toast.makeText(Login.this, "Username can't be empty", Toast.LENGTH_SHORT).show();
                                               }else{
                                                    username = name.getText().toString();
                                                  loadingdialog.showLoading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            getPass();
                                                            loadingdialog.disMiss();
                                                        }
                                                    }, 1000);
                                                   // sendPassword();
                                                    //Toast.makeText(Login.this, pemail, Toast.LENGTH_SHORT).show();
                                                    dialogPlus.dismiss();
                                                }
                                }
                            });
                            /*final DialogPlus dialogPlus1 = DialogPlus.newDialog(Login.this)
                                    .setContentHolder(new ViewHolder(R.layout.activity_forgot_admin_pass))
                                    .setExpanded(true, 500)
                                    .create();
                            dialogPlus1.show();
                            View v1 = dialogPlus1.getHolderView();
                            EditText adminem = v1.findViewById(R.id.faemail);
                            Button faconfirm = v1.findViewById(R.id.fabtnconfirm);

                            faconfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loadingdialog.showLoading();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            boolean result = isOnline();
                                            if(result == false) {
                                                Toast.makeText(Login.this, "Not Connected. Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                if(adminem.getText().toString(). length() == 0){
                                                    adminem.setError("Email data is needed");
                                                    Toast.makeText(Login.this, "Email data is needed", Toast.LENGTH_SHORT).show();
                                                }else if (isValidEmail(adminem.getText().toString()) == false){
                                                    adminem.setError("Please enter a valid email");
                                                    Toast.makeText(Login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    adminemail = adminem.getText().toString();
                                                    getadminpass();
                                                    dialogPlus1.dismiss();
                                                }
                                            }
                                            loadingdialog.disMiss();
                                        }
                                    }, 1000);

                                }
                            });*/
            }
        });
        usersp = getSharedPreferences("user", Context.MODE_PRIVATE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkuser = usersp.getString("user","");
                if(checkuser.length() == 0){
                    loadingdialog.showLoading();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(Login.this, "Connected", Toast.LENGTH_SHORT).show();
                            if(edtUser.getText().toString().length() == 0){
                                edtUser.setError("Username can't be empty");
                                Toast.makeText(Login.this, "Username can't be empty", Toast.LENGTH_SHORT).show();
                            }else if (edtPass.getText().toString().length() == 0) {
                                edtPass.setError("Password can't be empty");
                                Toast.makeText(Login.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                            }else {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String user = edtUser.getText().toString();
                                        String pass = edtPass.getText().toString();

                                        if (dataSnapshot.child(user).exists()) {
                                            if (dataSnapshot.child(user).child("password").getValue(String.class).equals(pass)) {
                                                if (dataSnapshot.child(user).child("as").getValue(String.class).equals("admin")) {
                                                    preferences.setDataLogin(Login.this, true);
                                                    preferences.setDataAs(Login.this, "admin");
                                                    SharedPreferences.Editor editor = usersp.edit();
                                                    editor.putString("user",user);
                                                    editor.commit();
                                                    logsadmin();
                                                    startActivity(new Intent(Login.this, AdminActivity.class));
                                                } else if (dataSnapshot.child(user).child("as").getValue(String.class).equals("user")){
                                                    preferences.setDataLogin(Login.this, true);
                                                    preferences.setDataAs(Login.this, "user");
                                                    String patientnum = dataSnapshot.child(user).child("username").getValue().toString();
                                                    SharedPreferences.Editor editor = usersp.edit();
                                                    editor.putString("user",patientnum);
                                                    editor.commit();
                                                    logspatient();
                                                    startActivity(new Intent(Login.this, IntroActivity.class));
                                                }

                                            } else {
                                                Toast.makeText(Login.this, "You have entered a Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "Account not Found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            loadingdialog.disMiss();
                        }
                    }, 300);
                }else{
                    loadingdialog.showLoading();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           if (edtPass.getText().toString().length() == 0) {
                                edtPass.setError("Password can't be empty");
                                Toast.makeText(Login.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                            }else {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String user = txt_username.getText().toString();
                                        String pass = edtPass.getText().toString();

                                        if (dataSnapshot.child(user).exists()) {
                                            if (dataSnapshot.child(user).child("password").getValue(String.class).equals(pass)) {
                                                if (dataSnapshot.child(user).child("as").getValue(String.class).equals("admin")) {
                                                    preferences.setDataLogin(Login.this, true);
                                                    preferences.setDataAs(Login.this, "admin");
                                                    SharedPreferences.Editor editor = usersp.edit();
                                                    editor.putString("user",user);
                                                    editor.commit();
                                                    logsadmin();
                                                    startActivity(new Intent(Login.this, AdminActivity.class));
                                                    finish();
                                                } else if (dataSnapshot.child(user).child("as").getValue(String.class).equals("user")){
                                                    preferences.setDataLogin(Login.this, true);
                                                    preferences.setDataAs(Login.this, "user");
                                                    String patientnum = dataSnapshot.child(user).child("username").getValue().toString();
                                                    SharedPreferences.Editor editor = usersp.edit();
                                                    editor.putString("user",patientnum);
                                                    editor.commit();
                                                    logspatient();
                                                    startActivity(new Intent(Login.this, IntroActivity.class));
                                                    finish();
                                                }

                                            } else {
                                                Toast.makeText(Login.this, "You have entered a Wrong Password", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(Login.this, "Account not Found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            loadingdialog.disMiss();
                        }
                    }, 300);
                }
            }

        });

    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getDataLogin(this)) {
            if (preferences.getDataAs(this).equals("admin")) {
                edtUser.setVisibility(View.INVISIBLE);
                ll_user.setVisibility(View.VISIBLE);
                String user = usersp.getString("user","");
                txt_username.setText(user);
            } else {
                edtUser.setVisibility(View.INVISIBLE);
                ll_user.setVisibility(View.VISIBLE);
                String user = usersp.getString("user","");
                txt_username.setText(user);
            }
        }
    }
    public void getPass(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ppass.setText(snapshot.child("pass").getValue().toString());
                            getEmail();
                        }else if(!snapshot.exists()){
                            Toast.makeText(Login.this, "Credentials not found. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getadminpass(){
        FirebaseDatabase.getInstance().getReference().child("login").child("mhoadmin")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            adminpass = snapshot.child("password").getValue().toString();
                            sendadminPass();
                            Toast.makeText(Login.this, "Password Data has been sent to your Email.", Toast.LENGTH_SHORT).show();
                        }else if(!snapshot.exists()){
                            Toast.makeText(Login.this, "Credentials not found. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getEmail(){
        FirebaseDatabase.getInstance().getReference().child("patients").child(username)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            pemail1.setText(snapshot.child("email").getValue().toString());
                            sendPassword();
                            Toast.makeText(Login.this, "Password data has been sent to your email", Toast.LENGTH_SHORT).show();
                        }else if(!snapshot.exists()){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void sendadminPass(){
       // try {
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

            /*javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });*/
            try{

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(stringSenderEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                message.setSubject("Subject: Forgot admin password data");
                MimeMultipart multipart = new MimeMultipart("related");


                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = "<img src=\"cid:image\"><p><br><br>Hi! your admin password is " +adminpass + " Thank you. <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
                messageBodyPart.setContent(htmlText, "text/html");
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
                DataSource fds = new FileDataSource(image);
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setFileName("mhobanner(disregard)");
                messageBodyPart.setHeader("Content-ID", "<image>");


               /* MimeBodyPart messageBodyPart2 = new MimeBodyPart();

                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/ActivePatientInformation.pdf";//change accordingly
                DataSource source = new FileDataSource(filename);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName("ActivePatientInformation.pdf");*/

                multipart.addBodyPart(messageBodyPart);
                // put everything together
                message.setContent(multipart);


                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                Transport.send(message);
            }catch (MessagingException e){
                throw new RuntimeException(e);
            }
           /* Message message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
            message.setFrom(new InternetAddress(stringSenderEmail));

            message.setSubject("Subject: Forgot Password Data");

            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "TANGINAMOO!";
            messageBodyPart.setContent(htmlText, "text");
            // add it
            multipart.addBodyPart(messageBodyPart);
            //message.setText("Hi! your admin is");
            message.setContent(multipart);*/
           /* Thread thread = new Thread(new Runnable() {
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
        }*/
    }
    public void sendPassword(){
        try {
            String stringSenderEmail = "gmamho43@gmail.com";
            String stringReceiverEmail = pemail1.getText().toString();
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

            message.setSubject("Subject: Forgot password data");
            MimeMultipart multipart = new MimeMultipart("related");


            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<img src=\"cid:image\"><p><br><br>Hi! your password is " +ppass.getText().toString() + " Thank you. <br><br><br><br><br></p>   Regards, <h4> MHO Medical Staff </h4> <p>General Mariano Alvarez Municipal Hall, Congressional Road, 4117</p> " ;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/set.webp";
            DataSource fds = new FileDataSource(image);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setFileName("mhobanner(disregard)");
            messageBodyPart.setHeader("Content-ID", "<image>");


               /* MimeBodyPart messageBodyPart2 = new MimeBodyPart();

                String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/ActivePatientInformation.pdf";//change accordingly
                DataSource source = new FileDataSource(filename);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName("ActivePatientInformation.pdf");*/

            multipart.addBodyPart(messageBodyPart);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
  /* @Override
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
    }*/
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
   public void logsadmin(){
       Map<String, Object> map = new HashMap<>();
       map.put("date", datewithtime);
       map.put("action", "Logged in");
       FirebaseDatabase.getInstance().getReference().child("adminlogs").child(datetoday).push().setValue(map);
   }

    public void logspatient(){
        Map<String, Object> map1 = new HashMap<>();
        map1.put("action", "Logged In");
        map1.put("date",datewithtime);
        FirebaseDatabase.getInstance().getReference().child("patientlogs").child(edtUser.getText().toString()).child(datetoday)
                .push().setValue(map1);
    }

    public void SharePDF(){
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/P-20220054Information.pdf";
        File file = new File(pdfpath);

        Intent share = new Intent();

        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        share.setType("application/pdf");
        startActivity(share);
    }
}