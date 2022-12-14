package com.poke.pokewikicompose.utils

import android.content.Context
import android.os.Environment
import android.support.annotation.WorkerThread
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.R
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonImageCacheBean
import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.dataBase.data.repository.DownloadType
import com.poke.pokewikicompose.ui.theme.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File

// 导航
const val COVER_PAGE = "COVER_PAGE"
const val LOGIN_PAGE = "LOGIN_PAGE"
const val REGISTER_PAGE = "REGISTER_PAGE"
const val MAIN_PAGE = "MAIN_PAGE"
const val SEARCH_MAIN_PAGE = "SEARCH_MAIN_PAGE"
const val PROFILE_EDIT_PAGE = "PROFILE_EDIT_PAGE"
const val PASSWORD_EDIT_PAGE = "PASSWORD_EDIT_PAGE"
const val FEEDBACK_PAGE = "FEEDBACK_PAGE"
const val COLLECTION_PAGE = "COLLECTION_PAGE"
const val DETAIL_PAGE = "DETAIL_PAGE"
const val SEARCH_PAGE = "SEARCH_PAGE"
const val SEARCH_DETAIL_PAGE = "SEARCH_DETAIL_PAGE"

// 服务器地址
const val SERVER_URL = "http://192.168.2.12:8080/v1/"
const val DOWNLOAD_URL = "http://192.168.2.12:8080/image/"

// 信号
const val INIT = 0
const val LOADING = 10000
const val SUCCESS = 20001
const val ERROR = -1

enum class PokemonSearchMode {
    NAME, TYPE, GEN
}

val PokemonTypeList = listOf(
    "一般",
    "飞行",
    "火",
    "超能力",
    "水",
    "虫",
    "电",
    "岩石",
    "草",
    "幽灵",
    "冰",
    "龙",
    "格斗",
    "恶",
    "毒",
    "钢",
    "地面",
    "妖精"
)

val PokemonGenList = listOf(
    "第一世代",
    "第二世代",
    "第三世代",
    "第四世代",
    "第五世代",
    "第六世代",
    "第七世代",
    "第八世代",
)

@Composable
fun getColorByText(text: String): Color {
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

fun getFilePath(type: DownloadType, pokeID: Int, context: Context): String {
    val root =
        "${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/${
            when (type) {
                DownloadType.SMALL -> "small"
                DownloadType.BIG -> "big"
            }
        }"
    if (!File(root).exists()) {
        Logger.d("create: $root")
        File(root).mkdir()
    }
    return "$root/$pokeID.png"
}

object JsonConverter {
    @OptIn(ExperimentalSerializationApi::class)
    val Json: Json = Json {
        // 忽略实体类中未出现的json键值
        ignoreUnknownKeys = true
        // 忽略实体类空值
        explicitNulls = false
        // 不编码默认值
        encodeDefaults = false
        // 忽略json空值
        coerceInputValues = true
    }
}

object AppCache {
    val pokemonPathCache = ArrayList<PokemonImageCacheBean>()
    val pokemonDetailCache = ArrayList<PokemonDetailBean>()

    fun getPathItem(id: Int): PokemonImageCacheBean? {
        for (item in pokemonPathCache) {
            if (item.pokemonID == id) return item
        }
        return null
    }

    @WorkerThread
    fun updatePathItem(item: PokemonImageCacheBean) {
        val preItem = pokemonPathCache.filter { it.pokemonID == item.pokemonID }
        if (preItem.isEmpty()) {
            GlobalDataBase.database.pokeImageCacheDao().insert(item)
        } else {
            pokemonPathCache.remove(preItem[0])
            GlobalDataBase.database.pokeImageCacheDao().update(item)
        }
        pokemonPathCache.add(item)
    }

    fun getDetailItem(id: Int): PokemonDetailBean? {
        for (item in pokemonDetailCache) {
            if (item.pokemon_id.toInt() == id) return item
        }
        return null
    }

    @WorkerThread
    fun insertDetailItem(item: PokemonDetailBean) {
        if (item !in pokemonDetailCache) {
            pokemonDetailCache.add(item)
            GlobalDataBase.database.pokeDetailCacheDao().insert(item)
        }
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
        isAutoCache = false,
        searchHistory = ArrayList()
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