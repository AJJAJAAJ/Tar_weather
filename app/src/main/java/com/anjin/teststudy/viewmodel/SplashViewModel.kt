package com.anjin.teststudy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.anjin.library.base.BaseViewModel
import com.anjin.teststudy.datebase.bean.BingResponse
import com.anjin.teststudy.datebase.bean.Province
import com.anjin.teststudy.repository.BingRepository
import com.anjin.teststudy.repository.CityRepository

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
class SplashViewModel : BaseViewModel() {
    var listMutableLiveData: MutableLiveData<List<Province>> = MutableLiveData<List<Province>>()
    var bingMutableLiveData = MutableLiveData<BingResponse>()

    /**
     * 获取每日壁纸
     */
    fun bing() {
        BingRepository.getInstance().bing(
            bingMutableLiveData,
            failed
        )
    }

    /**
     * 添加城市数据
     */
    fun addCityData(provinceList: List<Province?>?) {
        CityRepository.getInstance().addCityData(provinceList)
    }

    /**
     * 获取所有城市数据
     */
    fun getAllCityData() {
        CityRepository.getInstance().getCityData(listMutableLiveData)
    }
}