package com.poke.pokewikicompose.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.data.bean.PokemonImageCacheBean
import com.poke.pokewikicompose.dataBase.data.repository.DownloadRepository
import com.poke.pokewikicompose.dataBase.data.repository.DownloadType
import com.poke.pokewikicompose.ui.theme.*
import okio.buffer
import okio.sink
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun getAppVersionName(context: Context): String {
    var versionName = ""
    try {
        val pm = context.packageManager
        val pi = pm.getPackageInfo(context.packageName, 0);
        versionName = pi.versionName;
        if (versionName == null || versionName.isEmpty()) {
            return ""
        }
    } catch (e: Exception) {
        Logger.e("VersionInfo", "Exception", e)
    }
    return versionName
}

fun dip2px(context: Context, dpValue: Double): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun md5(string: String): String {
    if (string.isEmpty()) {
        return ""
    }
    val md5: MessageDigest?
    try {
        md5 = MessageDigest.getInstance("MD5")
        val bytes = md5.digest(string.toByteArray())
        val result = StringBuilder()
        for (b in bytes) {
            var temp = Integer.toHexString(b.toInt() and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result.append(temp)
        }
        println(result.toString())
        return result.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

fun getPokemonColor(pokemonColor: String): Color {
    return when (pokemonColor) {
        "black" -> PokemonBlack
        "blue" -> PokemonBlue
        "brown" -> PokemonBrown
        "gray" -> PokemonGray
        "green" -> PokemonGreen
        "pink" -> PokemonPink
        "purple" -> PokemonPurple
        "red" -> PokemonRed
        "white" -> PokemonWhite
        "yellow" -> PokemonYellow
        else -> {
            Logger.e("wrong pokemon color: $pokemonColor")
            Color.Transparent
        }
    }
}

suspend fun downloadWithType(
    pokeID: String,
    type: DownloadType,
    context: Context
): String {
    when (val result =
        DownloadRepository.getInstance().getImageWithTypeAndID(type, pokeID.toInt())) {
        is NetworkState.Success -> {
            val path = getFilePath(type, pokeID.toInt(), context)
            val dest = File(path)
            val sink = dest.sink()
            val bufferedSink = sink.buffer()
            bufferedSink.writeAll(result.data!!.source())
            bufferedSink.close()

            var item = AppCache.getPathItem(pokeID.toInt())
            when (type) {
                DownloadType.SMALL -> {
                    item = item?.copy(pokemonID = pokeID.toInt(), smallPath = path)
                        ?: PokemonImageCacheBean(pokemonID = pokeID.toInt(), smallPath = path)
                    AppCache.updatePathItem(item)
                }
                DownloadType.BIG -> {
                    item = item?.copy(pokemonID = pokeID.toInt(), bigPath = path)
                        ?: PokemonImageCacheBean(pokemonID = pokeID.toInt(), bigPath = path)
                    AppCache.updatePathItem(item)
                }
            }


            return if (dest.length() > 0L) path
            else ""
        }
        is NetworkState.Error -> throw Exception(result.msg)
    }
}