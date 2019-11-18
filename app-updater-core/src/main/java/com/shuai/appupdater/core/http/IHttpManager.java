package com.shuai.appupdater.core.http;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.Map;


public interface IHttpManager {


    /**
     * 下载
     * @param url               下载地址
     * @param path              下载路径
     * @param filename          文件名
     * @param fileMd5           要下载文件的MD5
     * @param requestProperty   请求头参数
     * @param callback          请求结果回调
     */
    void download(String url, String path, String filename,String fileMd5, @Nullable Map<String,String> requestProperty, DownloadCallback callback);

    /**
     * 取消下载
     */
    void cancel();

    interface DownloadCallback extends Serializable{
        /**
         * 开始下载
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
         * 下载完成
         * @param file
         */
        void onFinish(File file);

        /**
         * 下载错误
         * @param e
         */
        void onError(Exception e);


        /**
         * 下载取消
         */
        void onCancel();
    }
}
