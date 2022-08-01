package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

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
        return UnifiedExceptionHandler.handleSuspend { ServerApi.create().getAllWithPage(page) }
    }
}