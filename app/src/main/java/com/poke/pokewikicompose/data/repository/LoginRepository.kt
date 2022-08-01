package com.poke.pokewikicompose.data.repository

import com.example.pokewiki.bean.PokemonSearchBean
import com.poke.pokewikicompose.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import java.net.SocketTimeoutException

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
        val result = try {
            ServerApi.create().getAuth(email, password)
        }
        catch (e: SocketTimeoutException){
            return NetworkState.Error("连接超时，网络好像被精灵球捕捉了QAQ")
        }catch (e: Exception) {
            e.printStackTrace()
            return NetworkState.Error("未知错误，请联系管理员")
        }
        result.let {
            when (it.status) {
                200 -> return NetworkState.Success(it.data)
                else -> return NetworkState.Error(it.msg ?: "未知错误，请联系管理员")
            }
        }
    }
}