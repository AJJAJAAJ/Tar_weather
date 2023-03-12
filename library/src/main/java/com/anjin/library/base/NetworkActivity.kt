package com.anjin.library.base

import androidx.viewbinding.ViewBinding

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
abstract class NetworkActivity<VB : ViewBinding?> :
    BaseVBActivity<VB?>() {
    public override fun initData() {
        onCreate()
        onObserveData()
    }

    protected abstract fun onCreate()
    protected abstract fun onObserveData()
}
