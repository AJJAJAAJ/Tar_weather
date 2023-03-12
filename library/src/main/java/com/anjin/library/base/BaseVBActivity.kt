package com.anjin.library.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.Nullable
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseVBActivity<VB : ViewBinding?> :
    BaseActivity() {
    var binding: VB? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        onRegister()
        super.onCreate(savedInstanceState)
        val type = this.javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<*>
                //反射
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                binding = method.invoke(null, layoutInflater) as VB
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setContentView(binding!!.root)
        }
        initData()
    }

    protected abstract fun onRegister()
    protected abstract fun initData()
}