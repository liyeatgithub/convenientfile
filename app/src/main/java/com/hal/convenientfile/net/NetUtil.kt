package com.hal.convenientfile.net

import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.util.Device
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/1
 */
class NetUtil(private val isGzip: Boolean = true) {
    private val DEFAULT_TIMEOUT: Long = 10

    // Install the all-trusting trust manager TLS
    private val unsafeOkHttpClient: OkHttpClient
        get() = run {
            // Install the all-trusting trust manager TLS
            /*   val sslContext = SSLContext.getInstance("TLS")
               sslContext.init(null, trustAllCerts, SecureRandom())
               // Create an ssl socket factory with our all-trusting manager
               val sslSocketFactory = sslContext.socketFactory*/
            OkHttpClient.Builder()
//            okBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .apply {
                            addHeader("content-type", "application/json")
                            addHeader(
                                "original",
                                Device.androidId
                            )
                            if (isGzip) addHeader("Content-Encoding", "gzip")
                        }
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(
                    if (BaseApp.app.isDebug) HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    ) else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                )
//            okBuilder.hostnameVerifier { _, _ -> true }
                .build()
        }

    fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            //parse primitives, boxed, and String
            .addConverterFactory(ScalarsConverterFactory.create()) //添加ScalarsConverterFactory支持
            //parse json
            .addConverterFactory(GsonConverterFactory.create())//可以接收自定义的Gson，当然也可以不传
            //create方法默认为异步
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(unsafeOkHttpClient)
            .build()
    }
}