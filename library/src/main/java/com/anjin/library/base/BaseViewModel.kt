package com.anjin.library.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
open class BaseViewModel : ViewModel() {
    var failed = MutableLiveData<String>()
}
