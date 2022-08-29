package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

class ProfileEditRepository {
    companion object {
        @Volatile
        private var instance: ProfileEditRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProfileEditRepository().also { instance = it }
            }
    }

    suspend fun changeUserName(
        userName: String,
        userId: String,
        token: String
    ): NetworkState<UserBean> {
        return UnifiedExceptionHandler.nullableHandleSuspend {
            ServerApi.create().updateUsername(username = userName, userId = userId, token = token)
        }
    }
}