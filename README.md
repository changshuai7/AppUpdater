# AppUpdater

对于http请求报错，解决方案：https://blog.csdn.net/gengkui9897/article/details/82863966
BUG:后台来回切换，dialog偶尔消失。

BUG:下载好以后，点击会直接安装，MD5校验如何做？

BUG:路径位置不可配置。默认路径在哪里？

进度可以监听吗？对于下载的进度处理，比如下载错误的弹框等。

TextUtils.isEmpty替换成LangUtil.

增加一个安装并校验MD5的InstallApkWithCheckMD5的方法。考虑是否将“是否校验”作为参数true/false传入


