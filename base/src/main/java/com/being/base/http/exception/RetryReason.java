package com.being.base.http.exception;

/**
 * Created by zhangpeng on 16/3/9.
 */
public enum RetryReason {
    NOREASON,
    QINIUTOKEN,
    TIMEOUT,
    SERVERERROR,
    LOGOUT
}
