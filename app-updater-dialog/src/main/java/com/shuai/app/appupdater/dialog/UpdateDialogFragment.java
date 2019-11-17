package com.shuai.app.appupdater.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuai.app.appupdater.dialog.utils.Util;
import com.shuai.app.dialog.R;
import com.shuai.app.appupdater.dialog.utils.ColorUtil;
import com.shuai.app.appupdater.dialog.utils.DrawableUtil;
import com.shuai.app.appupdater.dialog.widget.NumberProgressBar;
import com.shuai.appupdater.core.AppUpdater;
import com.shuai.appupdater.core.UpdateConfig;
import com.shuai.appupdater.core.callback.UpdateCallback;
import com.shuai.appupdater.core.util.AppUtils;

import java.io.File;

/**
 * 版本升级弹框
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static boolean isShow = false;   //弹框是否展示出来了

    private UpdateBean mUpdateApp;
    private UpdateConfig mUpdateConfig;

    private NumberProgressBar mNumberProgressBar;

    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private TextView mTvApkSize;

    private LinearLayout mLlClose;

    private int mDefaultColor = 0xffe94339;//默认色
    private int mDefaultPicResId = R.mipmap.lib_update_app_top_bg;
    private ImageView mTopIv;
    private Activity mActivity;

    public static UpdateDialogFragment newInstance(Bundle args) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        //setStyle(DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);

        mActivity = getActivity();

    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        //是否可以取消,会影响上面那条属性
//        setCancelable(false);
//        //window外可以点击,不拦截窗口外的事件
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (mUpdateApp != null && mUpdateApp.isForce()) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lib_update_app_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //新版本大小
        mTvApkSize = view.findViewById(R.id.tv_size);
        //标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);

        //点击监听
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mUpdateApp = getArguments().getParcelable(UpdateManager.INTENT_KEY_UPDATE_BEAN);
        mUpdateConfig =  getArguments().getParcelable(UpdateManager.INTENT_KEY_UPDATE_CONFIG);

        //设置主题色
        initTheme();

        if (mUpdateApp != null) {
            //弹出对话框
            final String dialogTitle = mUpdateApp.getNewAppUpdateDialogTitle();
            final String newVersion = mUpdateApp.getNewAppVersion();
            final String targetSize = mUpdateApp.getNewAppSize();
            final String updateLog = mUpdateApp.getNewAppUpdateLog();

            //更新内容
            mContentTextView.setText(TextUtils.isEmpty(updateLog) ? "暂无" : updateLog);
            //更新大小
            if (!TextUtils.isEmpty(targetSize)) {
                mTvApkSize.setVisibility(View.VISIBLE);
                mTvApkSize.setText(String.format("新版本大小：%s", targetSize));
            }

            //标题
            mTitleTextView.setText(TextUtils.isEmpty(dialogTitle) ? String.format("发现新版本：%s", newVersion) : dialogTitle);
            //强制更新
            if (mUpdateApp.isForce()) {
                mLlClose.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {

        final int color = getArguments().getInt(UpdateManager.KEY_THEME, -1);
        final int topResId = getArguments().getInt(UpdateManager.KEY_TOP_IMAGE, -1);

        if (-1 == topResId) {
            if (-1 == color) {
                //默认红色
                setDialogTheme(mDefaultColor, mDefaultPicResId);
            } else {
                setDialogTheme(color, mDefaultPicResId);
            }

        } else {
            if (-1 == color) {
////                自动提色.如果采用自动提色，则需要引入v7相关的包compile 'com.android.support:palette-v7:26.1.0'
//                Palette.from(Util.drawableToBitmap(this.getResources().getDrawable(topResId))).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        int mDominantColor = palette.getDominantColor(mDefaultColor);
//                        setDialogTheme(mDominantColor, topResId);
//                    }
//                });
                setDialogTheme(mDefaultColor, topResId);
            } else {
                //更加指定的上色
                setDialogTheme(color, topResId);
            }
        }


    }

    /**
     * 设置弹框主题
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackgroundDrawable(DrawableUtil.getDrawable(Util.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //字体颜色随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                    Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                } else {
                    // 申请授权。
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

            } else {
                updateApp();
            }

        } else if (i == R.id.iv_close) {
            dismiss();
        }
    }


    /**
     * 执行更新
     */
    private void updateApp() {
        AppUpdater mAppUpdater;
        mAppUpdater = new AppUpdater(mActivity, mUpdateConfig)
                .setUpdateCallback(new UpdateCallback() {

                    @Override
                    public void onDownloading(boolean isDownloading) {
                        if (isDownloading) {
                            Toast.makeText(mActivity, "已经在下载中,请勿重复下载。", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onStart(String url) {
                        mNumberProgressBar.setProgress(0);
                        mNumberProgressBar.setVisibility(View.VISIBLE);
                        mUpdateOkButton.setVisibility(View.GONE);
                    }

                    @Override
                    public void onProgress(int progress, int total, boolean isChange) {
                        if (isChange) {
                            if (!UpdateDialogFragment.this.isRemoving()) {
                                mNumberProgressBar.setVisibility(View.VISIBLE);
                                mNumberProgressBar.setProgress(/*Math.round(progress * 100)*/progress);
                                mNumberProgressBar.setMax(total);
                                mUpdateOkButton.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        final File apkFile = file;
                        if (!UpdateDialogFragment.this.isRemoving()) {
                            if (mUpdateApp.isForce()) {
                                mNumberProgressBar.setVisibility(View.GONE);
                                mUpdateOkButton.setText("安装");
                                mUpdateOkButton.setVisibility(View.VISIBLE);
                                mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //安装
                                        installApp(apkFile);
                                    }
                                });
                            } else {
                                dismissAllowingStateLoss();
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        //提示错误信息
                        Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
                        if (!UpdateDialogFragment.this.isRemoving()){
                            mNumberProgressBar.setVisibility(View.GONE);
                            mUpdateOkButton.setText("下载失败，点击重试");
                            mUpdateOkButton.setVisibility(View.VISIBLE);
                            mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //执行重试
                                    updateApp();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {
                        mNumberProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mAppUpdater.start();
    }

    /**
     * 安装app
     * @param file
     */
    private void installApp(File file){

        //TODO 安装前是需要校验MD5的
        if (!TextUtils.isEmpty(mUpdateConfig.getFileMD5())){
            if (!AppUtils.INSTANCE.checkApkMd5(mUpdateConfig.getFileMD5(),file)){
                Toast.makeText(mActivity, "MD5校验不通过", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String authority = mUpdateConfig.getAuthority();
        if(TextUtils.isEmpty(authority)){//如果为空则默认
            authority = getContext().getPackageName() + ".fileProvider";
        }
        AppUtils.INSTANCE.installApk(getContext(),file,authority);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //升级
                updateApp();
            } else {
                //提示，并且关闭
                Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                dismiss();

            }
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("", "对话框 requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
//        switch (resultCode) {
//            case Activity.RESULT_CANCELED:
//                switch (requestCode){
//                    // 得到通过UpdateDialogFragment默认dialog方式安装，用户取消安装的回调通知，以便用户自己去判断，比如这个更新如果是强制的，但是用户下载之后取消了，在这里发起相应的操作
//                    case AppUpdateUtils.REQ_CODE_INSTALL_APP:
//                        if (mUpdateApp.isConstraint()) {
//                            if (AppUpdateUtils.appIsDownloaded(mUpdateApp)) {
//                                AppUpdateUtils.installApp(UpdateDialogFragment.this, AppUpdateUtils.getAppFile(mUpdateApp));
//                            }
//                        }
//                        break;
//                }
//                break;
//
//            default:
//        }
//    }

    @Override
    public void show(FragmentManager manager, String tag) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        super.onDestroyView();
    }

}

