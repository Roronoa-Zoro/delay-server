package com.illegalaccess.delay.toolkit.http;

public interface HttpCallback {

    void onError(String msg, Throwable throwable);

    void onSuccess(String msg);

    void onFail(String msg);
}
