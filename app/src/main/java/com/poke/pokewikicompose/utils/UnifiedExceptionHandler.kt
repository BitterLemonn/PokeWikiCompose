package com.poke.pokewikicompose.utils

import com.orhanobut.logger.Logger
import java.net.SocketTimeoutException

object UnifiedExceptionHandler {
    private const val TAG = "UnifiedException"

    suspend fun <T> handleSuspend(function: suspend () -> ResponseData<T>): NetworkState<T> {
        return try {
            val result = function.invoke()
            when (result.status) {
                200 -> NetworkState.Success(result.data)
                else -> NetworkState.Error(result.msg ?: "未知错误，请联系管理员")
            }
        } catch (e: SocketTimeoutException) {
            Logger.e(TAG, "链接超时")
            return NetworkState.Error("连接超时，网络好像被精灵球捕捉了QAQ")
        } catch (e: Exception) {
            e.localizedMessage?.let { Logger.e(it) }
            return NetworkState.Error("未知错误，请联系管理员")
        }
    }

    suspend fun <T> nullableHandleSuspend(function: suspend () -> NullableResponseData<T>): NetworkState<T> {
        return try {
            val result = function.invoke()
            Logger.d("result: $result")
            when (result.status) {
                200 -> {
                    result.data?.let {
                        NetworkState.Success(result.data)
                    } ?: NetworkState.NoNeedResponse(result.msg ?: "未知错误，请联系管理员")
                }
                else -> NetworkState.Error(result.msg ?: "未知错误，请联系管理员")
            }
        } catch (e: SocketTimeoutException) {
            Logger.e(TAG, "链接超时")
            return NetworkState.Error("连接超时，网络好像被精灵球捕捉了QAQ")
        } catch (e: Exception) {
            e.localizedMessage?.let { Logger.e(it) }
            return NetworkState.Error("未知错误，请联系管理员")
        }
    }
}