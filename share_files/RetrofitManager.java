package com.sjs.sjsapp.network;

import com.sjs.sjsapp.application.DataManager;
import com.sjs.sjsapp.log.Logger;
import com.sjs.sjsapp.network.entity.UserInfoWrapper;
import com.sjs.sjsapp.network.service.UserService;
import com.sjs.sjsapp.utils.InterfaceSignUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xeno on 2017/6/7.
 */

public class RetrofitManager {

    private static RetrofitManager INSTANCE;

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private ParamsInterceptor mParamsInterceptor;

    public static RetrofitManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RetrofitManager();
        return INSTANCE;
    }

    private RetrofitManager() {
        initOkHttpClient();
        initRetrofit();
    }

    private void initOkHttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ParamsInterceptor())
                .build();
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(Config.BASE_URI)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public Observable<UserInfoWrapper> userService() {
        UserService userService = mRetrofit.create(UserService.class);
        return userService.requestUserInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    private static class ParamsInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            HttpUrl.Builder bulider = request.url().newBuilder();


//            bulider.addQueryParameter("sign", InterfaceSignUtils.sevenDaySignForOkHttp(request.url(), Config.DP_APPKEY, Config.DP_SECRET, request.url().queryParameterNames(), request.url().queryParameterValues()))

//            //add sign
//            params.put("sign", InterfaceSignUtils.sevenDaySign(url, DP_APPKEY, DP_SECRET, params));
//            //add clientInfo
//            params.put("clientInfo", DataManager.getInstance().getClientInfo());
//            //add token
//            params.put("authority", DataManager.getInstance().getAuthority());

//            Logger.logInfo("======okhttp3 请求:" + request.toString() + "======");//输出请求前整个url
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
//            Logger.logInfo(response.request().url().toString() + response.headers().toString());//输出一个请求的网络信息
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Logger.logInfo(request.url().toString() + "  ================返回结果=============== " + content);//输出返回信息
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

}
