package com.example.automessagessender;

import static com.example.automessagessender.MySMSservice.startActionWHATSAPP;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    EditText tv_message, tv_number, tv_count;
    Button bt_manual, bt_sms, bt_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message = findViewById(R.id.tv_message);
        tv_number = findViewById(R.id.tv_number);
        tv_count = findViewById(R.id.tv_count);
        bt_manual = findViewById(R.id.bt_manual);
        bt_sms = findViewById(R.id.bt_sms);
        bt_whatsapp = findViewById(R.id.bt_whatsapp);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

        if (!isAccessibilityOn(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        bt_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for (int i = 0; i < Integer.parseInt(tv_count.getText().toString()); i++) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(tv_number.getText().toString(), null, tv_message.getText().toString(), null, null);
                        Toast.makeText(MainActivity.this, "SMS sent successfully ✔. \n Count: " + (i + 1), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "SMS not sent ❌.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendsms = new Intent(Intent.ACTION_SEND);
                sendsms.setType("text/plain");
                sendsms.putExtra(Intent.EXTRA_TEXT, tv_message.getText().toString());
                startActivity(sendsms);
            }
        });

//        bt_whatsapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = tv_message.getText().toString();
//                String count = tv_count.getText().toString();
//                String mobile = tv_number.getText().toString();

//
//                MySMSservice.startActionWHATSAPP(getApplicationContext(),msg,count,mobile);

//                startActionWHATSAPP(getApplicationContext(), msg, count, mobile);

//last one
//                try{
//                    PackageManager packageManager = getApplicationContext().getPackageManager();
//                    try {
//                        int ct = Integer.parseInt(count);
//                        for (int i=0; i<ct; i++) {
//                            String url = "https://api.whatsapp.com?phone=" + mobile + "&text=" + URLEncoder.encode(msg,"UTF-8");
//                            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
//                            whatsappIntent.setPackage("com.whatsapp");
//                            whatsappIntent.setAction(Intent.ACTION_SEND);
//                            whatsappIntent.setData(Uri.parse(url));
//                            whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            if (whatsappIntent.resolveActivity(packageManager)==null) {
//                                getApplicationContext().startActivity(whatsappIntent);
//                                Thread.sleep(10000);
//                                sendBroadcastMessage("ResultTry: " + mobile);
//                            }else {
//                                sendBroadcastMessage("WhatsApp not installled!");
//                            }
//                        }
//                    }catch (NumberFormatException e) {}
//                }catch (Exception e) {
//                    sendBroadcastMessage("ResultCat: " + e);
//                }

//                try{
//                    String number = tv_number.getText().toString();
//                    String message = tv_message.getText().toString();
//                    String c = tv_count.getText().toString();
//                    int ct = Integer.parseInt(c);
//                    PackageManager packageManager = getApplicationContext().getPackageManager();
//                            for (int i=0; i<ct; i++) {
//                                String url = "https://api.whatsapp.com?phone=" + number + "&text=" + URLEncoder.encode(message,"UTF-8");
//                                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
//                                whatsappIntent.setPackage("com.whatsapp");
//                                whatsappIntent.setAction(Intent.ACTION_SEND);
//                                whatsappIntent.setData(Uri.parse(url));
//                                whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                if (whatsappIntent.resolveActivity(packageManager)==null) {
//                                    getApplicationContext().startActivity(whatsappIntent);
//                                    Thread.sleep(10000);
//                                    sendBroadcastMessage("ResultTry: " + number);
//                                }else {
//                                    sendBroadcastMessage("WhatsApp not installled!");
//                                }
//                            }
//                }catch (Exception e) {
//                    sendBroadcastMessage("ResultCat: " + e);

//                    String number = tv_number.getText().toString();
//                    String message = tv_message.getText().toString();
//                    String url = null;
//                    try {
//                        url = "https://wa.me/91" + number + "?text=" + URLEncoder.encode(message,"UTF-8");
//                        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
//                        whatsappIntent.setPackage("com.whatsapp");
//                        whatsappIntent.setData(Uri.parse(url));
//                        whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(whatsappIntent);
//                        sendBroadcastMessage("Resultcat: " + number);
//                    } catch (UnsupportedEncodingException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });

        IntentFilter intentFilter = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myLocalBroadcastReceiver, intentFilter);
    }

    private boolean isAccessibilityOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + WhatAppAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        TextUtils.SimpleStringSplitter colonSpliter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            if (settingValue != null) {
                colonSpliter.setString(settingValue);
                while (colonSpliter.hasNext()) {
                    String accessinilityService = colonSpliter.next();
                    if (accessinilityService.equals(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void sendBroadcastMessage(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("Result: ", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("Result: ");
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    };

    public void WhatsApp(View view) {
        String msg = tv_message.getText().toString();
        String count = tv_count.getText().toString();
        String mobile = tv_number.getText().toString();

        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            try {
                int ct = Integer.parseInt(count);
                for (int i = 0; i < ct; i++) {
                    String url = "https://wa.me/91" + mobile + "?text=" + URLEncoder.encode(msg, "UTF-8");
                    Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.setAction(Intent.ACTION_SEND);
                    whatsappIntent.setData(Uri.parse(url));
                    whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(whatsappIntent);
                    Thread.sleep(10000);
                    sendBroadcastMessage("ResultTry: " + mobile);
                }
            } catch (NumberFormatException e) {
            }
        } catch (Exception e) {
//            sendBroadcastMessage("ResultCat: " + e.toString());
            String url = null;
            try {
                url = "https://wa.me/91" + mobile + "?text=" + URLEncoder.encode(msg, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.setData(Uri.parse(url));
            whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(whatsappIntent);
            sendBroadcastMessage("ResultCat: " + mobile);
        }
    }
}