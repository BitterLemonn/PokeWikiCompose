package com.poke.pokewikicompose.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poke.pokewikicompose.dataBase.dao.LocalSettingDao
import com.poke.pokewikicompose.dataBase.dao.UserDao
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.bean.UserBean

@Database(
    entities = [UserBean::class, PokemonSearchBean::class, PokemonDetailBean::class, LocalSetting::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        private const val DATABASE_NAME = "pokeWiki.db"

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                    .build().also { instance = it }
            }
        }
    }

    abstract fun userDao(): UserDao

    abstract fun localSettingDao(): LocalSettingDao
}