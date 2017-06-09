package com.sjs.sjsapp.network;

import android.content.Context;

import com.google.gson.Gson;
import com.sjs.sjsapp.application.DataManager;
import com.sjs.sjsapp.log.Logger;
import com.sjs.sjsapp.network.entity.WrapperEntity;
import com.sjs.sjsapp.ui.activity.BaseActivity;
import com.sjs.sjsapp.ui.activity.LoginActivity;
import com.sjs.sjsapp.utils.InterfaceSignUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xeno on 2016/2/16.
 */
public class OKHttpRequest<T extends WrapperEntity> {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String DP_APPKEY = "2016sjs2";
    public static final String DP_SECRET = "8a810b3d7c0b86d6b08580eb5ad6fa6b";

    //处理结果
//            100	成功
//            200	失败
//            201	添加失败
//            202	修改失败
//            203	删除失败
//            204	查无记录
//            999	其它错误

//                第1位：
//                1-成功
//                2-操作失败
//                3-参数类
//                4-权限类
//                9-系统类


    private OkHttpClient mClient;
    private Context mContext;
    private Gson mGson;

    public OKHttpRequest(Context context) {
        mContext = context;
        mClient = new OkHttpClient();

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        mClient.setSslSocketFactory(sc.getSocketFactory());
        mClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });



        mGson = new Gson();
    }

    //借用一下hibernateBaseDao的方法
    private Class<T> getTClass() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<T>) params[0];
    }

    /**
     */
    public void requestPost(String url, HashMap<String, Object> params, Class<? extends WrapperEntity> clazz, ResultCallback resultCallback) {

        String uri = Config.BASE_URI + url;

            //add sign
        params.put("sign", InterfaceSignUtils.sevenDaySign(url, DP_APPKEY, DP_SECRET, params));
        //add clientInfo
        params.put("clientInfo", DataManager.getInstance().getClientInfo());
        //add token
        params.put("authority", DataManager.getInstance().getAuthority());

        String jsonParams = mGson.toJson(params);
        Logger.logInfo("==================目标=================" + uri);
        Logger.logInfo("================请求参数===============" + jsonParams);

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonParams);

        try {
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .build();

            Call call = mClient.newCall(request);
            makeCallAsync(uri, call, clazz, resultCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void requestPostSilently(String url, HashMap<String, Object> params, Class<T> clazz, ResultCallback resultCallback) {
        String uri = Config.BASE_URI + url;

        //add sign
        params.put("sign", InterfaceSignUtils.sevenDaySign(url, DP_APPKEY, DP_SECRET, params));
        //add clientInfo
        params.put("clientInfo", DataManager.getInstance().getClientInfo());
        //add token
        params.put("authority", DataManager.getInstance().getAuthority());

        String jsonParams = mGson.toJson(params);
        Logger.logInfo("==================目标=================" + uri);
        Logger.logInfo("================请求参数===============" + jsonParams);

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonParams);

        try {
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .build();

            Call call = mClient.newCall(request);
            makeCallAsyncSilently(uri, call, clazz, resultCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeCallAsync(final String uri, Call call, final Class<? extends WrapperEntity> clazz, final ResultCallback resultCallback) {

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // 提示IO异常
                resultCallback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                WrapperEntity resultT = deliverResult(uri, response, clazz);

                if (resultT != null) {
                    if (resultT.getMsgCode() == 405) {
                        // fail for authority problem

                        Observable.create(new Observable.OnSubscribe<Object>() {
                            @Override
                            public void call(Subscriber<? super Object> subscriber) {
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<Object>() {
                                    @Override
                                    public void call(Object o) {
                                        DataManager.getInstance().loginStateFail();
                                        LoginActivity.goFromTokenInvalid((BaseActivity) mContext);
                                    }
                                });
                    } else {
                        //success
                        resultCallback.onSuccess(resultT);
                    }
                } else {
                    resultCallback.onFailure(new Exception("Gson解析对象为空"));
                }
            }
        });
    }

    /**
     * make a call，will not handler when encounter authority probelms
     */
    private void makeCallAsyncSilently(final String uri, Call call, final Class<T> clazz, final ResultCallback resultCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // 提示IO异常
                resultCallback.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                WrapperEntity resultT = deliverResult(uri, response, clazz);

                if (resultT != null) {
                    if (resultT.getMsgCode() == 405) {
                        // fail for authority problem, will not handle it
                        Observable.create(new Observable.OnSubscribe<Object>() {
                            @Override
                            public void call(Subscriber<? super Object> subscriber) {
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<Object>() {
                                    @Override
                                    public void call(Object o) {
                                        DataManager.getInstance().loginStateFail();
                                    }
                                });
                    } else {
                        //success
                        resultCallback.onSuccess(resultT);
                    }
                } else {
                    resultCallback.onFailure(new Exception("Gson解析对象为空"));
                }
            }
        });
    }

    private WrapperEntity deliverResult(String uri, Response response, Class<? extends WrapperEntity> clazz) throws IOException {
        String result = response.body().string();
        WrapperEntity resultT = null;

        try {
            Logger.logInfo(uri + " ================返回结果===============" + result);
            resultT = mGson.fromJson(result, clazz);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultT;

    }

}
