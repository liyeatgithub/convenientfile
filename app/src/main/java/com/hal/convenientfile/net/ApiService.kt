package com.hal.convenientfile.net

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Description:网络请求接口
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/1
 */
interface ApiService {

    /**
     * 查询当前设备 cloak黑白名单
     */
    @GET("saginaw/weave")
    fun getDeviceType(@QueryMap queryMap: @JvmSuppressWildcards Map<String, Any?> = mapOf()): Observable<String>

    /**
     * 事件上报接口,install/广告事件/埋点等，通过参数区分
     */
    @POST("corset/theology")//正式
//    @POST("whom/armament/jonas")//测试
    fun uploadTbaEvent(
        @Body body: @JvmSuppressWildcards Map<String, Any?> = mapOf()
    ): Observable<Any>

}