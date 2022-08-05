package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter

@TypeConverters(StringArrayConverter::class)
@Entity
data class PokemonSearchBean(
    @PrimaryKey val pokemon_id: String,
    @ColumnInfo val img_path: String,
    @ColumnInfo val img_url: String,
    @ColumnInfo val pokemon_name: String,
    @ColumnInfo val pokemon_type: ArrayList<String>
)
