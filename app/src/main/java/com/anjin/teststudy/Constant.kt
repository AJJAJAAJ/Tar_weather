package com.anjin.teststudy

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
object Constant {
    /**
     * 和风天气的KEY，请使用自己的
     */
    const val API_KEY = "9ef2f7389c63452c93f20ab1d136815c"

    /**
     * 和风天气接口请求成功状态码
     */
    const val SUCCESS = "200"

    /**
     * 搜索类型：精准搜索
     */
    const val EXACT = "exact"

    /**
     * 搜索类型：模糊搜索
     */
    const val FUZZY = "fuzzy"

    /**
     * 程序第一次运行
     */
    const val FIRST_RUN = "firstRun"

    /**
     * 今天第一次启动时间
     */
    const val FIRST_STARTUP_TIME_TODAY = "firstStartupTimeToday"

    /**
     * 是否使用必应壁纸
     */
    const val USED_BING = "usedBing"

    /**
     * 必应图片地址
     */
    const val BING_URL = "bingUrl"

    /**
     * 当前定位城市
     */
    const val LOCATION_CITY = "locationCity"

    /**
     * 页面返回城市结果
     */
    const val CITY_RESULT = "cityResult"

    /**
     * 推荐城市数组
     */
    val CITY_ARRAY = arrayOf(
        "北京",
        "上海",
        "广州",
        "深圳",
        "天津",
        "武汉",
        "长沙",
        "重庆",
        "沈阳",
        "杭州",
        "南京",
        "沈阳",
        "哈尔滨",
        "长春",
        "呼和浩特",
        "石家庄",
        "银川",
        "乌鲁木齐",
        "呼和浩特",
        "拉萨",
        "西宁",
        "西安",
        "兰州",
        "太原",
        "昆明",
        "南宁",
        "成都",
        "济南",
        "南昌",
        "厦门",
        "扬州",
        "岳阳",
        "赣州",
        "苏州",
        "福州",
        "贵阳",
        "桂林",
        "海口",
        "三亚",
        "香港",
        "澳门",
        "台北"
    )
}