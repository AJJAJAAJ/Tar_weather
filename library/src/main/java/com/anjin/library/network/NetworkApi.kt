package com.anjin.library.network

import com.anjin.library.network.errorhandler.ExceptionHandle
import com.anjin.library.network.errorhandler.HttpErrorHandler
import com.anjin.library.network.interceptor.RequestInterceptor
import com.anjin.library.network.interceptor.ResponseInterceptor
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
object NetworkApi {
    //获取APP运行状态及版本信息，用于日志打印
    private var iNetworkRequiredInfo: INetworkRequiredInfo? = null

    //OkHttp客户端
    private var okHttpClient: OkHttpClient? = null

    //retrofitHashMap
    private val retrofitHashMap = HashMap<String, Retrofit?>()

    //API访问地址
    private var mBaseUrl: String? = null

    /**
     * 初始化
     */
    fun init(networkRequiredInfo: INetworkRequiredInfo?) {
        iNetworkRequiredInfo = networkRequiredInfo
    }

    /**
     * 创建serviceClass的实例
     */
    fun <T> createService(serviceClass: Class<T>, apiType: ApiType): T {
        getBaseUrl(apiType)
        return getRetrofit(serviceClass)!!.create(serviceClass)
    }

    /**
     * 修改访问地址
     *
     * @param apiType api类型
     */
    private fun getBaseUrl(apiType: ApiType) {
        mBaseUrl = when (apiType) {
            ApiType.SEARCH ->
                "https://geoapi.qweather.com" //和风天气搜索城市
            ApiType.WEATHER ->
                "https://devapi.qweather.com"//和风天气API"
            ApiType.BING -> "https://cn.bing.com"
        }
    }

    /**
     * 配置OkHttp
     *
     * @return OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient? {
        //不为空则说明已经配置过了，直接返回即可。
        if (okHttpClient == null) {
            //OkHttp构建器
            val builder = OkHttpClient.Builder()
            //设置缓存大小
            val cacheSize = 100 * 1024 * 1024
            //设置OkHttp网络缓存
            builder.cache(
                Cache(
                    iNetworkRequiredInfo!!.getApplicationContext().cacheDir,
                    cacheSize.toLong()
                )
            )
            //设置网络请求超时时长，这里设置为10s
            builder.connectTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(20, TimeUnit.SECONDS).build()
            //添加请求拦截器，如果接口有请求头的话，可以放在这个拦截器里面
            builder.addInterceptor(RequestInterceptor(iNetworkRequiredInfo!!))
            //添加返回拦截器，可用于查看接口的请求耗时，对于网络优化有帮助
            builder.addInterceptor(ResponseInterceptor())
            //当程序在debug过程中则打印数据日志，方便调试用。
            if (iNetworkRequiredInfo != null && iNetworkRequiredInfo!!.isDebug()) {
                //iNetworkRequiredInfo不为空且处于debug状态下则初始化日志拦截器
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                //设置要打印日志的内容等级，BODY为主要内容，还有BASIC、HEADERS、NONE。
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                //将拦截器添加到OkHttp构建器中
                builder.addInterceptor(httpLoggingInterceptor)
            }
            //OkHttp配置完成
            okHttpClient = builder.build()
        }
        return okHttpClient
    }

    /**
     * 配置Retrofit
     *
     * @param serviceClass 服务类
     * @return Retrofit
     */
    private fun getRetrofit(serviceClass: Class<*>): Retrofit? {
        if (retrofitHashMap[mBaseUrl + serviceClass.name] != null) {
            //刚才上面定义的Map中键是String，值是Retrofit，当键不为空时，必然有值，有值则直接返回。
            return retrofitHashMap[mBaseUrl + serviceClass.name]
        }
        //初始化Retrofit  Retrofit是对OKHttp的封装，通常是对网络请求做处理，也可以处理返回数据。
        //Retrofit构建器
        val builder = Retrofit.Builder()
        //设置访问地址
        builder.baseUrl(mBaseUrl)
        //设置OkHttp客户端，传入上面写好的方法即可获得配置后的OkHttp客户端。
        builder.client(getOkHttpClient())
        //设置数据解析器 会自动把请求返回的结果（json字符串）通过Gson转化工厂自动转化成与其结构相符的实体Bean
        builder.addConverterFactory(GsonConverterFactory.create())
        //设置请求回调，使用RxJava 对网络返回进行处理
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        //retrofit配置完成
        val retrofit = builder.build()
        //放入Map中
        retrofitHashMap[mBaseUrl + serviceClass.name] = retrofit
        //最后返回即可
        return retrofit
    }

    /**
     * 配置RxJava 完成线程的切换，如果是Kotlin中完全可以直接使用协程
     *
     * @param observer 这个observer要注意不要使用lifecycle中的Observer
     * @param <T>      泛型
     * @return Observable
    </T> */
    fun <T> applySchedulers(observer: Observer<T>): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            val observable = upstream
                .subscribeOn(Schedulers.io()) //线程订阅
                .observeOn(AndroidSchedulers.mainThread()) //观察Android主线程
                .map(getAppErrorHandler()) //判断有没有500的错误，有则进入getAppErrorHandler
                .onErrorResumeNext(HttpErrorHandler()) //判断有没有400的错误
            //这里还少了对异常
            //订阅观察者
            observable.subscribe(observer)
            observable
        }
    }

    /**
     * 错误码处理
     */
    private fun <T> getAppErrorHandler(): Function<T, T> {
        return Function { response -> //当response返回出现500之类的错误时
            if (response is BaseResponse && response.responseCode!! >= 500) {
                //通过这个异常处理，得到用户可以知道的原因
                val exception = ExceptionHandle.ServerException()
                exception.code = response.responseCode!!
                exception.message =
                    if (response.responseError != null) response.responseError else ""
                throw exception
            }
            response
        }
    }
}