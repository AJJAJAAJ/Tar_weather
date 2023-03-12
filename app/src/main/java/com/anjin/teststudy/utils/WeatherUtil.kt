package com.anjin.teststudy.utils

import android.util.Log
import android.widget.ImageView
import com.anjin.teststudy.R

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
object WeatherUtil {
    /**
     * 根据传入的状态码修改填入的天气图标
     *
     * @param weatherStateIcon 显示的ImageView
     * @param code             天气状态码
     */
    fun changeIcon(weatherStateIcon: ImageView, code: Int) {
        when (code) {
            100 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_100)
            101 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_101)
            102 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_102)
            103 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_103)
            104 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_104)
            150 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_150)
            151, 152, 153 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_153)
            154 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_154)
            200, 202, 203, 204 ->                 //因为这几个状态的图标是一样的
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_200)
            201 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_201)
            205, 206, 207 ->                 //因为这几个状态的图标是一样的
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_205)
            208, 209, 210, 211, 212, 213 ->                 //因为这几个状态的图标是一样的
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_208)
            300 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_300)
            301 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_301)
            302 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_302)
            303 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_303)
            304 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_304)
            305 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_305)
            306 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_306)
            307 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_307)
            308 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_312)
            309 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_309)
            310 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_310)
            311 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_311)
            312 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_312)
            313 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_313)
            314 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_306)
            315 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_307)
            316 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_310)
            317 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_312)
            399 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_399)
            400 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_400)
            401 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_401)
            402 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_402)
            403 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_403)
            404 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_404)
            405 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_405)
            406 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_406)
            407 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_407)
            408 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_408)
            409 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_409)
            410 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_410)
            499 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_499)
            500 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_500)
            501 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_501)
            502 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_502)
            503 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_503)
            504 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_504)
            507 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_507)
            508 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_508)
            509, 510, 514, 515 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_509)
            511 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_511)
            512 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_512)
            513 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_513)
            900 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_900)
            901 -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_901)
            else -> weatherStateIcon.setBackgroundResource(R.mipmap.icon_999)
        }
    }

    /**
     * 紫外线等级描述
     *
     * @param uvIndex
     * @return
     */
    fun uvIndexInfo(uvIndex: String): String? {
        var result: String? = null
        Log.d("uvIndex-->", uvIndex)
        val level = uvIndex.toInt()
        if (level <= 2) {
            result = "较弱"
        } else if (level <= 5) {
            result = "弱"
        } else if (level <= 7) {
            result = "中等"
        } else if (level <= 10) {
            result = "强"
        } else if (level <= 15) {
            result = "很强"
        }
        return result
    }

    /**
     * 根据api的提示转为更为人性化的提醒
     *
     * @param apiInfo
     * @return
     */
    fun apiToTip(apiInfo: String): String? {
        var result: String? = null
        var str: String? = null
        str = if (apiInfo.contains("AQI ")) {
            apiInfo.replace("AQI ", " ")
        } else {
            apiInfo
        }
        when (str) {
            "优" -> result = "♪(^∇^*)  空气很好。"
            "良" -> result = "ヽ(✿ﾟ▽ﾟ)ノ  空气不错。"
            "轻度污染" -> result = "(⊙﹏⊙)  空气有些糟糕。"
            "中度污染" -> result = " ε=(´ο｀*)))  唉 空气污染较为严重，注意防护。"
            "重度污染" -> result = "o(≧口≦)o  空气污染很严重，记得戴口罩哦！"
            "严重污染" -> result = "ヽ(*。>Д<)o゜  完犊子了!空气污染非常严重，要减少出门，定期检查身体，能搬家就搬家吧！"
            else -> {}
        }
        return result
    }

    /**
     * 紫外线详细描述
     *
     * @param uvIndexInfo
     * @return
     */
    fun uvIndexToTip(uvIndexInfo: String?): String? {
        var result: String? = null
        when (uvIndexInfo) {
            "较弱" -> result = "紫外线较弱，不需要采取防护措施；若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"
            "弱" -> result = "紫外线弱，可以适当采取一些防护措施，涂擦SPF在12-15之间、PA+的防晒护肤品。"
            "中等" -> result = "紫外线中等，外出时戴好遮阳帽、太阳镜和太阳伞等；涂擦SPF高于15、PA+的防晒护肤品。"
            "强" -> result = "紫外线较强，避免在10点至14点暴露于日光下.外出时戴好遮阳帽、太阳镜和太阳伞等，涂擦SPF20左右、PA++的防晒护肤品。"
            "很强" -> result = "紫外线很强，尽可能不在室外活动，必须外出时，要采取各种有效的防护措施。"
            else -> {}
        }
        return result
    }

    /**
     * 早晚温差提示
     *
     * @param height 当天最高温
     * @param low    当天最低温
     */
    fun differenceTempTip(height: Int, low: Int): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append("    今天最高温$height℃，最低温$low℃。")
        if (height - low > 5) { //温差大
            if (height > 25) {
                stringBuffer.append("早晚温差较大，加强自我防护，防治感冒，对自己好一点(*￣︶￣)")
            } else if (height > 20) {
                stringBuffer.append("天气转阴温度低，上下班请注意添衣保暖(*^▽^*)")
            } else if (height > 15) {
                stringBuffer.append("关怀不是今天才开始，关心也不是今天就结束，希望你注意保暖ヾ(◍°∇°◍)ﾉﾞ")
            }
        } else { //温差小
            if (low > 20) {
                stringBuffer.append("多运动，多喝水，注意补充水分(*￣︶￣)")
            } else if (low > 10) {
                stringBuffer.append("早睡早起，别熬夜，无论晴天还是雨天，每天都是新的一天(*^▽^*)")
            } else if (low > 0) {
                stringBuffer.append("天气寒冷，注意防寒和保暖，也不要忘记锻炼喔ヾ(◍°∇°◍)ﾉﾞ")
            }
        }
        return stringBuffer.toString()
    }
}