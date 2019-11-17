package com.shuai.app.appupdater.dialog;

import java.io.Serializable;

/**
 * 版本信息
 */
public class UpdateBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isUpdate;   //是否有新版本（控制是否执行弹窗更新）
    private boolean isForce;    //是否强制更新

    private String newAppVersion;           //新版本号："1.2.3"
    private String newAppUpdateLog;         //更新日志："1、优化性能\n 2、修复bug"
    private String newAppUpdateDialogTitle; //配置dialog的title
    private String newAppSize;              //新app大小："100M"
    private String newAppMd5;               //Md5   ："abcdefsadf"

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
}
