package com.shuai.appupdater.core;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.shuai.appupdater.core.constant.Constants;

import java.util.HashMap;
import java.util.Map;

public class UpdateConfig implements Parcelable {

    private String mUrl;                //下载路径
    private String mPath;               //文件保存路径
    private String mFilename;           //保存文件名
    private String mFileMD5;            //要下载apk的md5.如果不配置md5,则不校验。若配置下载apk的md5，校验不通过则无法安装。

    private boolean isShowNotification = true;      //是否显示通知栏.默认是
    private boolean isInstallApk = true;            //下载完成后是否自动弹出安装.默认是
    private int mNotificationIcon;                  //通知栏图标.默认取app图标
    private int mNotificationId = Constants.DEFAULT_NOTIFICATION_ID;//通知栏ID
    private String mChannelId;                      //通知栏渠道ID
    private String mChannelName;                    //通知栏渠道名称
    private String mAuthority;                      //默认{@link Context#getPackageName() + ".fileProvider"}
    private boolean isReDownload = false;           //下载失败是否支持点击通知栏重复下载.默认false-->true有bug
    private boolean isShowPercentage = true;        //是否显示百分比
    private boolean isVibrate;                      //是否震动提示，为true时使用通知默认震动
    private boolean isSound;                        //是否铃声提示,为true时使用通知默认铃声
    private Integer mVersionCode;                   //要下载的APK的versionCode
    private Map<String,String> mRequestProperty;    //下载请求头参数
    private boolean isDeleteCancelFile = true;      //是否删除取消下载的文件 //TODO 这个好像无效。

    public UpdateConfig() {

    }

    public UpdateConfig build(){
        return this;
    }

    public String getFileMD5() {
        return mFileMD5;
    }

    public UpdateConfig setFileMD5(String mFileMD5) {
        this.mFileMD5 = mFileMD5;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public UpdateConfig setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public String getPath() {
        return mPath;
    }

    public UpdateConfig setPath(String path) {
        this.mPath = path;
        return this;
    }

    public String getFilename() {
        return mFilename;
    }

    public UpdateConfig setFilename(String filename) {
        this.mFilename = filename;
        return this;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public UpdateConfig setShowNotification(boolean isShowNotification) {
        this.isShowNotification = isShowNotification;
        return this;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public UpdateConfig setChannelId(String channelId) {
        this.mChannelId = channelId;
        return this;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public UpdateConfig setChannelName(String channelName) {
        this.mChannelName = channelName;
        return this;
    }

    public UpdateConfig setNotificationId(int notificationId){
        this.mNotificationId = notificationId;
        return this;
    }

    public int getNotificationId(){
        return this.mNotificationId;
    }

    public UpdateConfig setNotificationIcon(@DrawableRes int icon){
        this.mNotificationIcon = icon;
        return this;
    }

    public int getNotificationIcon(){
        return this.mNotificationIcon;
    }

    public boolean isInstallApk() {
        return isInstallApk;
    }

    public UpdateConfig setInstallApk(boolean isInstallApk) {
        this.isInstallApk = isInstallApk;
        return this;
    }

    public String getAuthority() {
        return mAuthority;
    }

    public UpdateConfig setAuthority(String authority) {
        this.mAuthority = authority;
        return this;
    }

    public boolean isShowPercentage() {
        return isShowPercentage;
    }

    public UpdateConfig setShowPercentage(boolean showPercentage) {
        isShowPercentage = showPercentage;
        return this;
    }

    public boolean isReDownload() {
        return isReDownload;
    }

    public UpdateConfig setReDownload(boolean reDownload) {
        isReDownload = reDownload;
        return this;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public UpdateConfig setVibrate(boolean vibrate) {
        isVibrate = vibrate;
        return this;
    }

    public boolean isSound() {
        return isSound;
    }

    public UpdateConfig setSound(boolean sound) {
        isSound = sound;
        return this;
    }

    public Integer getVersionCode(){
        return mVersionCode;
    }

    public UpdateConfig setVersionCode(Integer versionCode){
        this.mVersionCode = versionCode;
        return this;
    }

    public Map<String, String> getRequestProperty() {
        return mRequestProperty;
    }

    public UpdateConfig addHeader(String key, String value){
        initRequestProperty();
        mRequestProperty.put(key,value);
        return this;
    }

    public UpdateConfig addHeader(Map<String,String> headers){
        initRequestProperty();
        mRequestProperty.putAll(headers);
        return this;
    }

    private void initRequestProperty(){
        if(mRequestProperty == null){
            mRequestProperty = new HashMap<>();
        }
    }

    public boolean isDeleteCancelFile() {
        return isDeleteCancelFile;
    }

    public UpdateConfig setDeleteCancelFile(boolean deleteCancelFile) {
        isDeleteCancelFile = deleteCancelFile;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUrl);
        dest.writeString(this.mPath);
        dest.writeString(this.mFileMD5);
        dest.writeString(this.mFilename);
        dest.writeByte(this.isShowNotification ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInstallApk ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mNotificationIcon);
        dest.writeInt(this.mNotificationId);
        dest.writeString(this.mChannelId);
        dest.writeString(this.mChannelName);
        dest.writeString(this.mAuthority);
        dest.writeByte(this.isReDownload ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowPercentage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVibrate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSound ? (byte) 1 : (byte) 0);
        dest.writeValue(this.mVersionCode);
        dest.writeInt(mRequestProperty!=null ? this.mRequestProperty.size() : 0);
        if(mRequestProperty!=null){
            for (Map.Entry<String, String> entry : this.mRequestProperty.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }
        dest.writeByte(this.isDeleteCancelFile ? (byte) 1 : (byte) 0);
    }

    protected UpdateConfig(Parcel in) {
        this.mUrl = in.readString();
        this.mPath = in.readString();
        this.mFileMD5 = in.readString();
        this.mFilename = in.readString();
        this.isShowNotification = in.readByte() != 0;
        this.isInstallApk = in.readByte() != 0;
        this.mNotificationIcon = in.readInt();
        this.mNotificationId = in.readInt();
        this.mChannelId = in.readString();
        this.mChannelName = in.readString();
        this.mAuthority = in.readString();
        this.isReDownload = in.readByte() != 0;
        this.isShowPercentage = in.readByte() != 0;
        this.isVibrate = in.readByte() != 0;
        this.isSound = in.readByte() != 0;
        this.mVersionCode = (Integer) in.readValue(Integer.class.getClassLoader());
        int mRequestPropertySize = in.readInt();
        this.mRequestProperty = new HashMap<>(mRequestPropertySize);
        for (int i = 0; i < mRequestPropertySize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.mRequestProperty.put(key, value);
        }
        this.isDeleteCancelFile = in.readByte() != 0;
    }

    public static final Creator<UpdateConfig> CREATOR = new Creator<UpdateConfig>() {
        @Override
        public UpdateConfig createFromParcel(Parcel source) {
            return new UpdateConfig(source);
        }

        @Override
        public UpdateConfig[] newArray(int size) {
            return new UpdateConfig[size];
        }
    };
}
