package com.shuai.appupdater.dialog;

import android.app.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.shuai.appupdater.core.UpdateConfig;

/**
 * 升级管理器
 *
 * @author changshuai
 */

public class UpdateManager {

    final static String INTENT_KEY_UPDATE_CONFIG = "intent_key_update_config";
    final static String INTENT_KEY_UPDATE_BEAN = "intent_key_update_bean";
    final static String INTENT_KEY_UPDATE_DIALOG_BEAN = "intent_key_update_dialog_bean";
    final static String INTENT_KEY_UPDATE_DIALOG_LISTENER = "intent_key_update_dialog_listener";

    private static final String TAG = UpdateManager.class.getSimpleName();

    private Activity mActivity;

    private UpdateBean mUpdateBean;
    private UpdateConfig mUpdateConfig;
    private UpdateDialogBean mUpdateDialogBean;
    private UpdateDialogListener mUpdateDialogListener;

    private UpdateManager(Builder builder) {
        mActivity = builder.getActivity();
        mUpdateConfig = builder.getUpdateConfig();
        mUpdateDialogBean = builder.getUpdateDialogBean();
        mUpdateBean = builder.getUpdateAppBean();
        mUpdateDialogListener = builder.getUpdateDialogListener();

    }

    public static class Builder {

        private Activity mActivity;
        private UpdateConfig mUpdateConfig;
        private UpdateBean mUpdateBean;
        private UpdateDialogBean mUpdateDialogBean;
        private UpdateDialogListener mUpdateDialogListener;

        public Activity getActivity() {
            return mActivity;
        }
        public Builder setActivity(Activity activity) {
            mActivity = activity;
            return this;
        }

        public UpdateDialogBean getUpdateDialogBean() {
            return mUpdateDialogBean;
        }

        public Builder setUpdateDialogBean(UpdateDialogBean mUpdateDialogBean) {
            this.mUpdateDialogBean = mUpdateDialogBean;
            return this;
        }

        public UpdateConfig getUpdateConfig(){
            return mUpdateConfig;
        }
        public Builder setUpdateConfig(UpdateConfig config){
            this.mUpdateConfig = config;
            return this;
        }

        public UpdateBean getUpdateAppBean(){
            return mUpdateBean;
        }
        public Builder setUpdateBean(UpdateBean updateBean){
            this.mUpdateBean = updateBean;
            return this;
        }

        public UpdateDialogListener getUpdateDialogListener() {
            return mUpdateDialogListener;
        }
        public Builder setUpdateDialogListener(UpdateDialogListener mUpdateDialogListener) {
            this.mUpdateDialogListener = mUpdateDialogListener;
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
        if (!verify()) return;

        if (mActivity != null && !mActivity.isFinishing()) {
            Bundle bundle = new Bundle();
            //添加信息，
            if (mUpdateBean!=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_BEAN, mUpdateBean);
            }

            if (mUpdateDialogBean!=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_DIALOG_BEAN,mUpdateDialogBean);
            }

            if (mUpdateConfig !=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_CONFIG, mUpdateConfig);
            }

            if (mUpdateDialogListener !=null){
                bundle.putParcelable(INTENT_KEY_UPDATE_DIALOG_LISTENER,mUpdateDialogListener);
            }

            if (mUpdateBean!=null && mUpdateBean.isUpdate()){
                UpdateDialogFragment
                        .newInstance(bundle)
                        .show(((FragmentActivity) mActivity).getSupportFragmentManager(), "dialog");
            }

        }
    }

    private boolean verify() {

        if (mUpdateConfig == null){
            Toast.makeText(mActivity, "UpdateConfig配置不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mUpdateBean == null){
            Toast.makeText(mActivity, "UpdateBean配置不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
