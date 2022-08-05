package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.PokemonEvolutionArrayConverter
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter
import kotlinx.serialization.Serializable

@Entity
@TypeConverters(StringArrayConverter::class, PokemonEvolutionArrayConverter::class)
data class PokemonIntroBean(
    @ColumnInfo val intro_text: String? = "",
    @ColumnInfo val poke_evolution: ArrayList<PokemonEvolutionBean> = ArrayList(),
    @ColumnInfo val general_abilities: ArrayList<String> = ArrayList(),
    @ColumnInfo val hidden_abilities: ArrayList<String>? = ArrayList(),
    @ColumnInfo val genus: String = "",
    @ColumnInfo val habitat: String? = "",
    @ColumnInfo val shape: String = ""
)

@Serializable
@Entity
data class PokemonEvolutionBean(
    @ColumnInfo val id: Int = 0,
    @ColumnInfo val img_url: String = "",
    @ColumnInfo val min_level: Int = 0,
    @ColumnInfo val name: String = ""
)