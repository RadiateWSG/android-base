package com.being.base.http.callback;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.being.base.http.GsonManager;
import com.being.base.http.model.BaseObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 基础网络请求回调抽象类
 * 将具体的数据对象设置为泛型
 * 简化了class的传递过程
 * Created by Zhp on 2014/5/27.
 */
public abstract class ResponseCallbackImpl<GSON_TYPE> extends ResponseCallback<GSON_TYPE> {

    public ResponseCallbackImpl() {
        super();
    }

    public ResponseCallbackImpl(boolean sync) {
        super(sync);
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize, boolean download) {

    }

    @Override
    public abstract void onSuccess(GSON_TYPE baseData);

    /**
     * 请求失败
     * 可能是业务逻辑的失败，或者是http请求失败
     * @param statusCode 0为无需特别处理。
     *                   值为{@link ResponseCallback.NO_NETWORK_STATUS_CODE}时为无网络
     * @param failDate  失败的信息
     * @param error    具体的错误
     */
    @Override
    public abstract void onFail(int statusCode, @Nullable GSON_TYPE failDate, @Nullable Throwable error);

    @Override
    public GSON_TYPE parseResponse(Response response) throws IOException {
        return null;
    }

    @Override
    public void onFinish() {

    }

    /**
     * 获取默认的Gson
     * 子类可以重写该方法返回自定义的Gson
     *
     * @return GsonManager.getGson()
     */
    @Override
    public Gson getGson() {
        return GsonManager.getGson();
    }

    /**
     * 通过反射获取包含具体泛型对象的Class<br>
     * 可以省去再次传递class的过程
     * 除非需要Gson解析的Class与GSON_TYPE不同，否则请不要重写此类
     * @return Type
     */
    @Override
    public Type getClazz() {
        try {
            ResponseCallbackImpl responseCallback = this;
            Type genericSuperclass = responseCallback.getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            if (type != null) {
                return type;
            } else {
                return BaseObject.class;
            }
        } catch (Exception e) {
            return BaseObject.class;
        }
    }

}
