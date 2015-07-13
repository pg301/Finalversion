package com.example.callsrejected;
import java.lang.reflect.Method;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class IncomingCallReceiver extends BroadcastReceiver{
    String incomingNumber="";
    AudioManager audioManager;
    TelephonyManager telephonyManager;


    public void onReceive(Context context, Intent intent) {

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            }
        }

        if (!incomingNumber.equals("")) {
            rejectCall();
            startApp(context, incomingNumber);
        }
    }



    private void startApp(Context context, String number){
        Intent intent=new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("number",number);
        context.sendBroadcast(intent);
    }

    private void rejectCall(){


        try {

            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            Object telephonyInterface = method.invoke(telephonyManager);
            Class<?> telephonyInterfaceClass =Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            methodEndCall.invoke(telephonyInterface);
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}