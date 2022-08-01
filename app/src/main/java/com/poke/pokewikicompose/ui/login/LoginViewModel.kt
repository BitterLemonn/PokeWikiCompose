package com.poke.pokewikicompose.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.data.repository.LoginRepository
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.md5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository.getInstance()
    var viewStates by mutableStateOf(LoginViewState())
        private set
    private val _viewEvent = Channel<LoginViewEvent>(Channel.BUFFERED)
    val viewEvent = _viewEvent.receiveAsFlow()

    fun dispatch(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.UpdateEmail -> updateEmail(viewAction.email)
            is LoginViewAction.UpdatePassword -> updatePassword(viewAction.password)
            is LoginViewAction.ChangeErrorState -> updateErrorState(viewAction.error)
            is LoginViewAction.OnLoginClicked -> login()
        }
    }

    private fun updateEmail(email: String) {
        viewStates = viewStates.copy(email = email)
    }

    private fun updatePassword(password: String) {
        viewStates = viewStates.copy(password = password)
    }

    private fun updateErrorState(error: Boolean) {
        viewStates = viewStates.copy(error = error)
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登陆成功")
            }.onStart {
                _viewEvent.send(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvent.send(LoginViewEvent.DismissLoadingDialog)
            }.catch {
                _viewEvent.send(LoginViewEvent.DismissLoadingDialog)
                _viewEvent.send(LoginViewEvent.ShowToast(it.message ?: ""))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun loginLogic() {
        val email = viewStates.email
        val password = viewStates.password

        // 测试
        if (email == "1" && password == "1")
            _viewEvent.send(LoginViewEvent.TransIntent)
        else
            when (val result = repository.getAuth(email, md5(password))) {
                is NetworkState.Success -> {
                    _viewEvent.send(LoginViewEvent.TransIntent)
//                //写入全局
//                AppContext.userData = result.data
//                //写入内存
//                sp.edit().putString(USER_DATA, Gson().toJson(result.data)).apply()
                }
                is NetworkState.Error -> throw Exception(result.msg)
            }
    }
}


// 状态
data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val error: Boolean = false
) {
    val enable: Boolean = email.isNotBlank() && password.isNotBlank()
}

sealed class LoginViewAction {
    object OnLoginClicked : LoginViewAction()
    data class UpdateEmail(val email: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
    data class ChangeErrorState(val error: Boolean) : LoginViewAction()
}

sealed class LoginViewEvent {
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
    object TransIntent : LoginViewEvent()
    data class ShowToast(val msg: String) : LoginViewEvent()
}