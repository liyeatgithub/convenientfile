1.初始化:
class App : Application() {

  override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
		QOV.b(true)//默认打开保活,如果默认关闭注释掉
        QOV.init(this)
    }
}


2.保活开关 QOV.c(this,true)(不要重复调用)

3.修改字符串常量:分散到各个java文件.

	
4.防关联需要注意地方：
	1.包结构修改(打乱路径,文件名,资源名等)
	2.组件修改(activity,provider,service,receiver,instrumentation等见AndroidManifest.xml)
	3.jni函数,路径,so名字修改(注意jni的回调)
	4.无声音乐文件silent.mp3重命名,md5值不一样(修改md5值：https://www.strerr.com/cn/md5_edit.html)
	5.资源文件中有一张图片，用创建项目的logo 代替即可
	6.修改部分类中声明变量的名称。
	7.部分类去除无用方法，或者添加无用方法都可.
	8.带有keep的方法都在混淆文件(proguard-rules.pro)中keep住,去掉方法上面的@keep,防止@keep关联(有keep方法的类,类名是可以随便修改的,可以添加无用方法的).
	
5.使用常量字符加密工具(stringfog)加密

6.见lh文件注释

