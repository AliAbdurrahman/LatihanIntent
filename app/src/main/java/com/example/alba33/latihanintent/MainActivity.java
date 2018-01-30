package com.example.alba33.latihanintent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends AppCompatActivity {

    Spinner spin;
    EditText link, nomor, email, subject, pesan;
    Button submit;

    String item;
    Context context = this;

    String[] dataKirim = new String[]{
            "Email", "SMS", "Phone", "Web"
    };

    private static final int PERMISSON_REQUEST_CODE = 1;

    private static final int SPINNER_OPTION_FIRST = 0;
    private static final int SPINNER_OPTION_SECOND = 1;
    private static final int SPINNER_OPTION_THIRD = 2;
    private static final int SPINNER_OPTION_FOURTH = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin = (Spinner) findViewById(R.id.spinner);
        link = (EditText) findViewById(R.id.etLink);
        nomor = (EditText) findViewById(R.id.etNomor);
        email = (EditText) findViewById(R.id.etEmail);
        subject = (EditText) findViewById(R.id.etSubject);
        pesan = (EditText) findViewById(R.id.etPesan);
        submit = (Button) findViewById(R.id.btnSubmit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataKirim);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                if(spin.getSelectedItem().toString() == "Web"){
                    email.setVisibility(View.INVISIBLE);
                    pesan.setVisibility(View.INVISIBLE);
                    nomor.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.INVISIBLE);
                    link.setVisibility(View.VISIBLE);
                }else if (spin.getSelectedItem().toString() == "SMS"){
                    pesan.setVisibility(View.VISIBLE);
                    nomor.setVisibility(View.VISIBLE);
                    link.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                }else if (spin.getSelectedItem().toString() == "Phone"){
                    nomor.setVisibility(View.VISIBLE);
                    link.setVisibility(View.INVISIBLE);
                    subject.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    pesan.setVisibility(View.INVISIBLE);
                }else if (spin.getSelectedItem().toString() == "Email"){
                    email.setVisibility(View.VISIBLE);
                    subject.setVisibility(View.VISIBLE);
                    pesan.setVisibility(View.VISIBLE);
                    link.setVisibility(View.INVISIBLE);
                    nomor.setVisibility(View.INVISIBLE);

                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Link = link.getText().toString();
                String Nomor = nomor.getText().toString();
                String Email = email.getText().toString();
                String Subject = subject.getText().toString();
                String Pesan = pesan.getText().toString();

                if (item == "SPINNER_OPTION_FIRST") {
                    //String untuk pengiriman Email
                    String emailTujuan = Email;
                    String subjectEmail = Subject;
                    String isiEmail = Pesan;

                    //Intent Email
                    Intent nEmail = new Intent(Intent.ACTION_SEND);
                    nEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTujuan});
                    nEmail.putExtra(Intent.EXTRA_SUBJECT, subjectEmail);
                    nEmail.putExtra(Intent.EXTRA_TEXT, isiEmail);

                    //Format kode untuk pengiriman email
                    nEmail.setType("message/rcf822");
                    startActivity(Intent.createChooser(nEmail, "Pilih Email Client"));
                } else if (item == "SPINNER_OPTION_SECOND") {
                    String dataIsiSMS = "Hallo, ini adalah SMS";

                    //Android Permission 6.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                            Log.d("Permission", "Permission Denied to SEND_SMS - requesting it");
                            String[] permissions = {Manifest.permission.SEND_SMS};

                            requestPermissions(permissions, PERMISSON_REQUEST_CODE);
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    //Memanggil library SMS Manager dan memanggil string dataIsiSMS
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(Nomor, null, dataIsiSMS, pi, null);

                    Toast.makeText(getApplicationContext(), "SMS Berhasil Dikirim", Toast.LENGTH_SHORT).show();

                } else if (item == "SPINNER_OPTION_THIRD") {
                    //Aksi ketika button phone dipencet
                    //Intent implicit ke no telp
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("Tel : " + Nomor));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                } else if (item == "SPINNER_OPTION_FOURTH") {
                    //Memanggil URL
                    //Memanggil urlWeb ketika Intent
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
                    startActivity(intent1);
                }

            }

        });
    }
}
