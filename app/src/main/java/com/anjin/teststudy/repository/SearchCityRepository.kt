package com.anjin.teststudy.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anjin.library.network.ApiType
import com.anjin.library.network.NetworkApi.applySchedulers
import com.anjin.library.network.NetworkApi.createService
import com.anjin.library.network.observer.BaseObserver
import com.anjin.teststudy.api.ApiService
import com.anjin.teststudy.Constant
import com.anjin.teststudy.datebase.bean.SearchCityResponse

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */

class SearchCityRepository {
    @SuppressLint("CheckResult")
    fun searchCity(
        responseLiveData: MutableLiveData<SearchCityResponse?>,
        failed: MutableLiveData<String>,
        cityName: String?,
        isExact: Boolean
    ) {
        createService(
            ApiService::class.java,
            ApiType.SEARCH
        ).searchCity(
            cityName,
            if (isExact) Constant.EXACT else Constant.FUZZY
        )?.compose(applySchedulers(object : BaseObserver<SearchCityResponse>() {
            override fun onSuccess(searchCityResponse: SearchCityResponse?) {
                if (searchCityResponse == null) {
                    Log.e(TAG, "searchCityResponse is null")
                    return
                }
                //请求接口成功返回数据，失败返回状态码
                if (Constant.SUCCESS == searchCityResponse.code) {
                    responseLiveData.postValue(searchCityResponse)
                } else {
                    failed.postValue(searchCityResponse.code)
                }
            }

            override fun onFailure(e: Throwable?) {
                Log.e(TAG, "onFailure: " + e!!.message)
                failed.postValue(e.message)
            }
        }))
    }

    companion object {
        private val TAG = SearchCityRepository::class.java.simpleName
        private var instance: SearchCityRepository? = null
        fun getInstance(): SearchCityRepository {
            if (instance == null) {
                synchronized(SearchCityRepository::class.java) {
                    if (instance == null) {
                        instance = SearchCityRepository()
                    }
                }
            }
            return instance!!
        }
    }
}