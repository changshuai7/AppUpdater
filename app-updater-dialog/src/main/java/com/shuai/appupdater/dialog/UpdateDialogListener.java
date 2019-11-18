package com.shuai.appupdater.dialog;

import android.view.View;


import java.io.File;
import java.io.Serializable;

public interface UpdateDialogListener extends Serializable {

    void onClickDialogConfirm(View view);       //点击弹框确定

    void onClickDialogCancel(View view);        //点击弹框取消

    void onUpdateIsDownloading(boolean isDownloading);            //下载之前，onStart之前，检查是否正在下载

    void onUpdateStart(String url);             //升级下载开始

    void onUpdateProgress(int progress, int total, boolean isChange);//升级下载进度

    void onUpdateFinish(File file);             //升级下载结束

    void onUpdateError(Exception e);            //升级下载异常

    void onUpdateCancel();                      //升级下载取消
}
