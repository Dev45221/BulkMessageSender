package com.example.automessagessender;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.net.URLEncoder;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MySMSservice extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SMS = "com.example.automessagessender.action.FOO";
    private static final String ACTION_WHATSAPP = "com.example.automessagessender.action.BAZ";

    // TODO: Rename parameters
    private static final String MESSAGE = "com.example.automessagessender.extra.PARAM1";
    private static final String COUNT = "com.example.automessagessender.extra.PARAM2";
    private static final String MOBILE_NUMBER = "com.example.automessagessender.extra.PARAM2";

    public MySMSservice() {
        super("MySMSservice");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSMS(Context context, String message, String count, String mobile_number) {
        Intent intent = new Intent(context, MySMSservice.class);
        intent.setAction(ACTION_SMS);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER, mobile_number);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWHATSAPP(Context context, String message, String count, String mobile_number) {
        Intent intent = new Intent(context, MySMSservice.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER, mobile_number);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SMS.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String count = intent.getStringExtra(COUNT);
                final String mobile_number = intent.getStringExtra(MOBILE_NUMBER);
                handleActionSMS(message, count, mobile_number);
            } else if (ACTION_WHATSAPP.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String count = intent.getStringExtra(COUNT);
                final String mobile_number = intent.getStringExtra(MOBILE_NUMBER);
                handleActionWHATSAPP(message, count, mobile_number);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSMS(String message, String count, String mobile_number) {
        // TODO: Handle action Foo
        try {
            for (int i=0;i<Integer.parseInt(count.toString());i++) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobile_number,null, message,null,null);
                sendBroadcastMessage("result: " + i + " " + mobile_number);
            }
        }catch (Exception e) {}
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWHATSAPP(String message, String count, String mobile_number) {

        try{
            PackageManager packageManager = getApplicationContext().getPackageManager();
            try {
                int ct = Integer.parseInt(count);
                for (int i=0; i<ct; i++) {
                    String url = "https://api.whatsapp.com?phone=" + mobile_number + "&text=" + URLEncoder.encode(message,"UTF-8");
                    Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.setAction(Intent.ACTION_SEND);
                    whatsappIntent.setData(Uri.parse(url));
                    whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (whatsappIntent.resolveActivity(packageManager)==null) {
                        getApplicationContext().startActivity(whatsappIntent);
                        Thread.sleep(10000);
                        sendBroadcastMessage("ResultTry: " + mobile_number);
                    }else {
                        sendBroadcastMessage("WhatsApp not installled!");
                    }
                }
            }catch (NumberFormatException e) {}
        }catch (Exception e) {
            sendBroadcastMessage("ResultCat: " + e);

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
        }
    }

    private void sendBroadcastMessage(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("Result: " , message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}