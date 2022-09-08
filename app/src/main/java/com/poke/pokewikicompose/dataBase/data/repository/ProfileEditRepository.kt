package com.poke.pokewikicompose.dataBase.data.repository

import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.UnifiedExceptionHandler
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileEditRepository {
    companion object {
        @Volatile
        private var instance: ProfileEditRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ProfileEditRepository().also { instance = it }
            }
    }

    suspend fun changeUserName(
        userName: String,
        userId: String,
        token: String
    ): NetworkState<UserBean> {
        return UnifiedExceptionHandler.handleSuspend {
            ServerApi.create().updateUsername(username = userName, userId = userId, token = token)
        }
    }

    suspend fun changeUserIcon(
        userID: String,
        iconFile: File
    ): NetworkState<String> {
        val iconRequestBody = iconFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Builder()
            .addFormDataPart("userId", userID)
            .addFormDataPart("profilePicture", iconFile.name, iconRequestBody)
            .setType(MultipartBody.FORM)
            .build()

        return UnifiedExceptionHandler.handleSuspend {
            ServerApi.create().updateUserIcon(part)
        }
    }
}