package com.being.base.http.exception;

/**
 * Created by zhangpeng on 17/2/21.
 */

public class ApiException extends Throwable {

    protected int mCode;

    public ApiException(int code, String message) {
        super(message);
        mCode = code;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "Code=" + mCode +
                "} " + super.toString();
    }
}
