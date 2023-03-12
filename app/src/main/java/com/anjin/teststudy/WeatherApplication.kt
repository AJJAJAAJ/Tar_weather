package com.anjin.teststudy

import android.content.Context
import com.anjin.library.base.BaseApplication
import com.anjin.library.network.NetworkApi
import com.anjin.teststudy.datebase.AppDatabase
import com.anjin.teststudy.utils.MVUtils
import com.baidu.location.LocationClient
import com.tencent.mmkv.MMKV

/**
 *
 * @description 上下文
 * @author Anjin
 * @date 2023-01-14
 *
 */
class WeatherApplication : BaseApplication() {
    private lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        //使用定位，同意隐私政策
        LocationClient.setAgreePrivacy(true)
        NetworkApi.init(NetworkRequiredInfo(this))
        //MMKV初始化
        MMKV.initialize(this);
        //工具类初始化
        MVUtils.getInstance()
        //初始化Room数据库
        database = AppDatabase.getInstance(this)
        context = this
    }

    companion object{
        private var database: AppDatabase? = null
        @JvmStatic
        fun getDb(): AppDatabase? {
            return database
        }

        fun getContext(): Context {
            return context!!
        }
    }
}