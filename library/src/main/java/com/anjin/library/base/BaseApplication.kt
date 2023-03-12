package com.anjin.library.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //声明Activity管理
        activityManager = ActivityManager()
        context = applicationContext
        application = this
    }

    companion object {
        var activityManager: ActivityManager? = null
            private set

        @SuppressLint("StaticFieldLeak")
        var application: BaseApplication? = null
            private set

        //内容提供器
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }
}