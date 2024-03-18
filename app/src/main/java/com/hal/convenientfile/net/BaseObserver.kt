package com.hal.convenientfile.net

import com.hal.convenientfile.util.log
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/2
 */
open class BaseObserver<T : Any>(
    private val disposable: CompositeDisposable?,
) :
    Observer<T> {
    override fun onSubscribe(d: Disposable) {
        disposable?.add(d)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        log("request error==>", e.message)
    }

    override fun onComplete() {
    }

    override fun onNext(t: T) {

    }

}