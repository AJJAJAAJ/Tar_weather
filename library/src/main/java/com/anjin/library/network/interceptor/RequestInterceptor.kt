package com.anjin.library.network.interceptor

import android.annotation.SuppressLint
import com.anjin.library.network.INetworkRequiredInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
class RequestInterceptor(iNetworkRequiredInfo: INetworkRequiredInfo) : Interceptor {
    /**
     * 网络请求信息
     */
    private val iNetworkRequiredInfo: INetworkRequiredInfo

    init {
        this.iNetworkRequiredInfo = iNetworkRequiredInfo
    }

    /**
     * 拦截
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //构建器
        val builder = chain.request().newBuilder()
        //添加使用环境
        builder.addHeader("os", "android")
        //添加版本号
        builder.addHeader("appVersionCode", iNetworkRequiredInfo.getAppVersionCode())
        //添加版本名
        builder.addHeader("appVersionName", iNetworkRequiredInfo.getAppVersionName())
        //添加日期时间
        builder.addHeader("datetime", nowDateTime)
        //返回
        return chain.proceed(builder.build())
    }

    companion object {
        val nowDateTime: String
            get() {
                @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return sdf.format(Date())
            }
    }
}