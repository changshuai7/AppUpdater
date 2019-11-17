package com.shuai.app.appupdater.dialog;

import android.app.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.shuai.appupdater.core.UpdateConfig;

/**
 * Created by changshuai on 2019/11/17.
 *
 */

public class UpdateManager {

    final static String INTENT_KEY_UPDATE_BEAN = "intent_key_update_bean";
    final static String INTENT_KEY_UPDATE_CONFIG = "intent_key_update_config";
    final static String KEY_THEME = "key_theme";
    final static String KEY_TOP_IMAGE = "key_top_image";

    private static final String TAG = UpdateManager.class.getSimpleName();

    private Activity mActivity;

    private UpdateBean mUpdateBean;
    private UpdateConfig mUpdateConfig;

    private int mThemeColor;//按钮、进度条颜色
    private
    @DrawableRes
    int mTopPic;//顶部图片

    private UpdateManager(Builder builder) {
        mActivity = builder.getActivity();
        mUpdateConfig = builder.getUpdateConfig();
        mThemeColor = builder.getThemeColor();
        mTopPic = builder.getTopPic();
        mUpdateBean = builder.getUpdateAppBean();

    }

    public static class Builder {

        private UpdateConfig mUpdateConfig;
        private UpdateBean mUpdateBean;
        private Activity mActivity;
        //设置按钮，进度条的颜色
        private int mThemeColor = 0;
        //顶部的图片
        private
        @DrawableRes
        int mTopPic = 0;

        public UpdateConfig getUpdateConfig(){
            return mUpdateConfig;
        }
        public Builder setUpdateConfig(UpdateConfig config){
            this.mUpdateConfig = config;
            return this;
        }


        public Activity getActivity() {
            return mActivity;
        }
        public Builder setActivity(Activity activity) {
            mActivity = activity;
            return this;
        }

        public UpdateBean getUpdateAppBean(){
            return mUpdateBean;
        }
        public Builder setUpdateBean(UpdateBean updateBean){
            this.mUpdateBean = updateBean;
            return this;
        }


        public int getThemeColor() {
            return mThemeColor;
        }
        public Builder setThemeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        public int getTopPic() {
            return mTopPic;
        }
        public Builder setTopPic(int topPic) {
            mTopPic = topPic;
            return this;
        }


        /**
         * @return 生成app管理器
         */
        public UpdateManager build() {
            //校验
            if (getActivity() == null  || getUpdateConfig()==null ) {
                throw new NullPointerException("必要参数不能为空");
            }
            if (TextUtils.isEmpty(getUpdateConfig().getPath())) {
                //sd卡是否存在
                String path = "";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
                    try {
                        path = getActivity().getExternalCacheDir().getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(path)) {
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    }
                } else {
                    path = getActivity().getCacheDir().getAbsolutePath();
                }
                getUpdateConfig().setPath(path);
            }

            return new UpdateManager(this);
        }

    }

    public void start(){
        showDialogFragment();
    }

    /**
     * 跳转到更新页面
     */
    private void showDialogFragment() {

        //校验
        if (!verify()){
            return;
        }

        if (mActivity != null && !mActivity.isFinishing()) {
            Bundle bundle = new Bundle();
            //添加信息，
            if (mUpdateBean!=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_BEAN, mUpdateBean);
            }
            if (mThemeColor != 0) {
                bundle.putInt(KEY_THEME, mThemeColor);
            }

            if (mTopPic != 0) {
                bundle.putInt(KEY_TOP_IMAGE, mTopPic);
            }

            if (mUpdateConfig !=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_CONFIG, mUpdateConfig);
            }

            if (mUpdateBean!=null && mUpdateBean.isUpdate()){
                UpdateDialogFragment
                        .newInstance(bundle)
                        .show(((FragmentActivity) mActivity).getSupportFragmentManager(), "dialog");
            }


        }
    }

    private boolean verify() {
//        //版本忽略
//        if (mShowIgnoreVersion && AppUpdateUtils.isNeedIgnore(mActivity, mUpdateApp.getNewVersion())) {
//            return true;
//        }
//
//        if (TextUtils.isEmpty(mTargetPath)
////                || !mTargetPath.startsWith(preSuffix)
//                ) {
//            Log.e(TAG, "下载路径错误:" + mTargetPath);
//            return true;
//        }
//        return mUpdateApp == null;
        return true;
    }

}
