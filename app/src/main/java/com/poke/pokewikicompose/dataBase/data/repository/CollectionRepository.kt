package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

class CollectionRepository {
    companion object {
        @Volatile
        private var instance: CollectionRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CollectionRepository().also { instance = it }
            }
    }

    suspend fun getCollection(userID: String): NetworkState<ArrayList<PokemonSearchBean>> {
        return UnifiedExceptionHandler.handleSuspend { ServerApi.create().getMyCollection(userID) }
    }

    suspend fun delCollection(userID: String, pokeID: Int): NetworkState<String> {
        return UnifiedExceptionHandler.handleSuspend { ServerApi.create().unlike(userID, pokeID) }
    }
}