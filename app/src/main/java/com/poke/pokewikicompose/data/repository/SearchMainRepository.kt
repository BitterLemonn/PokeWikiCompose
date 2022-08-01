package com.poke.pokewikicompose.data.repository

import com.example.pokewiki.bean.PokemonSearchBean
import com.poke.pokewikicompose.utils.NetworkState

class SearchMainRepository {
    companion object {
        @Volatile
        private var instance: SearchMainRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: SearchMainRepository().also { instance = it }
            }
    }

    suspend fun getAllPokemonWithPage(page: Int): NetworkState<ArrayList<PokemonSearchBean>> {
        val result = try {
            ServerApi.create().getAllWithPage(page)
        } catch (e: Exception) {
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