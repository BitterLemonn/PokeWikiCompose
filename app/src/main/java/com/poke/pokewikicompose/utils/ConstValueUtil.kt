package com.poke.pokewikicompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting
import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.ui.theme.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

// 导航
const val COVER_PAGE = "COVER_PAGE"
const val LOGIN_PAGE = "LOGIN_PAGE"
const val REGISTER_PAGE = "REGISTER_PAGE"
const val MAIN_PAGE = "MAIN_PAGE"
const val SEARCH_MAIN_PAGE = "SEARCH_MAIN_PAGE"

// 服务器地址
const val SERVER_URL = "http://192.168.2.12:8080/v1/"
const val DOWNLOAD_URL = "http://192.168.128.34:8080/image/"

// 信号
const val INIT = 0
const val LOADING = 10000
const val SUCCESS = 20001
const val ERROR = -1

//sp
const val SHARED_KEY = "POKE_WIKI_SHARED_KEY"
const val USER_DATA = "USER_DATA"

@Composable
fun GetColorByText(text: String): Color {
    return when (text) {
        stringResource(R.string.pokemon_type_general) -> General
        stringResource(R.string.pokemon_type_fly) -> Fly
        stringResource(R.string.pokemon_type_fire) -> Fire
        stringResource(R.string.pokemon_type_psychic) -> Psychic
        stringResource(R.string.pokemon_type_water) -> Water
        stringResource(R.string.pokemon_type_insect) -> Insect
        stringResource(R.string.pokemon_type_electric) -> Electric
        stringResource(R.string.pokemon_type_rock) -> Rock
        stringResource(R.string.pokemon_type_grass) -> Grass
        stringResource(R.string.pokemon_type_ghost) -> Ghost
        stringResource(R.string.pokemon_type_ice) -> Ice
        stringResource(R.string.pokemon_type_dragon) -> Dragon
        stringResource(R.string.pokemon_type_fight) -> Fight
        stringResource(R.string.pokemon_type_evil) -> Evil
        stringResource(R.string.pokemon_type_poison) -> Poison
        stringResource(R.string.pokemon_type_steel) -> Steel
        stringResource(R.string.pokemon_type_ground) -> Ground
        stringResource(R.string.pokemon_type_fairy) -> Fairy

        stringResource(R.string.pokemon_gen_1) -> Generation1
        stringResource(R.string.pokemon_gen_2) -> Generation2
        stringResource(R.string.pokemon_gen_3) -> Generation3
        stringResource(R.string.pokemon_gen_4) -> Generation4
        stringResource(R.string.pokemon_gen_5) -> Generation5
        stringResource(R.string.pokemon_gen_6) -> Generation6
        stringResource(R.string.pokemon_gen_7) -> Generation7
        stringResource(R.string.pokemon_gen_8) -> Generation8

        else -> PokeBallRed
    }
}

object JsonConverter {
    @OptIn(ExperimentalSerializationApi::class)
    val Json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = false
        explicitNulls = false
    }
}

object AppContext {
    var userData = UserBean(
        userId = 0,
        email = "",
        profile_photo = null,
        token = "",
        username = ""
    )

    var localSetting = LocalSetting(
        userId = 0,
        isAutoCache = false
    )

    fun logout() {
        userData = UserBean(
            userId = 0,
            email = "",
            profile_photo = null,
            token = "",
            username = ""
        )
    }
}