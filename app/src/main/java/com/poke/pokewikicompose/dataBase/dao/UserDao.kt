package com.poke.pokewikicompose.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.poke.pokewikicompose.dataBase.data.bean.UserBean

@Dao
interface UserDao {
    @Query("SELECT * FROM userbean")
    fun getAll(): List<UserBean>?

    @Insert
    fun insert(userBean: UserBean)

    @Query("DELETE FROM userbean")
    fun deleteAll()
}