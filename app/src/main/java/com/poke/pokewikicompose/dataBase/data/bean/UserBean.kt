package com.poke.pokewikicompose.dataBase.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 用户信息数据类
 * @param email 用户邮箱
 * @param profile_photo 用户头像url
 * @param token
 * @param userId 用户id
 * @param username 用户名
 */
@Entity
@Serializable
data class UserBean(
    @PrimaryKey val userId: Int,
    @ColumnInfo val email: String,
    @ColumnInfo var profile_photo: String?,
    @ColumnInfo val token: String,
    @ColumnInfo val username: String
)

@Entity
data class LocalSetting(
    @PrimaryKey val userId: Int,
    @ColumnInfo val isAutoCache: Boolean
)
