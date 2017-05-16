package com.being.base.http;

import com.being.base.http.callback.DownloadCallback;
import com.being.base.http.callback.GsonResponseCallbackImpl;
import com.being.base.http.callback.ResponseCallback;
import com.being.base.http.exception.FileDownloadExecption;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

/**
 * Http请求管理类
 * Created by Zhp on 2014/5/19.
 * <pre>
 * Simple Demo:
 * HttpManager.getInstance().post(mTestUrl, requestParams, new ResponseCallbackImpl<TestGsonModel>() {

            &#064;Override
            public void onStart() {
            }

            &#064;Override
            public void onSuccess(TestGsonModel baseData) {

            }

            &#064;Override
            public void onFail(TestGsonModel failData, Throwable error) {

            }
        });
 * </pre>
 */
@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class HttpManager {

    private static HttpManager mInstance = new HttpManager();

    private AsyncOkHttp mAsyncOkHttp;

    public static HttpManager getInstance() {
        return mInstance;
    }

    private HttpManager() {
        mAsyncOkHttp = new AsyncOkHttp();
    }

    public AsyncOkHttp getAsyncOkHttp() {
        return mAsyncOkHttp;
    }

    /**
     * 基础post请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * 使用Gson处理数据
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return RequestHandle
     */
    public CallHandler post(final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncOkHttp.post(url, params, new GsonResponseCallbackImpl(responseCallback));
    }

    /**
     * 基础get请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     * @param url     请求的url
     * @param params  请求的参数
     * @param responseCallback   请求的回调
     * @return CallHandler
     */
    public CallHandler get(final String url, final RequestParams params, final ResponseCallback responseCallback) {
        return mAsyncOkHttp.get(url, params, new GsonResponseCallbackImpl(responseCallback));
    }

    /**
     * 基础download请求，在回调中返回获取的数据。
     * 所有的回调都运行在UI线程中
     *
     * @param url              请求的url
     * @param filePath         需要保存的文件路径
     *                         如果是文件夹，会从url中解析文件名，如果是绝对路径，则按照指定文件名保存
     * @param responseCallback 请求的回调
     * @return CallHandler
     */
    public BaseDownloadTask download(final String url, final String filePath, final DownloadCallback responseCallback) {
        return download(url, filePath, false, responseCallback);
    }

    /**
     * 基础download请求，在回调中返回获取的数据。
     * 所有的回调都运行在download线程中
     *
     * @param url              请求的url
     * @param filePath         需要保存的文件路径
     *                         如果是文件夹，会从url中解析文件名，如果是绝对路径，则按照指定文件名保存
     * @param responseCallback 请求的回调
     * @return CallHandler
     */
    public BaseDownloadTask download(final String url, final String filePath, boolean sync, final DownloadCallback responseCallback) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(url).setPath(filePath, new File(filePath).isDirectory()).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (responseCallback != null) {
                    responseCallback.onProgress(soFarBytes, totalBytes, totalBytes == soFarBytes);
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (responseCallback != null) {
                    long total = task.getSmallFileTotalBytes();
                    responseCallback.onProgress(total, total, true);
                    responseCallback.onSuccess(new File(filePath));
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (responseCallback != null) {
                    String messsage = task.getUrl() + "\n" + (e != null ? e.getMessage() : "");
                    responseCallback.onFail(0, new File(task.getPath()), new FileDownloadExecption(messsage, e));
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });
        baseDownloadTask.setSyncCallback(sync);
        baseDownloadTask.start();
        return baseDownloadTask;
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        mAsyncOkHttp.cancelAll();
    }

    public void setCertificates(InputStream... certificates) {
        setCertificates(certificates, null, null);
    }

    /**
     * 设置信任证书
     * @param certificates 服务端证书，如果不需要验证服务端证书，则传空
     * @param bksFile      客户端证书
     * @param password     客户端证书密码
     */
    @SuppressWarnings("TrulyRandom")
    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        SSLSocketFactory factory = TrustUtils.getTrustSslSocketFactory(certificates, bksFile, password);
        if (factory != null) {
            mAsyncOkHttp.setSslSocketFactory(factory);
        }
    }

}