package com.carme.smslib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.util.Log;

public class MessageRead extends BroadcastReceiver {
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";

    public MessageRead() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(ACTION_SMS_RECEIVER.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                SmsMessage[] smsArr;
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    smsArr = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                }else{
                    smsArr = getMessagesFromIntent(intent);
                }

                for (SmsMessage sms: smsArr) {
                    if(sms != null) {
                        Log.d("sss",sms.getMessageBody());
                    }
                }
            }
        }
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages;
        try {
            messages = (Object[]) intent.getSerializableExtra("pdus");
        }
        catch (ClassCastException e) {
            return null;
        }

        if (messages == null) {
            return null;
        }

        int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];

        for (int i = 0; i < pduCount; i++) {
            byte[] pdu = (byte[]) messages[i];
            msgs[i] = SmsMessage.createFromPdu(pdu);
        }
        return msgs;
    }
}
