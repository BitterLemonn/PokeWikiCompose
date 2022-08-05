package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.PokemonIntroConverter
import com.poke.pokewikicompose.dataBase.data.PokemonMovesConverter
import com.poke.pokewikicompose.dataBase.data.PokemonStateConverter
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter

@Entity
@TypeConverters(
    StringArrayConverter::class,
    PokemonIntroConverter::class,
    PokemonStateConverter::class,
    PokemonMovesConverter::class
)
data class PokemonDetailBean(
    @PrimaryKey val pokemon_id: String,
    @ColumnInfo val img_url: String,
    @ColumnInfo var is_star: Int,
    @ColumnInfo val pokemon_color: String,
    @ColumnInfo val pokemon_name: String,
    @ColumnInfo val pokemon_type: ArrayList<String>,

    @ColumnInfo val poke_intro: PokemonIntroBean,
    @ColumnInfo val poke_stat: PokemonStateBean,
    @ColumnInfo val poke_moves: PokemonMovesBean
)