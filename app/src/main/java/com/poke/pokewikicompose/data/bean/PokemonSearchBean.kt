package com.poke.pokewikicompose.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PokemonSearchBean(
    @ColumnInfo val pokemon_id: String = "",
    @ColumnInfo val img_path : String = "",
    @ColumnInfo val img_url: String = "",
    @ColumnInfo val pokemon_name: String = "",
    @ColumnInfo val pokemon_type: ArrayList<String> = ArrayList()
)
