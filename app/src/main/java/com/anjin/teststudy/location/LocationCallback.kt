package com.anjin.teststudy.location

import com.baidu.location.BDLocation

/**
 *
 * @description 定位回调
 * @author Anjin
 * @date 2023-01-14
 *
 */
interface LocationCallback {

    /**
     * 接收定位
     * @param bdLocation 定位数据
     */
    fun onReceiveLocation(bdLocation: BDLocation)
}