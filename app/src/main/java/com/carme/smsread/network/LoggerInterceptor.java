package com.carme.smsread.network;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by yxwang on 17/2/28.
 */

public class LoggerInterceptor implements Interceptor{
    public static final String TAG = "OkHttpManager";

    public LoggerInterceptor() {
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        this.logForRequest(request);
        Response response = chain.proceed(request);
        return this.logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            Log.i("OkHttpManager", "-------------response\'log---------");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.i("OkHttpManager", "url : " + clone.request().url());
            Log.i("OkHttpManager", "code : " + clone.code());
            Log.i("OkHttpManager", "protocol : " + clone.protocol());
            if(!TextUtils.isEmpty(clone.message())) {
                Log.i("OkHttpManager", "message : " + clone.message());
            }

            ResponseBody body = clone.body();
            if(body != null) {
                MediaType mediaType = body.contentType();
                if(mediaType != null) {
                    Log.i("OkHttpManager", "responseBody\'s contentType : " + mediaType.toString());
                    if(this.isText(mediaType)) {
                        String resp = body.string();
                        Log.i("OkHttpManager", "responseBody\'s content : " + resp);
                        body = ResponseBody.create(mediaType, resp);
                        Log.i("OkHttpManager", "---------------response\'log-------------end");
                        return response.newBuilder().body(body).build();
                    }

                    Log.i("OkHttpManager", "responseBody\'s content :  maybe [file part] , too large too print , ignored!");
                }
            }

            Log.i("OkHttpManager", "---------------response\'log-------------end");
        } catch (Exception var7) {
            ;
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            Log.i("OkHttpManager", "-------------request\'log-------------");
            Log.i("OkHttpManager", "method:" + request.method());
            Log.i("OkHttpManager", "url:" + url);
            if(headers != null && headers.size() > 0) {
                Log.i("OkHttpManager", "headers:" + headers.toString());
            }

            RequestBody requestBody = request.body();
            if(requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if(mediaType != null) {
                    Log.i("OkHttpManager", "requestBody\'s contentType:" + mediaType.toString());
                    if(this.isText(mediaType)) {
                        Log.i("OkHttpManager", "requestBody\'s content:" + this.bodyToString(request));
                    } else {
                        Log.i("OkHttpManager", "requestBody\'s content: maybe [file part] , too large too print , ignored!");
                    }
                }
            }

            Log.i("OkHttpManager", "-------------request\'log----------------end");
        } catch (Exception var6) {
            ;
        }

    }

    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null && mediaType.type().equals("text")?true:mediaType.subtype() != null && (mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") || mediaType.subtype().equals("html") || mediaType.subtype().equals("webviewhtml"));
    }

    private String bodyToString(Request request) {
        try {
            Request e = request.newBuilder().build();
            Buffer buffer = new Buffer();
            e.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException var4) {
            var4.printStackTrace();
            return "something error when show requestBody" + var4.getMessage();
        }
    }
}
