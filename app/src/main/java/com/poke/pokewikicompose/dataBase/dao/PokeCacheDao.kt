package com.poke.pokewikicompose.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonImageCacheBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean

@Dao
interface PokeSearchCacheDao {
    @Query("SELECT * FROM PokemonSearchBean LIMIT :start, 10")
    fun getWithPage(start: Int): List<PokemonSearchBean>?

    @Insert
    fun insert(pokeCache: PokemonSearchBean)

    @Update
    fun update(pokeCache: PokemonSearchBean)
}

@Dao
interface PokeDetailCacheDao {
    @Query("SELECT * FROM PokemonDetailBean")
    fun getAll(): List<PokemonDetailBean>?

    @Insert
    fun insert(pokeCache: PokemonDetailBean)
}

@Dao
interface PokeImageCacheDao {
    @Query("SELECT * FROM PokemonImageCacheBean")
    fun getAllCache(): List<PokemonImageCacheBean>?

    @Insert
    fun insert(pokeCache: PokemonImageCacheBean)

    @Update
    fun update(pokeCache: PokemonImageCacheBean)
}