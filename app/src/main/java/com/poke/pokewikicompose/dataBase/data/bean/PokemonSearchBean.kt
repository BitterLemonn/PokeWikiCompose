package com.poke.pokewikicompose.dataBase.data.bean

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.poke.pokewikicompose.dataBase.data.StringArrayConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@TypeConverters(StringArrayConverter::class)
@Entity
@Serializable
@Parcelize
data class PokemonSearchBean(
    @PrimaryKey val pokemon_id: String,
    @ColumnInfo val img_path: String? = null,
    @ColumnInfo val img_url: String,
    @ColumnInfo val pokemon_name: String,
    @ColumnInfo val pokemon_type: List<String>
) : Parcelable
