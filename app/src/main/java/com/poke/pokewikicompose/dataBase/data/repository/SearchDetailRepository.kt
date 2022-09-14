package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.PokemonSearchMode
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler

class SearchDetailRepository {
    companion object {
        @Volatile
        private var instance: SearchDetailRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: SearchDetailRepository().also { instance = it }
            }
    }

    suspend fun getSearch(
        searchKey: String,
        searchMode: PokemonSearchMode
    ): NetworkState<ArrayList<PokemonSearchBean>> {
        return UnifiedExceptionHandler.handleSuspend {
            when (searchMode) {
                PokemonSearchMode.NAME -> ServerApi.create().getPokemonByName(searchKey)
                PokemonSearchMode.GEN -> ServerApi.create().getPokemonByGen(searchKey)
                PokemonSearchMode.TYPE -> ServerApi.create().getPokemonByType(searchKey)
            }
        }
    }
}