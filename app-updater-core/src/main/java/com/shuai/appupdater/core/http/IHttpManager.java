package com.shuai.appupdater.core.http;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.Map;


public interface IHttpManager {


    /**
     * 下载
     * @param url
     * @param path
     * @param filename
     * @param fileMd5
     * @param requestProperty
     * @param callback
     */
    void download(String url, String path, String filename,String fileMd5, @Nullable Map<String,String> requestProperty, DownloadCallback callback);

    /**
     * 取消下载
     */
    void cancel();

    interface DownloadCallback extends Serializable{
        /**
         * 开始
         * @param url
         */
        void onStart(String url);

        /**
         * 加载进度…
         * @param progress
         * @param total
         */
        void onProgress(int progress,int total);

        /**
         * 完成
         * @param file
         */
        void onFinish(File file);

        /**
         * 错误
         * @param e
         */
        void onError(Exception e);


        /**
         * 取消
         */
        void onCancel();
    }
}
