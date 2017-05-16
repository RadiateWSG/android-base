package com.being.base.http.callback;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * 文件返回处理类
 * Created by Zhp on 2015/10/8.
 */
public abstract class FileResponseCallbackImpl extends ResponseCallback<File> {

    protected static final int BUFFER_SIZE = 4096;

    protected final File file;
    protected final boolean append;
    protected File frontendFile;
    protected final boolean renameIfExists;

    public FileResponseCallbackImpl(File file) {
        this(file, false, false);
    }

    public FileResponseCallbackImpl(File file, boolean append) {
        this(file, append, false);
    }

    public FileResponseCallbackImpl(File file, boolean append, boolean renameIfExists) {
        this.file = file;
        this.append = append;
        this.renameIfExists = renameIfExists;
    }

    /**
     * 请求进度
     *
     * @param bytesWritten 已写入
     * @param totalSize    总数
     * @param download
     */
    @Override
    public void onProgress(long bytesWritten, long totalSize, boolean download) {

    }

    /**
     * 请求成功
     *
     * @param baseData 已解析的数据
     */
    @Override
    public final void onSuccess(File baseData) {
        onFileSuccess(frontendFile);
    }

    /**
     * 请求成功
     *
     * @param baseData 已解析的数据
     */
    public abstract void onFileSuccess(File baseData);

    /**
     * 解析返回数据
     *
     * @param response 返回数据
     * @return
     */
    @Override
    public File parseResponse(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            BufferedSink sink = Okio.buffer(Okio.sink(frontendFile));
            sink.writeAll(response.body().source());
            sink.close();
            return frontendFile;
        }
        return null;
    }

    /**
     * 请求失败
     *
     * @param statusCode 0为基类已提示，具体逻辑中不用再提示。
     * @param failDate
     * @param error      具体的错误
     */
    @Override
    public abstract void onFail(int statusCode, File failDate, Throwable error);

    /**
     * 请求完成，无论失败或者成功，都会调用该方法。
     */
    @Override
    public void onFinish() {

    }

    /**
     * 需要提供Gson对象
     * 用于返回数据解析
     *
     * @return Gson对象
     */
    @Override
    public Gson getGson() {
        return null;
    }

    /**
     * 返回需要解析的数据对象Class
     *
     * @return 需要解析的数据对象Class
     */
    @Override
    public Type getClazz() {
        return null;
    }

    /**
     * Will return File instance for file representing last URL segment in given folder.
     * If file already exists and renameTargetFileIfExists was set as true, will try to find file
     * which doesn't exist, naming template for such cases is "filename.ext" =&gt; "filename (%d).ext",
     * or without extension "filename" =&gt; "filename (%d)"
     *
     * @return File in given directory constructed by last segment of mRequest URL
     */
    protected File getTargetFileByParsingURL() {
        asserts(getOriginalFile().isDirectory(), "Target file is not a directory, cannot proceed");
        asserts(getRequest()!= null, "RequestURI is null, cannot proceed");
        String requestURL = getRequest().url().toString();
        String filename = requestURL.substring(requestURL.lastIndexOf('/') + 1, requestURL.length());
        File targetFileRtn = new File(getOriginalFile(), filename);
        if (targetFileRtn.exists() && renameIfExists) {
            String format;
            if (!filename.contains(".")) {
                format = filename + " (%d)";
            } else {
                format = filename.substring(0, filename.lastIndexOf('.')) + " (%d)" + filename.substring(filename.lastIndexOf('.'), filename.length());
            }
            int index = 0;
            while (true) {
                targetFileRtn = new File(getOriginalFile(), String.format(format, index));
                if (!targetFileRtn.exists())
                    return targetFileRtn;
                index++;
            }
        }
        return targetFileRtn;
    }

    /**
     * Retrieves File object in which the response is stored
     *
     * @return File file in which the response was to be stored
     */
    protected File getOriginalFile() {
        asserts(file != null, "Target file is null, fatal!");
        return file;
    }

    /**
     * Retrieves File which represents response final location after possible renaming
     *
     * @return File final target file
     */
    public File getTargetFile() {
        if (frontendFile == null) {
            frontendFile = getOriginalFile().isDirectory() ? getTargetFileByParsingURL() : getOriginalFile();
        }
        return frontendFile;
    }

    @Override
    public void setRequest(Request request) {
        super.setRequest(request);
        getTargetFile();
    }

    /**
     * Will throw AssertionError, if expression is not true
     *
     * @param expression    result of your asserted condition
     * @param failedMessage message to be included in error log
     * @throws AssertionError
     */
    public static void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }

    /**
     * Will throw IllegalArgumentException if provided object is null on runtime
     *
     * @param argument object that should be asserted as not null
     * @param name     name of the object asserted
     * @throws IllegalArgumentException
     */
    public static <T> T notNull(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " should not be null!");
        }
        return argument;
    }
}
