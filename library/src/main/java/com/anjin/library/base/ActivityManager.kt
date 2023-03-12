package com.anjin.library.base

import android.app.Activity

/**
 *
 * @description 管理activity
 * @author Anjin
 * @date 2023-01-14
 *
 */
class ActivityManager {
    /**
     * 保存创建的所有Activity
     */
    private val allActivities = mutableListOf<Activity>()

    /**
     * 添加activity到管理器
     * @param activity activity
     */
    fun addActivity(activity: Activity?) {
        if (activity != null) {
            allActivities.add(activity);
        }
    }

    /**
     * 移除activity
     * @param activity activity
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            allActivities.remove(activity)
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAll() {
        for (activity in allActivities) {
            activity.finish()
        }
    }

    fun getTaskTop(): Activity {
        return allActivities[allActivities.size - 1]
    }
}