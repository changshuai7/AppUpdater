package com.shuai.appupdater.dialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;


import java.io.File;
import java.io.Serializable;

public abstract class UpdateDialogListener implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public abstract void onClickDialogConfirm(View view);       //点击弹框确定

    public abstract void onClickDialogCancel(View view);        //点击弹框取消

    public abstract void onPermissionDenied();                  //权限拒绝

    public abstract void onUpdateIsDownloading(boolean isDownloading);            //下载之前，onStart之前，检查是否正在下载

    public abstract void onUpdateStart(String url);             //升级下载开始

    public abstract void onUpdateProgress(int progress, int total, boolean isChange);//升级下载进度

    public abstract void onUpdateFinish(File file);             //升级下载结束

    public abstract void onUpdateError(Exception e);            //升级下载异常

    public abstract void onUpdateCancel();                      //升级下载取消
}
