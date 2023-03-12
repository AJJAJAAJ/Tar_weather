package com.anjin.teststudy.ui.adapter

import android.view.View
import com.anjin.teststudy.utils.AdministrativeType

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
open interface AdministrativeClickCallback {
    /**
     * 行政区 点击事件
     *
     * @param view     点击视图
     * @param position 点击位置
     * @param type     行政区类型
     */
    fun onAdministrativeItemClick(view: View?, position: Int, type: AdministrativeType?)
}