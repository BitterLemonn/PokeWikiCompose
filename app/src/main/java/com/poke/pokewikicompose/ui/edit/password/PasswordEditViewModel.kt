package com.poke.pokewikicompose.ui.edit.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.repository.PasswordEditRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PasswordEditViewModel : ViewModel() {
    private val _viewStates = MutableStateFlow(PasswordEditViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<PasswordEditViewEvents>()
    val viewEvents = _viewEvents.asSharedFlow()
    private val repository = PasswordEditRepository.getInstance()

    fun dispatch(viewActions: PasswordEditViewActions) {
        when (viewActions) {
            is PasswordEditViewActions.UpdateNewPassword ->
                _viewStates.setState { copy(newPassword = viewActions.newPassword) }
            is PasswordEditViewActions.UpdateOldPassword ->
                _viewStates.setState { copy(oldPassword = viewActions.oldPassword) }
            is PasswordEditViewActions.ChangePassword -> changePassword()
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            flow {
                changePasswordLogic()
                emit("修改成功")
            }.onStart {
                _viewEvents.setEvent(PasswordEditViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(PasswordEditViewEvents.DismissLoadingDialog)
            }.catch { e ->
                _viewEvents.setEvent(
                    PasswordEditViewEvents.DismissLoadingDialog,
                    PasswordEditViewEvents.ShowToast(e.message ?: "未知错误,请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun changePasswordLogic() {
        val newPassword = _viewStates.value.newPassword
        val oldPassword = _viewStates.value.oldPassword
        val token = AppContext.userData.token
        val userID = AppContext.userData.userId

        when (val result = repository.updateUserPassword(oldPassword, newPassword, userID, token)) {
            is NetworkState.Success -> {
                _viewEvents.setEvent(PasswordEditViewEvents.SuccessChange)
                AppContext.userData = result.data
                //Room持久化
                GlobalDataBase.database.userDao().deleteAll()
                GlobalDataBase.database.userDao().insert(result.data)
            }
            is NetworkState.Error -> throw Exception(result.msg)
            is NetworkState.NoNeedResponse -> throw Exception(result.msg)
        }
    }
}

data class PasswordEditViewStates(
    val newPassword: String = "",
    val oldPassword: String = ""
) {
    val enabled: Boolean = oldPassword.isNotBlank() && newPassword.isNotBlank()
}

sealed class PasswordEditViewActions {
    data class UpdateNewPassword(val newPassword: String) : PasswordEditViewActions()
    data class UpdateOldPassword(val oldPassword: String) : PasswordEditViewActions()
    object ChangePassword : PasswordEditViewActions()
}

sealed class PasswordEditViewEvents {
    object ShowLoadingDialog : PasswordEditViewEvents()
    object DismissLoadingDialog : PasswordEditViewEvents()
    object SuccessChange : PasswordEditViewEvents()
    data class ShowToast(val msg: String) : PasswordEditViewEvents()
}