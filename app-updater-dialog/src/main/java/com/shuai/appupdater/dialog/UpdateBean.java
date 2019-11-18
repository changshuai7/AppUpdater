package com.shuai.appupdater.dialog;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 和版本升级信息相关配置
 */
public class UpdateBean implements Parcelable {

    private boolean isUpdate;   //是否有新版本（控制是否执行弹窗更新）
    private boolean isForce;    //是否强制更新

    private String newAppVersion;           //新版本号："1.2.3"
    private String newAppUpdateLog;         //更新日志："1、优化性能\n 2、修复bug"
    private String newAppUpdateDialogTitle; //配置dialog的title。需要注意的是，如果这里传入了title，那么会覆盖形如："发现新版本1.0.0"的标题。
    private String newAppSize;              //新app大小："100M"

    public UpdateBean() {
    }

    public UpdateBean build(){
        return this;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public UpdateBean setUpdate(boolean update) {
        isUpdate = update;
        return this;
    }

    public boolean isForce() {
        return isForce;
    }

    public UpdateBean setForce(boolean force) {
        isForce = force;
        return this;
    }

    public String getNewAppVersion() {
        return newAppVersion;
    }

    public UpdateBean setNewAppVersion(String newAppVersion) {
        this.newAppVersion = newAppVersion;
        return this;
    }

    public String getNewAppUpdateLog() {
        return newAppUpdateLog;
    }

    public UpdateBean setNewAppUpdateLog(String newAppUpdateLog) {
        this.newAppUpdateLog = newAppUpdateLog;
        return this;
    }

    public String getNewAppUpdateDialogTitle() {
        return newAppUpdateDialogTitle;
    }

    public UpdateBean setNewAppUpdateDialogTitle(String newAppUpdateDialogTitle) {
        this.newAppUpdateDialogTitle = newAppUpdateDialogTitle;
        return this;
    }

    public String getNewAppSize() {
        return newAppSize;
    }

    public UpdateBean setNewAppSize(String newAppSize) {
        this.newAppSize = newAppSize;
        return this;
    }


    //----------------------------

    protected UpdateBean(Parcel in) {
        isUpdate = in.readByte() != 0;
        isForce = in.readByte() != 0;
        newAppVersion = in.readString();
        newAppUpdateLog = in.readString();
        newAppUpdateDialogTitle = in.readString();
        newAppSize = in.readString();
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
    }
}
