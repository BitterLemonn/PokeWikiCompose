package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler
import okhttp3.ResponseBody

enum class DownloadType {
    BIG, SMALL
}

class DownloadRepository {
    companion object {
        @Volatile
        private var instance: DownloadRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DownloadRepository().also { instance = it }
            }
    }

    suspend fun getImageWithTypeAndID(
        type: DownloadType,
        id: Int
    ): NetworkState<ResponseBody> {
        val result = try {
            when (type) {
                DownloadType.BIG -> DownloadApi.create().getBigPic(id)
                DownloadType.SMALL -> DownloadApi.create().getSmallPic(id)
            }
        } catch (e: Exception) {
            return NetworkState.Error(e.message?:"未知异常,请联系管理员")
        }

        result.let {
            if (it.body() != null) {
                return NetworkState.Success(it.body()!!)
            } else {
                return NetworkState.Error("服务器无回应")
            }
        }
    }
}