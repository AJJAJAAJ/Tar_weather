package com.anjin.library.network

import android.app.Application

/**
 *
 * @description 网络接口
 * @author Anjin
 * @date 2023-01-14
 *
 */
interface INetworkRequiredInfo {
    /**
     * 获取App版本名
     */
    fun getAppVersionName(): String

    /**
     * 获取App版本号
     */
    fun getAppVersionCode(): String

    /**
     * 判断是否为Debug模式
     */
    fun isDebug(): Boolean

    /**
     * 获取全局上下文参数
     */
    fun getApplicationContext(): Application
}