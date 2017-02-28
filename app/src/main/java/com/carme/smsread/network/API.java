package com.carme.smsread.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yxwang on 17/2/28.
 */

public class API {
    public interface IAPI{
        //mobile={mobile}&code={code}&to={to}&key=ilovecarme&time={time}
        @GET("message.php")
        Observable<String> uploadCode(@Query("mobile") String mobile,
                                      @Query("code") String code,
                                      @Query("to") String to,
                                      @Query("key") String key,
                                      @Query("time") String time);
    }
}
