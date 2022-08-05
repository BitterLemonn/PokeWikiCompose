package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.PokemonMoveArrayConverter
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity
@TypeConverters(PokemonMoveArrayConverter::class)
data class PokemonMovesBean(
    @ColumnInfo val moves: ArrayList<PokemonMoveBean> = ArrayList()
)

@Serializable
@Entity
data class PokemonMoveBean(
    @ColumnInfo val accuracy: Int? = 0,
    @ColumnInfo val damage_type: String = "",
    @ColumnInfo val level: Int? = null,
    @ColumnInfo val move_id: Int = 0,
    @ColumnInfo val move_name: String = "",
    @ColumnInfo val power: Int? = 0,
    @ColumnInfo val pp: Int = 0,
    @ColumnInfo val type_name: String = ""
)