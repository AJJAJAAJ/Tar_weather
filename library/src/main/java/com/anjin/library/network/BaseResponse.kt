package com.anjin.library.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 * @description .
 * @author Anjin
 * @date 2023-01-14
 *
 */
class BaseResponse {
    /**
     * 结果码
     */
    @SerializedName("res_code")
    @Expose
    var responseCode: Int? = null

    /**
     * 返回的错误信息
     */
    @SerializedName("res_error")
    @Expose
    var responseError: String? = null
}