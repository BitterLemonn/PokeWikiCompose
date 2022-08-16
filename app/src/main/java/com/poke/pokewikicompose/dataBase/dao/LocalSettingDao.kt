package com.poke.pokewikicompose.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting


@Dao
interface LocalSettingDao {
    @Query("SELECT * FROM localsetting WHERE userId == :userID")
    fun getLocalSettingWithUserID(userID: Int): LocalSetting?

    @Update
    fun updateLocalSetting(setting: LocalSetting)

    @Insert
    fun insertLocalSetting(setting: LocalSetting)
}