package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonImageCacheBean(
    @PrimaryKey val pokemonID: Int,
    @ColumnInfo val smallPath: String? = null,
    @ColumnInfo val bigPath: String? = null
)