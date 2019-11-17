package com.shuai.app.appupdater.dialog;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 版本信息
 */
public class UpdateBean implements Parcelable {

    private boolean isUpdate;   //是否有新版本（控制是否执行弹窗更新）
    private boolean isForce;    //是否强制更新

    private String newAppVersion;           //新版本号："1.2.3"
    private String newAppUpdateLog;         //更新日志："1、优化性能\n 2、修复bug"
    private String newAppUpdateDialogTitle; //配置dialog的title
    private String newAppSize;              //新app大小："100M"
    private String newAppMd5;               //Md5   ："abcdefsadf"


    public UpdateBean() {
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    public String getNewAppVersion() {
        return newAppVersion;
    }

    public void setNewAppVersion(String newAppVersion) {
        this.newAppVersion = newAppVersion;
    }

    public String getNewAppUpdateLog() {
        return newAppUpdateLog;
    }

    public void setNewAppUpdateLog(String newAppUpdateLog) {
        this.newAppUpdateLog = newAppUpdateLog;
    }

    public String getNewAppUpdateDialogTitle() {
        return newAppUpdateDialogTitle;
    }

    public void setNewAppUpdateDialogTitle(String newAppUpdateDialogTitle) {
        this.newAppUpdateDialogTitle = newAppUpdateDialogTitle;
    }


    public String getNewAppMd5() {
        return newAppMd5;
    }

    public void setNewAppMd5(String newAppMd5) {
        this.newAppMd5 = newAppMd5;
    }

    public String getNewAppSize() {
        return newAppSize;
    }

    public void setNewAppSize(String newAppSize) {
        this.newAppSize = newAppSize;
    }


    //----------------------------

    protected UpdateBean(Parcel in) {
        isUpdate = in.readByte() != 0;
        isForce = in.readByte() != 0;
        newAppVersion = in.readString();
        newAppUpdateLog = in.readString();
        newAppUpdateDialogTitle = in.readString();
        newAppSize = in.readString();
        newAppMd5 = in.readString();
    }

    public static final Creator<UpdateBean> CREATOR = new Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel in) {
            return new UpdateBean(in);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isUpdate ? 1 : 0));
        parcel.writeByte((byte) (isForce ? 1 : 0));
        parcel.writeString(newAppVersion);
        parcel.writeString(newAppUpdateLog);
        parcel.writeString(newAppUpdateDialogTitle);
        parcel.writeString(newAppSize);
        parcel.writeString(newAppMd5);
    }
}
