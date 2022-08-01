package com.poke.pokewikicompose.data.bean

/**
 * 用户信息数据类
 * @param email 用户邮箱
 * @param profile_photo 用户头像url
 * @param token
 * @param userId 用户id
 * @param username 用户名
 */
data class UserBean(
    val email: String = "",
    var profile_photo: String? = null,
    val token: String = "",
    val userId: String = "",
    val username: String = ""
)
