package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.PokemonIntroConverter
import com.poke.pokewikicompose.dataBase.data.PokemonMovesConverter
import com.poke.pokewikicompose.dataBase.data.PokemonStateConverter
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter
import kotlinx.serialization.Serializable

@Entity
@TypeConverters(
    StringArrayConverter::class,
    PokemonIntroConverter::class,
    PokemonStateConverter::class,
    PokemonMovesConverter::class
)
@Serializable
data class PokemonDetailBean(
    @PrimaryKey val pokemon_id: String,
    @ColumnInfo val img_url: String,
    @ColumnInfo var is_star: Int,
    @ColumnInfo val pokemon_color: String,
    @ColumnInfo val pokemon_name: String,
    @ColumnInfo val pokemon_type: List<String>,

    @ColumnInfo val poke_intro: PokemonIntroBean,
    @ColumnInfo val poke_stat: PokemonStateBean,
    @ColumnInfo val poke_moves: PokemonMovesBean
){
    companion object{
        fun getEmpty(): PokemonDetailBean{
            return PokemonDetailBean(
                "",
                "",
                0,
                "white",
                "",
                ArrayList(),
                PokemonIntroBean(),
                PokemonStateBean(),
                PokemonMovesBean()
            )
        }
    }
}