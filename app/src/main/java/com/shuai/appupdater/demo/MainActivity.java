package com.shuai.appupdater.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shuai.appupdater.dialog.UpdateBean;
import com.shuai.appupdater.dialog.UpdateManager;
import com.shuai.appupdater.core.AppUpdater;
import com.shuai.appupdater.core.UpdateConfig;
import com.shuai.appupdater.core.callback.AppUpdateCallback;
import com.shuai.appupdater.core.callback.UpdateCallback;
import com.shuai.appupdater.core.constant.Constants;
import com.shuai.appupdater.core.util.PermissionUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final Object mLock = new Object();

    private String mUrl = "http://7jpqno.com2.z0.glb.clouddn.com/motion/1573991245851/App_Updater.apk";

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
     * 后台升级
     */
    private void clickBtn1() {
        mAppUpdater = new AppUpdater(getContext(), mUrl);
        mAppUpdater.start();
    }

    /**
     * 下载并监听进度
     */
    private void clickBtn2() {
        UpdateConfig config = new UpdateConfig();
        config.setSound(true);
        config.setUrl(mUrl);
        config.addHeader("token", "abcdef");
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


    /**
     * 取消升级
     */
    private void clickCancel() {
        if (mAppUpdater != null) {
            mAppUpdater.stop();
        }

    }

    /**
     * 启动默认弹框
     */
    private void clickStartDialog() {

        /**设置升级功能配置*/
        UpdateConfig config = new UpdateConfig();
        config.setUrl(mUrl);
        config.setFileMD5("75E9EC2D28780E206DE8AD2068461664");
        config.setVersionCode(1);////设置versionCode之后，新版本相同的apk只下载一次,优先取本地缓存。
        config.addHeader("token", "xxxxxx");
        config.setReDownload(false);


        /**设置更新弹框的配置*/
        UpdateBean bean = new UpdateBean();
        bean.setUpdate(true);
        bean.setForce(false);
        bean.setNewAppVersion("1.0.0");
        bean.setNewAppUpdateLog(
                        "1、优化系统性能\n" +
                        "2、bug都是petter写的\n" +
                        "3、CS最帅\n" +
                        "4、吊炸天的帅气\n" +
                        "5、上官婉儿无敌帅\n" );
//        bean.setNewAppUpdateDialogTitle("又要升级了，你准备好了吗？");
        bean.setNewAppSize("20.5M");
        UpdateManager build = new UpdateManager
                .Builder()
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
            case R.id.btnStartDialog:
                clickStartDialog();
                break;
        }
    }
}
