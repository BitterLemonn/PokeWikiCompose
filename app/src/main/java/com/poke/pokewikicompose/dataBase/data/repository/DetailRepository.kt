package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

class DetailRepository {
    companion object {
        @Volatile
        private var instance: DetailRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DetailRepository().also { instance = it }
            }
    }

    suspend fun getDetail(pokeID: Int, userID: String): NetworkState<PokemonDetailBean> {
        return UnifiedExceptionHandler.handleSuspend {
            ServerApi.create().getPokemonDetail(poke_id = pokeID, user_id = userID)
        }
    }

    suspend fun clickLike(userID: String, pokeID: Int, like: Boolean): NetworkState<Any> {
        return if (like)
            UnifiedExceptionHandler.nullableHandleSuspend {
                ServerApi.create().like(user_id = userID, poke_id = pokeID)
            } else
            UnifiedExceptionHandler.nullableHandleSuspend {
                ServerApi.create().unlike(user_id = userID, poke_id = pokeID)
            }
    }
}