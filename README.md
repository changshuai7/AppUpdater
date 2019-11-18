# AppUpdater:强大且通用的的AndroidApp版本升级库
## 写在前边

本库专注于完成AndroidApp版本升级的功能，具有简约、稳定、并且高扩展的优势。


## 一、框架结构


| 项目 |功能  |
| --- | --- |
| app | 示例app |
| app-updater-core | 版本升级核心库 |
| app-updater-dialog | 版本升级默认弹框库 |



## 二、集成方式

### 1.引入

```
dependencies {
        //核心库，必须依赖。
        implementation 'com.shuai:app-updater-core:version'
        //弹框库，选择依赖。
        implementation 'com.shuai:app-updater-core:version'
}

版本号version 一般采用Tag中最新的版本。两者同步更新
```
需要说明的是：

如果你只需要用到版本更新中app的下载、安装、进度回调、通知栏显示等核心功能，UI弹窗要自己开发，那么直接依赖core库即可。你需要对版本升级弹窗做更多功能和稳定性的调试，这可能需要花费更多的时间。

如果你想直接使用为你写好的一个稳定美观的弹窗，除了依赖core库以外，还需要依赖dialog库。dialog库高度封装了一个版本升级的弹窗，使用非常便捷。

### 3.前提

由于涉及到下载功能，请提前申请存储权限

### 2.核心库core的使用
#### 简易使用：

```
AppUpdater mAppUpdater = new AppUpdater(getContext(), mUrl);//mUrl为下载地址
mAppUpdater.start();
```
只需要一行代码，即可完成下载安装。内部封装了一些列的默认配置

#### 传入配置使用：

通常简易的版本升级是无法满足大部分的使用场景的。那么就需要对升级进行配置。代码如下：

```
//创建配置UpdateConfig
UpdateConfig config = new UpdateConfig()
                //配置升级下载地址URL
                .setUrl(mUrl)
                //配置要下载的MD5，如果校验不通过，则执行异常回调。不传入则默认不校验
                .setFileMD5("1B64E40002948FF446D00517E59D9D49")
                //配置下载地址，若不配置，则存在默认的位置
                .setPath(Environment.getExternalStorageDirectory() + File.separator + "MyApp")
                //配置下载文件的命名，若不配置，则取URL上声明的名字
                .setFilename("test.apk")
                //设置要下载的VersionCode,设置versionCode之后，版本相同的apk只下载一次,优先取本地缓存。不设置则不读取缓存。
                .setVersionCode(1)
                //设置下载的请求头。有些特殊的下载地址需要配置请求头，请在这里添加
                .addHeader("header-test", "header1")
                //配置是否通知栏展示进度
                .setShowNotification(true)
                //设置是否下载完成以后自动安装
                .setInstallApk(true);


//将配置传入AppUpdater
AppUpdater mAppUpdater = new AppUpdater(getContext(), config);

//执行更新
mAppUpdater.start();
```
上面列出了一些常用的配置，更多的配置，请参考源码中非常详细的注释。

#### 设置监听：

库core可以对下载进度监听，方便你在UI中更新进度条、埋点等一系列操作。可扩展性非常强。

```
//设置下载监听
mAppUpdater.setUpdateCallback(new UpdateCallback() {
    @Override
    public void onDownloading(boolean isDownloading) {
       //最开始调用(在onStart之前调用)true 表示已经在下载，false表示准备刚调用下载
    }
    @Override
    public void onStart(String url) {
       //开始
    }
    @Override
    public void onProgress(int progress, int total, boolean isChange) {
       //加载进度…
       //isChange表示进度百分比是否有改变,主要可以用来过滤无用的刷新，从而降低刷新频率
    }
    @Override
    public void onFinish(File file) {
        //下载完成
    }
    @Override
    public void onError(Exception e) {
       //下载出错（包括MD5校验失败，也会在此回调）
    }
    @Override
    public void onCancel() {
       //下载取消
    }
});
```

#### 自定义网络请求框架：

为保证最少三方库依赖度（内部只依赖了V7包），库core内默认使用了HttpsURLConnection进行下载的网络请求。如果你想使用OkHttp等方式请求，请实现IHttpManager，覆盖download方法（下载功能）和cancel方法（取消下载）。将IHttpManager的实现类传入AppUpdater中即可。详细可以模仿或者参考内部已经写好的实现类HttpManager。

```
mAppUpdater.setHttpManager(new IHttpManager() {
    @Override
    public void download(String url, String path, String filename, String fileMd5, @Nullable Map<String, String> requestProperty, DownloadCallback callback) {
        //利用okhttp等网络框架执行更新操作
    }

    @Override
    public void cancel() {
        //取消更新操作
    }
});
```

### 3.扩展库dialog的使用
核心库中只封装了版本更新的功能，并未对弹窗UI做任何的封装。
**这样也使得UI部分和版本升级功能部分实现了彻底解耦。**
为了方便集成和快速使用，扩展库dialog内则封装了一个可使用的稳定的dialog。可以不用做任何UI的工作，完成版本升级的集成。

**注意：集成扩展库dialog的同时，必须集成核心库core**

#### 步骤一：配置UpdateConfig（必须）
UpdateConfig即为核心库core内部的版本升级配置
```
/**设置升级功能配置*/
UpdateConfig config = new UpdateConfig()
        //配置升级下载地址URL
        .setUrl(mUrl)
        //配置要下载的MD5，如果校验不通过，则执行异常回调。不传入则默认不校验
        .setFileMD5("1B64E40002948FF446D00517E59D9D49")
        //配置下载地址，若不配置，则存在默认的位置
        .setPath(Environment.getExternalStorageDirectory() + File.separator + "MyApp")
        //配置下载文件的命名，若不配置，则取URL上声明的名字
        .setFilename("test.apk")
        //设置要下载的VersionCode,设置versionCode之后，版本相同的apk只下载一次,优先取本地缓存。不设置则不读取缓存。
        .setVersionCode(1)
        //设置下载的请求头。有些特殊的下载地址需要配置请求头，请在这里添加
        .addHeader("header-test", "header1")
        //配置是否通知栏展示进度
        .setShowNotification(true)
        //设置是否下载完成以后自动安装
        .setInstallApk(true);

```

#### 步骤二：配置UpdateBean（必须）
UpdateBean为版本升级中包括是否更新、是否强制更新、更新日志等常用配置
```
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
```
#### 步骤三：配置UpdateDialogBean（可选）
UpdateDialogBean为版本更新弹窗的样式配置
```
/**设置和弹框相关的*/
UpdateDialogBean dialogBean = new UpdateDialogBean()
        //设置弹框的主题色.包括按钮,进度条的颜色等
        //.setThemeColor(getResources().getColor(R.color.colorAccent))
        //设置弹框的顶部图片.不设置默认是红色火箭头
        .setTopPic(R.mipmap.lib_update_app_top_bg);
```

#### 步骤四：配置UpdateDialogListener（可选）
UpdateDialogListener为版本更新中，弹框的点击事件、下载的进度、权限的拒绝等监听配置。
```
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
```
#### 步骤四：整合配置
将所有配置整合传入UpdateManager，通过start即可启动弹窗。
```
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
```