package com.being.base.http.callback;

import android.support.annotation.Nullable;

/**
 * Created by zhangpeng on 17/2/21.
 */
public interface ICallback<GSON_TYPE> {

    int NO_NETWORK_STATUS_CODE = -1;

    int OTHER_STATUS_CODE = -2;

    /**
     * 请求成功
     * @param baseData 已解析的数据
     */
    void onSuccess(GSON_TYPE baseData);

    /**
     * 请求失败
     * 可能是业务逻辑的失败，或者是http请求失败
     * @param statusCode 0为无需特别处理。
     *                   值为{@link ResponseCallback.NO_NETWORK_STATUS_CODE}时为无网络
     * @param failDate  失败的信息
     * @param error    具体的错误
     */
    void onFail(int statusCode, @Nullable GSON_TYPE failDate, @Nullable Throwable error);

    /**
     * 请求完成，无论失败或者成功，都会调用该方法。
     */
    void onFinish();
}
