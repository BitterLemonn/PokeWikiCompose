package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler
import com.poke.pokewikicompose.utils.md5

class RegisterRepository {
    companion object {
        @Volatile
        private var instance: RegisterRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository().also { instance = it }
            }
    }

    suspend fun register(email: String, password: String): NetworkState<UserBean> {
        val md5Password = md5(password)
        return UnifiedExceptionHandler.nullableHandleSuspend {
            ServerApi.create().register(email, md5Password)
        }
    }
}