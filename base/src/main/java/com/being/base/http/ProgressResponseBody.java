package com.being.base.http;

import android.os.Handler;

import com.being.base.http.callback.ICallback;
import com.being.base.http.callback.ResponseCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 带进度条的ResponseBody
 * Created by Zhp on 2015/10/8.
 */
public class ProgressResponseBody extends ResponseBody {

    private Response response;
    private final ResponseBody responseBody;
    private final ResponseCallback progressListener;
    private BufferedSource bufferedSource;
    private Handler handler;

    public ProgressResponseBody(Response response, ResponseCallback progressListener, Handler threadHandler) {
        this.responseBody = response.body();
        this.response = response;
        this.progressListener = progressListener;
        this.handler = threadHandler;
    }

    @Override
    public MediaType contentType() {
        return AsyncOkHttp.APPLICATION_FORM_URLENCODED;
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            try {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedSource;
    }

    private Source source(Source source) throws IOException{
        return new ForwardingSource(source) {
            //断点续传时需要计算已经下载的文件长度
            long totalBytesRead = getTotalLength() - responseBody.contentLength();
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                final long totalLength = getTotalLength();
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
                if (bytesRead <= 0 || totalLength <= 0) return bytesRead;
                Runnable runnable = new Runnable() {
                    public void run() {
                        progressListener.onProgress(totalBytesRead, totalLength, true);
                    }
                };
                handler.post(runnable);
                return bytesRead;
            }
        };
    }

    private long getTotalLength() throws IOException {
        if (response.code() != 206) {//断点续传时，需要计算真实文件总长度，从Content-Range中获取
            return responseBody.contentLength();
        } else {
            String contentRange = response.headers().get("Content-Range");
            int index = contentRange.indexOf("/");
            return Long.parseLong(contentRange.substring(index + 1));
        }
    }
}
