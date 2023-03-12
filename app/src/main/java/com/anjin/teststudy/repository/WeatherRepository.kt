package com.anjin.teststudy.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anjin.library.network.ApiType
import com.anjin.library.network.NetworkApi.applySchedulers
import com.anjin.library.network.NetworkApi.createService
import com.anjin.library.network.observer.BaseObserver
import com.anjin.teststudy.Constant
import com.anjin.teststudy.api.ApiService
import com.anjin.teststudy.datebase.bean.*

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
@SuppressLint("CheckResult")
class WeatherRepository {
    companion object {
        private val TAG = WeatherRepository::class.simpleName
        private const val LIFE_STYLE = "1,2,3,4,5,6,7,8,9"
        private var instance: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (instance == null) {
                synchronized(WeatherRepository::class.java) {
                    if (instance == null) {
                        instance = WeatherRepository()
                    }
                }
            }
            return instance!!
        }
    }

    fun nowWeather(
        responseLiveData: MutableLiveData<NowResponse?>,
        failed: MutableLiveData<String>,
        cityId: String?
    ) {
        val type = "实时天气-->"
        createService(ApiService::class.java, ApiType.WEATHER).nowWeather(cityId)
            ?.compose(applySchedulers(object : BaseObserver<NowResponse>() {
                override fun onSuccess(t: NowResponse?) {
                    if (t == null) {
                        Log.e(TAG, "nowResponse is null ")
                        return
                    }
                    if (Constant.SUCCESS == t.code) {
                        responseLiveData.postValue(t)
                    } else {
                        failed.postValue(type + t.code)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure : ${e?.message}")
                    failed.postValue(type + e?.message)
                }

            }))
    }


    fun dailyWeather(
        responseLiveData: MutableLiveData<DailyResponse?>,
        failed: MutableLiveData<String>,
        cityId: String?
    ) {
        val type = "天气预报-->"
        createService(ApiService::class.java, ApiType.WEATHER).dailyWeather(cityId)
            ?.compose(applySchedulers(object : BaseObserver<DailyResponse>() {
                override fun onSuccess(t: DailyResponse?) {
                    if (t == null) {
                        Log.e(TAG, "nowResponse is null ")
                        return
                    }
                    if (Constant.SUCCESS == t.code) {
                        responseLiveData.postValue(t)
                    } else {
                        failed.postValue(type + t.code)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure: ${e?.message}")
                    failed.postValue(type + e?.message)
                }

            }))
    }

    fun lifeStyle(
        responseLiveData: MutableLiveData<LifestyleResponse?>,
        failed: MutableLiveData<String>,
        cityId: String?
    ) {
        val type = "生活建议-->"
        createService(ApiService::class.java, ApiType.WEATHER).lifestyle(LIFE_STYLE, cityId)
            ?.compose(applySchedulers(object : BaseObserver<LifestyleResponse>() {
                override fun onSuccess(t: LifestyleResponse?) {
                    if (t == null) {
                        failed.postValue("生活指数数据为null，请检查城市ID是否正确。")
                        return
                    }
                    //请求接口成功返回数据，失败返回状态码
                    if (Constant.SUCCESS == t.code) {
                        responseLiveData.postValue(t)
                    } else {
                        failed.postValue(type + t.code)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure: " + e?.message);
                    failed.postValue(type + e?.message);
                }

            }))
    }

    fun hourlyWeather(
        responseLiveData: MutableLiveData<HourlyResponse?>,
        failed: MutableLiveData<String>,
        cityId: String?
    ) {
        val type = "逐小时天气预报-->"
        createService(ApiService::class.java, ApiType.WEATHER).hourlyWeather(cityId)
            ?.compose(applySchedulers(object : BaseObserver<HourlyResponse>() {
                override fun onSuccess(t: HourlyResponse?) {
                    if (t == null) {
                        failed.postValue("逐小时天气预报为null，请检查城市ID是否正确。")
                        return
                    }
                    //请求接口成功返回数据，失败返回状态码
                    if (Constant.SUCCESS == t.code) {
                        responseLiveData.postValue(t)
                    } else {
                        failed.postValue(type + t.code)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure: " + e?.message);
                    failed.postValue(type + e?.message);
                }
            }))
    }

    /**
     * 空气质量天气预报
     *
     * @param responseLiveData 成功数据
     * @param failed           错误信息
     * @param cityId           城市ID
     */
    fun airWeather(
        responseLiveData: MutableLiveData<AirResponse?>,
        failed: MutableLiveData<String>,
        cityId: String?
    ) {
        val type = "空气质量天气预报-->"
        createService(ApiService::class.java, ApiType.WEATHER).airWeather(cityId)
            ?.compose(applySchedulers(object : BaseObserver<AirResponse>() {
                override fun onSuccess(t: AirResponse?) {
                    if (t == null) {
                        failed.postValue("空气质量为null，请检查城市ID是否正确。")
                        return
                    }
                    //请求接口成功返回数据，失败返回状态码
                    if (Constant.SUCCESS == t.code) {
                        responseLiveData.postValue(t)
                    } else {
                        failed.postValue(type + t.code)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure: " + e?.message);
                    failed.postValue(type + e?.message);
                }
            }))
    }
}