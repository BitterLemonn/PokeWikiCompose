package com.poke.pokewikicompose.data.repository

import com.poke.pokewikicompose.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

class LoginRepository {
    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: LoginRepository().also { instance = it }
            }
    }

    suspend fun getAuth(email: String, password: String): NetworkState<UserBean> {
        return UnifiedExceptionHandler.handleSuspend { ServerApi.create().getAuth(email, password) }
    }
}