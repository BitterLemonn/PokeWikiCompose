package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class PokemonStateBean(
    @ColumnInfo val HP: Int = 0,
    @ColumnInfo val ATK: Int = 0,
    @ColumnInfo val DEF: Int = 0,
    @ColumnInfo val SATK: Int = 0,
    @ColumnInfo val SDEF: Int = 0,
    @ColumnInfo val SPD: Int = 0
)
