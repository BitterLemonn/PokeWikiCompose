package com.poke.pokewikicompose.dataBase.data.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poke.pokewikicompose.utils.DOWNLOAD_URL
import com.poke.pokewikicompose.utils.JsonConverter
import com.poke.pokewikicompose.utils.ResponseData
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface DownloadApi {
    @Streaming
    @GET("small/{pokemonID}.png")
    suspend fun getSmallPic(@Path("pokemonID") pokemonID: Int): Response<ResponseBody>

    @Streaming
    @GET("big/{pokemonID}.png")
    suspend fun getBigPic(@Path("pokemonID") pokemonID: Int): Response<ResponseBody>

    companion object {
        /**
         * 获取接口实例用于调用下载方法
         * @return DownloadApi
         */
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): DownloadApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(DOWNLOAD_URL)
                .addConverterFactory(
                    JsonConverter.Json
                        .asConverterFactory(contentType = "application/json".toMediaType())
                )
                .client(client)
                .build()
                .create(DownloadApi::class.java)
        }
    }
}