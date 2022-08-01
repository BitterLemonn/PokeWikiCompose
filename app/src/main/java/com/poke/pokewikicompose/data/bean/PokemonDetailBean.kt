package com.poke.pokewikicompose.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PokemonDetailBean(
    @ColumnInfo val pokemon_id : String = "",
    @ColumnInfo val img_url: String = "",
    @ColumnInfo var is_star: Int = 0,
    @ColumnInfo val pokemon_color: String = "",
    @ColumnInfo val pokemon_name : String = "",
    @ColumnInfo val pokemon_type : ArrayList<String> = ArrayList(),

    @ColumnInfo val poke_intro : PokemonIntroBean = PokemonIntroBean(),
    @ColumnInfo val poke_stat : PokemonStateBean = PokemonStateBean(),
    @ColumnInfo val poke_moves : PokemonMovesBean = PokemonMovesBean()
)