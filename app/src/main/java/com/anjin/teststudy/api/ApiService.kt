package com.anjin.teststudy.api

import com.anjin.teststudy.Constant.API_KEY
import com.anjin.teststudy.datebase.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
interface ApiService {
    /**
     * 搜索城市  模糊搜索，国内范围 返回10条数据
     *
     * @param location 城市名
     * @param mode     exact 精准搜索  fuzzy 模糊搜索
     * @return NewSearchCityResponse 搜索城市数据返回
     */
    @GET("/v2/city/lookup?key=$API_KEY&range=cn")
    fun searchCity(
        @Query("location") location: String?,
        @Query("mode") mode: String?
    ): Observable<SearchCityResponse?>?

    /**
     * @param location 城市名
     * @return NowResponse 实时天气数据
     */
    @GET("/v7/weather/now?key=$API_KEY")
    fun nowWeather(
        @Query("location") location: String?
    ): Observable<NowResponse?>?

    /**
     * @param location 城市名
     * @return DailyResponse 实时天气数据
     */
    @GET("/v7/weather/7d?key=$API_KEY")
    fun dailyWeather(
        @Query("location") location: String?
    ): Observable<DailyResponse?>?

    /**
     * @param type 生活类型
     * @param location 城市名
     * @return LifeResponse 生活建议
     */
    @GET("/v7/indices/1d?key=$API_KEY")
    fun lifestyle(
        @Query("type") type: String?,
        @Query("location") location: String?
    ): Observable<LifestyleResponse?>?

    /**
     * @return BingResponse 每日壁纸
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    fun bing(): Observable<BingResponse?>?

    /**
     * 每小时的天气
     */
    @GET("/v7/weather/24h?key=$API_KEY")
    fun hourlyWeather(
        @Query("location") location: String?
    ): Observable<HourlyResponse?>?

    /**
     * 空气质量
     */
    @GET("/v7/air/now?key=$API_KEY")
    fun airWeather(
        @Query("location") location: String?
    ): Observable<AirResponse?>?


}