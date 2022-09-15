package com.poke.pokewikicompose.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting
import com.poke.pokewikicompose.dataBase.data.repository.RegisterRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = RegisterRepository.getInstance()
    private val _viewStates = MutableStateFlow(RegisterViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<RegisterViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: RegisterViewAction) {
        when (viewAction) {
            is RegisterViewAction.UpdateEmail -> _viewStates.setState { copy(email = viewAction.email) }
            is RegisterViewAction.UpdatePassword -> _viewStates.setState { copy(password = viewAction.password) }
            is RegisterViewAction.UpdateCertain -> _viewStates.setState { copy(certain = viewAction.certain) }
            is RegisterViewAction.OnRegisterClicked -> register()
        }
    }

    private fun register() {
        if (viewStates.value.same)
            viewModelScope.launch {
                flow {
                    registerLogic()
                    emit("注册成功")
                }.onStart {
                    _viewEvents.setEvent(RegisterViewEvent.ShowLoadingDialog)
                }.onEach {
                    _viewEvents.setEvent(RegisterViewEvent.DismissLoadingDialog)
                }.catch {
                    _viewEvents.setEvent(
                        RegisterViewEvent.DismissLoadingDialog,
                        RegisterViewEvent.ShowToast(it.message ?: "")
                    )
                }.flowOn(Dispatchers.IO).collect()
            }
        else
            viewModelScope.launch {
                _viewEvents.setEvent(RegisterViewEvent.ShowToast("两次密码不一致"))
            }
    }

    private suspend fun registerLogic() {
        when (val result = repository.register(viewStates.value.email, viewStates.value.password)) {
            is NetworkState.Success -> {
                result.data?.let {
                    _viewEvents.setEvent(RegisterViewEvent.TransIntent)
                    //写入全局
                    AppContext.userData = it
                    //Room持久化
                    GlobalDataBase.database.userDao().deleteAll()
                    GlobalDataBase.database.userDao().insert(it)
                    GlobalDataBase.database.localSettingDao().insertLocalSetting(
                        LocalSetting(
                            userId = it.userId,
                            isAutoCache = false,
                            searchHistory = ArrayList()
                        )
                    )
                } ?: result.msg?.let { _viewEvents.setEvent(RegisterViewEvent.ShowToast(it)) }
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }
}

data class RegisterViewState(
    val email: String = "",
    val password: String = "",
    val certain: String = ""
) {
    val same: Boolean = password == certain
    val enable: Boolean =
        email.isNotBlank() && password.isNotBlank() && certain.isNotBlank()
}

sealed class RegisterViewAction {
    object OnRegisterClicked : RegisterViewAction()
    data class UpdateEmail(val email: String) : RegisterViewAction()
    data class UpdatePassword(val password: String) : RegisterViewAction()
    data class UpdateCertain(val certain: String) : RegisterViewAction()
}

sealed class RegisterViewEvent {
    object ShowLoadingDialog : RegisterViewEvent()
    object DismissLoadingDialog : RegisterViewEvent()
    object TransIntent : RegisterViewEvent()
    data class ShowToast(val msg: String) : RegisterViewEvent()
}