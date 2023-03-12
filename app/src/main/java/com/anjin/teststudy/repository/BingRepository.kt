package com.anjin.teststudy.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anjin.library.network.ApiType
import com.anjin.library.network.NetworkApi.applySchedulers
import com.anjin.library.network.NetworkApi.createService
import com.anjin.library.network.observer.BaseObserver
import com.anjin.teststudy.api.ApiService
import com.anjin.teststudy.datebase.bean.BingResponse

class BingRepository {

    companion object {
        private val TAG = BingRepository::class.simpleName
        private var instance: BingRepository? = null
        fun getInstance(): BingRepository {
            if (instance == null) {
                synchronized(BingRepository::class.java) {
                    if (instance == null) {
                        instance = BingRepository()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 必应壁纸
     *
     * @param responseLiveData 成功数据
     * @param failed           错误信息
     */
    @SuppressLint("CheckResult")
    fun bing(
        responseLiveData: MutableLiveData<BingResponse>,
        failed: MutableLiveData<String>
    ) {
        val type = "必应壁纸-->"
        createService(ApiService::class.java,
            ApiType.BING).
        bing()?.compose(applySchedulers(object : BaseObserver<BingResponse?>() {
                override fun onSuccess(bingResponse: BingResponse?) {
                    if (bingResponse == null) {
                        failed.postValue("必应壁纸数据为null。")
                        return
                    }
                    responseLiveData.postValue(bingResponse)
                }

                override fun onFailure(e: Throwable?) {
                    Log.e(TAG, "onFailure: " + e!!.message)
                    failed.postValue(type + e.message)
                }
            }))
    }
}