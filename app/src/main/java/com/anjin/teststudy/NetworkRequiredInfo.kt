package com.anjin.teststudy

import android.app.Application
import com.anjin.library.network.INetworkRequiredInfo
import com.anjin.teststudy.BuildConfig.VERSION_CODE
import com.anjin.teststudy.BuildConfig.VERSION_NAME

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
open class NetworkRequiredInfo(application: Application) : INetworkRequiredInfo {
    private val application: Application

    init {
        this.application = application
    }

    /**
     * 版本名
     */
    override fun getAppVersionName(): String {
        return VERSION_NAME
    }

    /**
     * 版本号
     */
    override fun getAppVersionCode(): String {
        return java.lang.String.valueOf(VERSION_CODE)
    }

    /**
     * 是否为debug
     */
    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * 应用全局上下文
     */
    override fun getApplicationContext(): Application {
        return application
    }
}