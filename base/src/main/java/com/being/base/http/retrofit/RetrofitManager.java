package com.being.base.http.retrofit;

import android.util.Log;

import com.being.base.Constant;
import com.being.base.http.intercept.TryCacheInterceptor;
import com.being.base.http.retrofit.calladapter.RxThreadCallAdapter;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangpeng on 17/2/20.
 */
public class RetrofitManager {

    public static final int DEFAULT_MAX_CONNECTIONS = 5;
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private int connectTimeout = DEFAULT_SOCKET_TIMEOUT;
    private int responseTimeout = DEFAULT_SOCKET_TIMEOUT;

    private static RetrofitManager mInstance;

    public static RetrofitManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClient mOkHttpClient;

    private Retrofit mRetrofit;

    private RetrofitManager() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DEFAULT_MAX_CONNECTIONS);
        dispatcher.setMaxRequestsPerHost(DEFAULT_MAX_CONNECTIONS);
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_SOCKET_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .dispatcher(dispatcher)
                .dns(Dns.SYSTEM)
                .build();
        setupInceptor();
    }

    public void init(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxThreadCallAdapter.create());
        builder.client(mOkHttpClient);
        mRetrofit = builder.build();
    }

    public void init(Retrofit.Builder builder) {
        mRetrofit = builder.client(mOkHttpClient).build();
    }

    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }

    public void addInterceptor(Interceptor interceptor) {
        mOkHttpClient = mOkHttpClient.newBuilder().addInterceptor(interceptor).build();
    }

    public void addNetworkInterceptor(Interceptor interceptor) {
        mOkHttpClient = mOkHttpClient.newBuilder().addNetworkInterceptor(interceptor).build();
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        if (mOkHttpClient != null) {
            mOkHttpClient = mOkHttpClient.newBuilder().sslSocketFactory(sslSocketFactory).build();
        }
    }

    public void setCacheDir(Cache cache) {
        mOkHttpClient = mOkHttpClient.newBuilder().cache(cache).build();
    }

    public void setDns(Dns dns) {
        mOkHttpClient = mOkHttpClient.newBuilder().dns(dns).build();
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mOkHttpClient = mOkHttpClient.newBuilder().hostnameVerifier(hostnameVerifier).build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    private void setupInceptor() {
        if (Constant.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.v("OkHttp", message);
                }
            });
            //下载文件时，如果使用BODY级别，会将整个ResponseBody缓存到内存，造成oom
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = mOkHttpClient.newBuilder().addInterceptor(logging).build();
        }
    }
}