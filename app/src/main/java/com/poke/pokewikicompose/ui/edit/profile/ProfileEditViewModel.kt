package com.poke.pokewikicompose.ui.edit.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.repository.ProfileEditRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class ProfileEditViewModel : ViewModel() {
    private val repository = ProfileEditRepository.getInstance()
    private val _viewStates = MutableStateFlow(ProfileEditViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<ProfileEditViewEvents>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: ProfileEditViewActions) {
        when (viewAction) {
            is ProfileEditViewActions.UpdateUsername -> _viewStates.setState { copy(username = viewAction.username) }
            is ProfileEditViewActions.ChangeUsername -> changeUsername()
            is ProfileEditViewActions.ChangeUserIcon -> changeUserIcon(viewAction.path)
        }
    }

    private fun changeUsername() {
        viewModelScope.launch {
            flow {
                changeUsernameLogic()
                emit("修改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileEditViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    ProfileEditViewEvents.DismissLoadingDialog,
                    ProfileEditViewEvents.SuccessChangeName
                )
            }.catch { e ->
                _viewEvents.setEvent(
                    ProfileEditViewEvents.DismissLoadingDialog,
                    ProfileEditViewEvents.ShowToast(e.message ?: "未知异常,请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeUsernameLogic() {
        val userID = AppContext.userData.userId
        val token = AppContext.userData.token
        val username = _viewStates.value.username
        when (val result = repository.changeUserName(username, userID.toString(), token)) {
            is NetworkState.Success -> {
                AppContext.userData = result.data
                // Room持久化储存
                GlobalDataBase.database.userDao().deleteAll()
                GlobalDataBase.database.userDao().insert(result.data)
            }
            is NetworkState.Error -> throw Exception(result.msg)
            is NetworkState.NoNeedResponse -> throw Exception(result.msg)
        }
    }

    private fun changeUserIcon(path: String) {
        viewModelScope.launch {
            flow {
                changeUserIconLogic(path)
                emit("更改成功")
            }.onStart {
                _viewEvents.setEvent(ProfileEditViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    ProfileEditViewEvents.DismissLoadingDialog,
                    ProfileEditViewEvents.SuccessChangeIcon
                )
            }.catch { e ->
                _viewEvents.setEvent(
                    ProfileEditViewEvents.DismissLoadingDialog,
                    ProfileEditViewEvents.ShowToast(e.message ?: "未知异常,请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changeUserIconLogic(path: String) {
        val userID = AppContext.userData.userId.toString()
        val iconFile = File(path)
        if (iconFile.exists()) {
            Logger.d("fileSize: ${iconFile.length()}")
            when (val result = repository.changeUserIcon(userID = userID, iconFile = iconFile)) {
                is NetworkState.Success -> {
                    Logger.d("response: ${result.data}")
                    AppContext.userData = AppContext.userData.copy(profile_photo = result.data)

                    GlobalDataBase.database.userDao().deleteAll()
                    GlobalDataBase.database.userDao().insert(AppContext.userData)
                }
                is NetworkState.Error -> throw Exception(result.msg)
                is NetworkState.NoNeedResponse -> throw Exception(result.msg)
            }
        } else
            throw Exception("无法获取文件信息,请重试")
    }
}

data class ProfileEditViewStates(
    val username: String = AppContext.userData.username
)

sealed class ProfileEditViewActions {
    data class UpdateUsername(val username: String) : ProfileEditViewActions()
    object ChangeUsername : ProfileEditViewActions()
    data class ChangeUserIcon(val path: String) : ProfileEditViewActions()
}

sealed class ProfileEditViewEvents {
    object ShowLoadingDialog : ProfileEditViewEvents()
    object DismissLoadingDialog : ProfileEditViewEvents()
    object SuccessChangeName : ProfileEditViewEvents()
    object SuccessChangeIcon: ProfileEditViewEvents()
    data class ShowToast(val msg: String) : ProfileEditViewEvents()
}