package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler
import com.poke.pokewikicompose.utils.md5

class PasswordEditRepository {
    companion object {
        @Volatile
        private var instance: PasswordEditRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PasswordEditRepository().also { instance = it }
            }
    }

    suspend fun updateUserPassword(
        oldPassword: String,
        newPassword: String,
        userID: Int,
        token: String
    ): NetworkState<UserBean> {
        return UnifiedExceptionHandler.handleSuspend {
            ServerApi.create().updatePassword(
                oldPassword = md5(oldPassword),
                newPassword = md5(newPassword),
                userId = userID.toString(),
                token = token
            )
        }
    }
}