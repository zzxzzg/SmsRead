package com.carme.smsread.network;

import com.carme.smsread.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yxwang on 17/2/28.
 */

public enum HttpMethod {
    INSTANCE;

    private static final int DEFAULT_TIMEOUT = 10;
    private static final String mBaseUrl="xxxxxxxxx";

    public Retrofit mRetrofit;
    HttpMethod(){
        init();
    }
    public void init(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //if(BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(new LoggerInterceptor());
        //}

        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .build();
    }
}
