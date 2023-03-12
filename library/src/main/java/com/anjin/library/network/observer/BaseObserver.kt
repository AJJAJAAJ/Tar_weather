package com.anjin.library.network.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
abstract class BaseObserver<T> : Observer<T?> {
    //开始
    override fun onSubscribe(d: Disposable) {}

    //继续
    override fun onNext(t: T) {
        onSuccess(t)
    }

    //异常
    override fun onError(e: Throwable) {
        onFailure(e)
    }

    //完成
    override fun onComplete() {}

    //成功
    abstract fun onSuccess(t: T?)

    //失败
    abstract fun onFailure(e: Throwable?)
}