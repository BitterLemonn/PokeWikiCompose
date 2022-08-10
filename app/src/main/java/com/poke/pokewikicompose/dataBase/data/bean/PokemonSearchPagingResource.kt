package com.poke.pokewikicompose.dataBase.data.bean

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.poke.pokewikicompose.dataBase.data.repository.ServerApi

class PokemonSearchPagingResource : PagingSource<Int, PokemonSearchBean>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonSearchBean>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonSearchBean> {
        return try {
            val currentPage = params.key ?: 1
            val data = ServerApi.create().getAllWithPage(currentPage)

            if (data.status == 200) {
                LoadResult.Page(
                    data = data.data,
                    prevKey = null,
                    nextKey = currentPage + 1
                )
            } else {
                LoadResult.Error(throwable = Throwable(data.msg ?: "未知错误，请联系管理员"))
            }
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }
}

class PokemonSearchPagingResourceTest : PagingSource<Int, PokemonSearchBean>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonSearchBean>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonSearchBean> {
        val searchItems = ArrayList<PokemonSearchBean>()
        val typeArrayList = ArrayList<String>()
        typeArrayList.add("草")
        typeArrayList.add("毒")
        for (i in 0 until 10) {
            searchItems.add(
                PokemonSearchBean(
                    img_url = "https://cdn.jsdelivr.net/gh/PokeAPI/sprites@master/sprites/pokemon/3.png",
                    pokemon_name = "妙蛙花",
                    pokemon_id = "#003",
                    pokemon_type = typeArrayList,
                    img_path = ""
                )
            )
        }
        return LoadResult.Page(
            data = searchItems,
            prevKey = null,
            nextKey = null
        )
    }

}