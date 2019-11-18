package com.shuai.appupdater.dialog;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;


/**
 * 和Dialog相关配置
 */
public class UpdateDialogBean implements Parcelable {

    //设置按钮，进度条的颜色
    private int mThemeColor = 0;  //TODO 考虑能否统一用R.mipmap形式传入
    //顶部的图片
    private
    @DrawableRes
    int mTopPic = 0;

    public UpdateDialogBean() {
    }


    public int getThemeColor() {
        return mThemeColor;
    }

    public UpdateDialogBean setThemeColor(int mThemeColor) {
        this.mThemeColor = mThemeColor;
        return this;
    }

    public int getTopPic() {
        return mTopPic;
    }


    protected UpdateDialogBean(Parcel in) {
        mThemeColor = in.readInt();
        mTopPic = in.readInt();
    }


    public UpdateDialogBean setTopPic(int mTopPic) {
        this.mTopPic = mTopPic;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mThemeColor);
        dest.writeInt(mTopPic);
    }


    public static final Creator<UpdateDialogBean> CREATOR = new Creator<UpdateDialogBean>() {
        @Override
        public UpdateDialogBean createFromParcel(Parcel in) {
            return new UpdateDialogBean(in);
        }

        @Override
        public UpdateDialogBean[] newArray(int size) {
            return new UpdateDialogBean[size];
        }
    };
}
