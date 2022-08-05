package com.poke.pokewikicompose.dataBase.data

import androidx.room.TypeConverter
import com.poke.pokewikicompose.dataBase.data.bean.PokemonEvolutionBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonIntroBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonMovesBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonStateBean
import com.poke.pokewikicompose.utils.JsonConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.util.*

class StringArrayConverter {
    @TypeConverter
    fun decodeStringArrayData(data: String?): List<String> {
        return if (data == null || data.isBlank())
            Collections.emptyList()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodeStringArrayData(data: List<String>): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class PokemonEvolutionArrayConverter {
    @TypeConverter
    fun decodePokemonEvolutionArrayData(data: String?): List<PokemonEvolutionBean> {
        return if (data == null || data.isBlank())
            Collections.emptyList()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodePokemonEvolutionArrayData(data: List<PokemonEvolutionBean>): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class PokemonMoveArrayConverter {
    @TypeConverter
    fun decodePokemonMoveArrayData(data: String?): List<PokemonEvolutionBean> {
        return if (data == null || data.isBlank())
            Collections.emptyList()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodePokemonMoveArrayData(data: List<PokemonEvolutionBean>): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class PokemonIntroConverter {
    @TypeConverter
    fun decodePokemonIntroData(data: String?): PokemonIntroBean {
        return if (data == null || data.isBlank())
            PokemonIntroBean()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodePokemonIntroData(data: PokemonIntroBean): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class PokemonMovesConverter {
    @TypeConverter
    fun decodePokemonMovesData(data: String?): PokemonMovesBean {
        return if (data == null || data.isBlank())
            PokemonMovesBean()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodePokemonMovesData(data: PokemonMovesBean): String {
        return JsonConverter.Json.encodeToString(data)
    }
}

class PokemonStateConverter {
    @TypeConverter
    fun decodePokemonStateData(data: String?): PokemonStateBean {
        return if (data == null || data.isBlank())
            PokemonStateBean()
        else
            JsonConverter.Json.decodeFromString(data)
    }

    @TypeConverter
    fun encodePokemonStateData(data: PokemonStateBean): String {
        return JsonConverter.Json.encodeToString(data)
    }
}