package com.anjin.library.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
class ResponseInterceptor : Interceptor {
    /**
     * 拦截
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        Log.i(TAG, "requestSpendTime=" + (System.currentTimeMillis() - requestTime) + "ms")
        return response
    }

    companion object {
        private val TAG = ResponseInterceptor::class.java.simpleName
    }
}