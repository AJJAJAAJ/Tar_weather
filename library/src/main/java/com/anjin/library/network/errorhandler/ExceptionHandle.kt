package com.anjin.library.network.errorhandler

import android.net.ParseException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-14
 *
 */
object ExceptionHandle {
    //未授权
    private const val UNAUTHORIZED = 401

    //禁止的
    private const val FORBIDDEN = 403

    //未找到
    private const val NOT_FOUND = 404

    //请求超时
    private const val REQUEST_TIMEOUT = 408

    //内部服务器错误
    private const val INTERNAL_SERVER_ERROR = 500

    //错误网关
    private const val BAD_GATEWAY = 502

    //暂停服务
    private const val SERVICE_UNAVAILABLE = 503

    //网关超时
    private const val GATEWAY_TIMEOUT = 504

    /**
     * 处理异常
     * @param throwable
     * @return
     */
    fun handleException(throwable: Throwable): ResponseThrowable {
        //返回时抛出异常
        val responseThrowable: ResponseThrowable
        return if (throwable is HttpException) {
            val httpException: HttpException = throwable as HttpException
            responseThrowable = ResponseThrowable(throwable, ERROR.HTTP_ERROR)
            when (httpException.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> responseThrowable.message =
                    "网络错误"
                else -> responseThrowable.message = "网络错误"
            }
            responseThrowable
        } else if (throwable is ServerException) {
            //服务器异常
            responseThrowable = ResponseThrowable(throwable, throwable.code)
            responseThrowable.message = throwable.message
            responseThrowable
        } else if (throwable is JsonParseException
            || throwable is JSONException
            || throwable is ParseException
        ) {
            responseThrowable = ResponseThrowable(throwable, ERROR.PARSE_ERROR)
            responseThrowable.message = "解析错误"
            responseThrowable
        } else if (throwable is ConnectException) {
            responseThrowable = ResponseThrowable(throwable, ERROR.NETWORK_ERROR)
            responseThrowable.message = "连接失败"
            responseThrowable
        } else if (throwable is SSLHandshakeException) {
            responseThrowable = ResponseThrowable(throwable, ERROR.SSL_ERROR)
            responseThrowable.message = "证书验证失败"
            responseThrowable
        } else if (throwable is ConnectTimeoutException) {
            responseThrowable = ResponseThrowable(throwable, ERROR.TIMEOUT_ERROR)
            responseThrowable.message = "连接超时"
            responseThrowable
        } else if (throwable is SocketTimeoutException) {
            responseThrowable = ResponseThrowable(throwable, ERROR.TIMEOUT_ERROR)
            responseThrowable.message = "连接超时"
            responseThrowable
        } else {
            responseThrowable = ResponseThrowable(throwable, ERROR.UNKNOWN)
            responseThrowable.message = "未知错误"
            responseThrowable
        }
    }

    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }

    class ResponseThrowable(throwable: Throwable?, var code: Int) :
        Exception(throwable) {
        override var message: String? = null
    }

    class ServerException : RuntimeException() {
        var code = 0
        override var message: String? = null
    }
}