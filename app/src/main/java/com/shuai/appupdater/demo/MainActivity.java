package com.shuai.appupdater.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shuai.appupdater.core.http.HttpManager;
import com.shuai.appupdater.core.http.IHttpManager;
import com.shuai.appupdater.dialog.UpdateBean;
import com.shuai.appupdater.dialog.UpdateDialogBean;
import com.shuai.appupdater.dialog.UpdateDialogListener;
import com.shuai.appupdater.dialog.UpdateManager;
import com.shuai.appupdater.core.AppUpdater;
import com.shuai.appupdater.core.UpdateConfig;
import com.shuai.appupdater.core.callback.AppUpdateCallback;
import com.shuai.appupdater.core.callback.UpdateCallback;
import com.shuai.appupdater.core.constant.Constants;
import com.shuai.appupdater.core.util.PermissionUtils;

import java.io.File;
import java.util.Map;


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

    /////////////////////////////////////////////  app-updater-dialog  ///////////////////////////////////////////////////

    /**
     * app-updater-dialog库中的功能
     * 启动默认弹框升级
     */
    private void btnDialogUpdate() {

        /**设置升级功能配置*/
        UpdateConfig config = new UpdateConfig()
                //配置升级下载地址URL
                .setUrl(mUrl)
                //配置要下载的MD5，如果校验不通过，则执行异常回调。不传入则默认不校验
                .setFileMD5("1B64E40002948FF446D00517E59D9D49")
                //配置下载地址，若不配置，则存储在默认的位置
                .setPath(Environment.getExternalStorageDirectory() + File.separator + "MyApp")
                //配置下载文件的命名，若不配置，则取URL上声明的名字
                .setFilename("test.apk")
                //设置要下载的VersionCode,设置versionCode之后，版本相同的apk只下载一次,优先取本地缓存。不设置则不读取缓存。
                .setVersionCode(1)
                //设置下载的请求头。有些特殊的下载地址需要配置请求头，请在这里添加
                .addHeader("header-test", "header1")
                //配置是否通知栏展示
                .setShowNotification(true)
                //设置是否下载完成以后自动安装
                .setInstallApk(true);


        /**设置更新数据的配置*/
        UpdateBean bean = new UpdateBean()
                //设置是否执行更新
                .setUpdate(true)
                //设置是否执行强制更新
                .setForce(false)
                //设置要下载apk的版本
                .setNewAppVersion("1.0.0")
                //设置更新Title。需要注意的是，如果这里传入了title，那么会覆盖形如："发现新版本1.0.0"的标题。
                //.setNewAppUpdateDialogTitle("又要升级了，你准备好了吗？")
                //设置要下载apk的大小.不设置则不展示
                .setNewAppSize("20.5M")
                //设置更新日志
                .setNewAppUpdateLog(
                        "1、优化系统性能\n" +
                                "2、bug都是petter写的\n" +
                                "3、CS最帅\n" +
                                "4、吊炸天的帅气\n" +
                                "5、上官婉儿无敌帅\n");


        /**设置和弹框相关的*/
        UpdateDialogBean dialogBean = new UpdateDialogBean()
                //设置弹框的主题色.包括按钮,进度条的颜色等
                //.setThemeColor(getResources().getColor(R.color.colorAccent))
                //设置弹框的顶部图片.不设置默认是红色火箭头
                .setTopPic(R.mipmap.lib_update_app_top_bg);

        /**监听弹框的点击和升级回调等事件*/
        UpdateDialogListener listener = new UpdateDialogListener() {

            // 点击了下载按钮
            @Override
            public void onClickDialogConfirm(View view) {
                Toast.makeText(MainActivity.this, "点击了确定更新", Toast.LENGTH_SHORT).show();
            }
            // 点击了取消按钮
            @Override
            public void onClickDialogCancel(View view) {
                Toast.makeText(MainActivity.this, "点击了取消更新", Toast.LENGTH_SHORT).show();
            }
            // 必要权限被拒绝
            @Override
            public void onPermissionDenied() {
                //小米手机有点恶心，为保证库正常工作，请请务必先获取到相关权限。
                Toast.makeText(MainActivity.this, "必要权限被拒绝！", Toast.LENGTH_LONG).show();
            }

            // 在开始下载前调用(在onStart之前调用) true 表示已经在下载，false表示准备刚调用下载
            @Override
            public void onUpdateIsDownloading(boolean isDownloading) {
                if (isDownloading) {
                    Toast.makeText(MainActivity.this, "已经在下载中,请勿重复下载。", Toast.LENGTH_SHORT).show();
                }
            }
            // 开始下载
            @Override
            public void onUpdateStart(String url) {
                Toast.makeText(MainActivity.this, "下载：start", Toast.LENGTH_SHORT).show();
            }
            // 下载进度回调
            @Override
            public void onUpdateProgress(int progress, int total, boolean isChange) {
                Log.d("下载进度：","progress = "+progress+" , total = "+total+" , isChange = "+isChange);
            }
            // 下载完成回调
            @Override
            public void onUpdateFinish(File file) {
                Toast.makeText(MainActivity.this, "下载：finish", Toast.LENGTH_SHORT).show();
            }
            // 下载出错回调（注意：如果要下载的文件MD5校验出错，那么会在此处回调）
            @Override
            public void onUpdateError(Exception e) {
                Toast.makeText(MainActivity.this, "下载出错："+e.toString(), Toast.LENGTH_SHORT).show();
            }
            // 取消下载回调
            @Override
            public void onUpdateCancel() {
                Toast.makeText(MainActivity.this, "下载：取消", Toast.LENGTH_SHORT).show();
            }
        };


        /**传入各种配置和参数，执行弹窗更新*/
        UpdateManager build = new UpdateManager
                .Builder()
                .setActivity(this)
                .setUpdateConfig(config)
                .setUpdateBean(bean)
                .setUpdateDialogBean(dialogBean)
                .setUpdateDialogListener(listener)
                .build();
        build.start();
    }

    /////////////////////////////////////////////  app-updater-core  ///////////////////////////////////////////////////

    /**
     * app-updater-core库中的功能
     * 简易执行后台升级
     */
    private void btnCoreEasy() {
        //这里展示了最简易的升级方式。无任何配置传入
        mAppUpdater = new AppUpdater(getContext(), mUrl);
        mAppUpdater.start();
    }

    /**
     * app-updater-core库中的功能
     * 下载并监听进度
     */
    private void btnCore1() {
        UpdateConfig config = new UpdateConfig();
        config.setSound(true);
        config.setUrl(mUrl);
        config.addHeader("header-test", "header");
        mAppUpdater = new AppUpdater(getContext(), config);
//        mAppUpdater.setHttpManager(new IHttpManager() {
//            @Override
//            public void download(String url, String path, String filename, String fileMd5, @Nullable Map<String, String> requestProperty, DownloadCallback callback) {
//                //执行更新操作
//            }
//
//            @Override
//            public void cancel() {
//                //取消更新操作
//            }
//        });
        mAppUpdater.setUpdateCallback(new UpdateCallback() {

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
     * app-updater-core库中的功能
     * 系统弹框升级
     */
    private void btnCore2() {
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
     * app-updater-core库中的功能
     * 取消升级
     */
    private void btnCoreCancel() {
        if (mAppUpdater != null) {
            mAppUpdater.stop();
        }

    }



    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_core_easy:
                btnCoreEasy();
                break;
            case R.id.btn_core_1:
                btnCore1();
                break;
            case R.id.btn_core_2:
                btnCore2();
                break;
            case R.id.btn_core_cancel:
                btnCoreCancel();
                break;
            case R.id.btn_update_dialog:
                btnDialogUpdate();
                break;
        }
    }
}
