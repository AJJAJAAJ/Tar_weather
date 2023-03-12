package com.anjin.library.network.errorhandler

import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
class HttpErrorHandler<T> :
    Function<Throwable?, Observable<T?>?> {
    /**
     * 处理以下两类网络错误：
     * 1、http请求相关的错误，例如：404，403，socket timeout等等；
     * 2、应用数据的错误会抛RuntimeException，最后也会走到这个函数来统一处理；
     */
    override fun apply(throwable: Throwable): Observable<T?>? {
        //通过这个异常处理，得到用户可以知道的原因
        return Observable.error(
            ExceptionHandle.handleException(
                throwable
            )
        )
    }
}