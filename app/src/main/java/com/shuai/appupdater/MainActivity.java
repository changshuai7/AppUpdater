package com.shuai.appupdater;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shuai.app.appupdater.dialog.UpdateBean;
import com.shuai.app.appupdater.dialog.UpdateManager;
import com.shuai.appupdater.core.AppUpdater;
import com.shuai.appupdater.core.UpdateConfig;
import com.shuai.appupdater.core.callback.AppUpdateCallback;
import com.shuai.appupdater.core.callback.UpdateCallback;
import com.shuai.appupdater.core.constant.Constants;
import com.shuai.appupdater.core.util.PermissionUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final Object mLock = new Object();

    private String mUrl = "https://b6.market.xiaomi.com/download/AppStore/0f9d947609a19a48069dcef4106f4dea13540ed9f/com.ss.android.article.lite.apk";

    private ProgressBar progressBar;

    private Toast toast;

    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        PermissionUtils.INSTANCE.verifyReadAndWritePermissions(this, Constants.RE_CODE_STORAGE_PERMISSION);
    }

    public Context getContext() {
        return this;
    }

    public void showToast(String text) {
        if (toast == null) {
            synchronized (mLock) {
                if (toast == null) {
                    toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(text);
        toast.show();
    }

    /**
     * 简单一键后台升级
     */
    private void clickBtn1() {
        mAppUpdater = new AppUpdater(getContext(), mUrl);
        mAppUpdater.start();
    }

    /**
     * 一键下载并监听
     */
    private void clickBtn2() {
        UpdateConfig config = new UpdateConfig();
        config.setSound(true);
        config.setUrl(mUrl);
        config.addHeader("token", "xxxxxx");
        mAppUpdater = new AppUpdater(getContext(), config)
                .setUpdateCallback(new UpdateCallback() {

                    @Override
                    public void onDownloading(boolean isDownloading) {
                        if (isDownloading) {
                            showToast("已经在下载中,请勿重复下载。");
                        }
                    }

                    @Override
                    public void onStart(String url) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onProgress(int progress, int total, boolean isChange) {
                        if (isChange) {
                            progressBar.setMax(total);
                            progressBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mAppUpdater.start();
    }

    /**
     * 系统弹框升级
     */
    private void clickBtn3() {
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAppUpdater = new AppUpdater.Builder()
                                .serUrl(mUrl)
                                .build(getContext())
                                .setUpdateCallback(new AppUpdateCallback() {
                                    @Override
                                    public void onProgress(int progress, int total, boolean isChange) {

                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        showToast("下载完成");
                                    }
                                });
                        mAppUpdater.start();
                    }
                }).show();
    }


    private void clickCancel() {
        if (mAppUpdater != null) {
            mAppUpdater.stop();
        }

    }

    private void clickNew() {



        UpdateConfig config = new UpdateConfig();
        config.setSound(true);
        config.setUrl(mUrl);
//        config.setFileMD5("75E9EC2D28780E206DE8AD2068461664");
//        config.setVersionCode(721);////设置versionCode之后，新版本相同的apk只下载一次,优先取本地缓存。
        config.addHeader("token", "xxxxxx");
        config.setInstallApk(true);


        UpdateBean bean = new UpdateBean();
        bean.setUpdate(true);
        bean.setForce(false);
        bean.setNewAppVersion("1.2.3");
        bean.setNewAppUpdateLog("1、优化系统性能\n2、bug都是petter写的\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n1、优化系统性能\n");
//        bean.setNewAppUpdateDialogTitle("又要升级了，你准备好了吗？");
        bean.setNewAppSize("100M");
        UpdateManager build = new UpdateManager
                .Builder()
                //当前Activity
                .setActivity(this)
                .setUpdateConfig(config)
                .setUpdateBean(bean)
//                .setTopPic(R.mipmap.test_bg)
//                .setThemeColor(getResources().getColor(R.color.colorAccent))
                .build();
        build.start();
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                clickBtn1();
                break;
            case R.id.btn2:
                clickBtn2();
                break;
            case R.id.btn3:
                clickBtn3();
                break;
            case R.id.btnCancel:
                clickCancel();
                break;
            case R.id.btnNewDialog:
                clickNew();
                break;
        }
    }
}
