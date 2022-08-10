package com.poke.pokewikicompose.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.repository.RegisterRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = RegisterRepository.getInstance()
    var viewStates by mutableStateOf(RegisterViewState())
        private set
    private val _viewEvents = Channel<RegisterViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(viewAction: RegisterViewAction) {
        when (viewAction) {
            is RegisterViewAction.UpdateEmail -> viewStates =
                viewStates.copy(email = viewAction.email)
            is RegisterViewAction.UpdatePassword ->
                viewStates = viewStates.copy(password = viewAction.password)
            is RegisterViewAction.UpdateCertain ->
                viewStates = viewStates.copy(certain = viewAction.certain)
            is RegisterViewAction.OnRegisterClicked -> register()
        }
    }

    private fun register() {
        viewModelScope.launch{
            flow {
                registerLogic()
                emit("注册成功")
            }.onStart {
                _viewEvents.send(RegisterViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.send(RegisterViewEvent.DismissLoadingDialog)
            }.catch {
                _viewEvents.send(RegisterViewEvent.DismissLoadingDialog)
                _viewEvents.send(RegisterViewEvent.ShowToast(it.message ?: ""))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun registerLogic() {
        when (val result = repository.register(viewStates.email, viewStates.password)) {
            is NetworkState.Success -> {
                _viewEvents.send(RegisterViewEvent.TransIntent)
                //写入全局
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