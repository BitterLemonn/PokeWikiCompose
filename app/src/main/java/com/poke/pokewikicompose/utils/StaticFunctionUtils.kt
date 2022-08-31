package com.poke.pokewikicompose.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.ui.theme.*
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