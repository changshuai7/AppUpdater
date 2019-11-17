package com.shuai.appupdater.core.callback;


public abstract class AppUpdateCallback implements UpdateCallback {
    @Override
    public void onDownloading(boolean isDownloading) {

    }

    @Override
    public void onStart(String url) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onCancel() {

    }
}
