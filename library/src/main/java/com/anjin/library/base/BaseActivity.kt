package com.anjin.library.base

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.anjin.library.R
import com.google.android.material.appbar.MaterialToolbar

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
open class BaseActivity : AppCompatActivity() {
    private var mContext: AppCompatActivity? = null
    private var mDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        BaseApplication.activityManager?.addActivity(this)
    }

    protected fun showMsg(msg: CharSequence?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun showLongMsg(msg: CharSequence?) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * 跳转页面
     *
     * @param clazz 目标页面
     */
    protected fun jumpActivity(clazz: Class<*>?) {
        startActivity(Intent(mContext, clazz))
    }

    /**
     * 跳转页面并关闭当前页面
     *
     * @param clazz 目标页面
     */
    protected fun jumpActivityFinish(clazz: Class<*>?) {
        startActivity(Intent(mContext, clazz))
        finish()
    }

    protected fun back(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    protected fun backAndFinish(toolbar: MaterialToolbar) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * 检查是有拥有某权限
     *
     * @param permission 权限名称
     * @return true 有  false 没有
     */
    @RequiresApi(Build.VERSION_CODES.M)
    protected fun hasPermission(permission: String?): Boolean {
        return checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 退出应用程序
     */
    protected fun exitTheProgram() {
        BaseApplication.activityManager?.finishAll()
    }

    protected fun setFullScreenImmersion() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        val option = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = option;
        window.statusBarColor = Color.TRANSPARENT;
        window.navigationBarColor = Color.TRANSPARENT;
    }

    protected open fun setStatusBar(dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller!!.setSystemBarsAppearance(
                if (dark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            controller.setSystemBarsAppearance(
                if (dark) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            window.decorView.systemUiVisibility =
                if (dark) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    fun showLoadingDialog() {
        if (mDialog == null) {
            mDialog = Dialog(mContext!!, R.style.loading_dialog)
        }
        mDialog!!.setContentView(R.layout.dialog_loading)
        mDialog!!.setCancelable(true)
        mDialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        mDialog!!.show()
    }

    fun dismissLoadingDialog() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
        mDialog = null
    }
}