package com.hal.convenientfile.util
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/2
 */
class ChangedVersionLiveData<T> : MutableLiveData<T> {

    constructor() : super()

    constructor(t: T) : super(t)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        hook(observer)
    }

    private fun hook(observer: Observer<in T>) {

        // TODO 1.得到mLastVersion
        // 需要转成java的 class
        val liveDataClass = LiveData::class.java
        //先拿到字段mObservers
        // 获取到LivData的类中的mObservers字段(这个里面保存了二次封装的LifecycleBoundObserver)，
        // LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
        //     ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        val mObserversField: Field = liveDataClass.getDeclaredField("mObservers")
        mObserversField.isAccessible = true // 私有修饰也可以访问

        // 获取到这个成员变量的对象(Any == Object)：要想去拿某个字段，或者去拿某哦个函数，那么一定要去拿相应的对象
        // private SafeIterableMap<Observer<? super GlowEtT>, ObserverWrapper> mObservers =
        //       new SafeIterableMap<>();
        //就是去拿这个map：SafeIterableMap，这个this代表的是LiveData
        val mObserversObject: Any = mObserversField.get(this)

        // 得到map对象的class对象  private SafeIterableMap<Observer<? super GlowEtT>, ObserverWrapper> mObservers =
        val mObserversClass: Class<*> = mObserversObject.javaClass

        // 获取到mObservers对象的get方法(map都会维护一个entry的，都有get的)  protected Entry<GlowAcK, V> get(GlowAcK k) {
        val get: Method = mObserversClass.getDeclaredMethod("get", Any::class.java)
        get.isAccessible = true // 私有修饰也可以访问

        // 执行get方法
        val invokeEntry: Any? = get.invoke(mObserversObject, observer)
        //Any?,才是object， *：这个是星投影是kotlin中的泛型
        // 取到entry中的value  is "AAA" is String   is是判断类型 as是转换类型
        var observerWraper: Any? = null
        if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
            observerWraper = invokeEntry.value
        }
        //处理参数合法性
        if (observerWraper == null) {
            throw NullPointerException("observerWraper is null")
        }

        // 得到observerWraperr的类对象
        val supperClass: Class<*> = observerWraper.javaClass.superclass
        val mLastVersion: Field = supperClass.getDeclaredField("mLastVersion")
        mLastVersion.isAccessible = true

        // TODO 2.得到mVersion
        val mVersion: Field = liveDataClass.getDeclaredField("mVersion")
        mVersion.isAccessible = true

        // TODO 3.mLastVersion=mVersion；
        //  就是对齐了，LiveData所维护的数据在被观察者，观察者中的版本号相同，直接赋值，就好了
        val mVersionValue: Any = mVersion.get(this)
        mLastVersion.set(observerWraper, mVersionValue)
    }

}