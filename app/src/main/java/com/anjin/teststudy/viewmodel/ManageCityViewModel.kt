package com.anjin.teststudy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.anjin.library.base.BaseViewModel
import com.anjin.teststudy.datebase.bean.MyCity
import com.anjin.teststudy.repository.CityRepository

class ManageCityViewModel : BaseViewModel() {
    val listMutableLiveData = MutableLiveData<List<MyCity>>()

    /**
     * 获取所有城市数据
     */
    fun getAllCityData() {
        CityRepository.getInstance().getMyCityData(listMutableLiveData)
    }

    /**
     * 添加我的城市数据，在定位之后添加数据
     */
    fun addMyCityData(cityName: String) {
        CityRepository.getInstance().addMyCityData(MyCity(cityName))
    }

    /**
     * 删除我的城市数据
     */
    fun deleteMyCityData(myCity: MyCity){
        CityRepository.getInstance().deleteMyCityData(myCity)
    }

    fun deleteMyCityData(cityName: String) {
        CityRepository.getInstance().deleteMyCityData(cityName)
    }
}