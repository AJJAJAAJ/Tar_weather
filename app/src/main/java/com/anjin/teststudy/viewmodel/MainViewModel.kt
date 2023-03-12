package com.anjin.teststudy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.anjin.library.base.BaseViewModel
import com.anjin.teststudy.datebase.bean.*
import com.anjin.teststudy.repository.CityRepository
import com.anjin.teststudy.repository.SearchCityRepository
import com.anjin.teststudy.repository.WeatherRepository

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
class MainViewModel : BaseViewModel() {
    var searchCityResponseMutableLiveData = MutableLiveData<SearchCityResponse?>()
    var nowResponseMutableLiveData = MutableLiveData<NowResponse?>()
    var dailyResponseMutableLiveData = MutableLiveData<DailyResponse?>()
    var lifeStyleResponseMutableLiveData = MutableLiveData<LifestyleResponse?>()
    val cityMutableLiveData = MutableLiveData<List<Province>>()
    val hourlyMutableLiveData = MutableLiveData<HourlyResponse?>()
    val airMutableLiveData = MutableLiveData<AirResponse?>()

    /**
     * 搜索成功
     * @param cityName 城市名称
     * @param isExact 是否精准搜索
     */
    fun searchCity(cityName: String?, isExact: Boolean) {
        SearchCityRepository.getInstance().searchCity(
            searchCityResponseMutableLiveData,
            failed,
            cityName,
            isExact
        )
    }

    /**
     * 获取实时天气成功
     * @param cityId 城市ID
     */
    fun nowWeather(cityId: String?) {
        WeatherRepository.getInstance().nowWeather(
            nowResponseMutableLiveData,
            failed,
            cityId
        )
    }

    /**
     * 获取天气预报成功
     * @param cityId 城市ID
     */
    fun dailyWeather(cityId: String?) {
        WeatherRepository.getInstance().dailyWeather(
            dailyResponseMutableLiveData,
            failed,
            cityId
        )
    }

    /**
     * 获取生活建议成功
     * @param cityId 城市ID
     */
    fun lifeStyle(cityId: String?) {
        WeatherRepository.getInstance().lifeStyle(
            lifeStyleResponseMutableLiveData,
            failed,
            cityId
        )
    }

    /**
     * 获取逐小时天气预报成功
     * @param cityId 城市ID
     */
    fun hourlyWeather(cityId: String?) {
        WeatherRepository.getInstance().hourlyWeather(
            hourlyMutableLiveData,
            failed,
            cityId
        )
    }

    /**
     * 获取空气质量
     */
    fun airWeather(cityId: String?) {
        WeatherRepository.getInstance().airWeather(
            airMutableLiveData,
            failed,
            cityId
        )
    }

    fun getAllCity() {
        CityRepository.getInstance().getCityData(cityMutableLiveData)
    }

    fun addMyCityData(cityName: String?) {
        val myCity = MyCity(cityName)
        CityRepository.getInstance().addMyCityData(myCity)
    }
}
