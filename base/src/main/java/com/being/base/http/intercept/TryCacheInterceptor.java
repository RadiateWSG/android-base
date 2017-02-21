package com.being.base.http.intercept;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 没有网络的情况下,尝试使用cache
 * Created by zhangpeng on 17/2/21.
 */

public class TryCacheInterceptor implements Interceptor {

    private OkHttpClient mClient;

    public TryCacheInterceptor(OkHttpClient client) {
        mClient = client;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!"get".equalsIgnoreCase(request.method())) {
            return chain.proceed(request);
        }
        Response cacheResponse;
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();
            cacheResponse = mClient.newCall(request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()).execute();
            if (cacheResponse != null && cacheResponse.code() == 504) {
                throw e;
            }
            return cacheResponse;
        }
    }
}
