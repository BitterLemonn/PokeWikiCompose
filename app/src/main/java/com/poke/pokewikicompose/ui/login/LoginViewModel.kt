package com.poke.pokewikicompose.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.dataBase.data.repository.LoginRepository
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.utils.AppContext
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

    var userResult: UserBean? = null

    fun dispatch(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.UpdateEmail ->
                viewStates = viewStates.copy(email = viewAction.email)
            is LoginViewAction.UpdatePassword ->
                viewStates = viewStates.copy(password = viewAction.password)
            is LoginViewAction.ChangeErrorState -> updateErrorState(viewAction.error)
            is LoginViewAction.OnLoginClicked -> login()
            is LoginViewAction.CheckLoginState -> checkLoginState()
        }
    }

    private fun updateErrorState(error: Boolean) {
        viewStates = viewStates.copy(error = error)
    }

    private fun checkLoginState() {
        val user = GlobalDataBase.database.userDao().getAll()
        user?.let {
            viewModelScope.launch {
                if (it.size == 1)
                    _viewEvent.send(LoginViewEvent.TransIntent)
            }
        }
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

        // 测试专用
        if (email == "1" && password == "1") {
            _viewEvent.send(LoginViewEvent.TransIntent)
            val data =
                UserBean(email = email, token = "123123123", userId = "1", username = "宝可梦训练师")
            //写入全局
            AppContext.userData = data
            //临时保存
            userResult = data
        } else
        // 正常流程
            when (val result = repository.getAuth(email, md5(password))) {
                is NetworkState.Success -> {
                    _viewEvent.send(LoginViewEvent.TransIntent)
                    //写入全局
                    AppContext.userData = result.data
                    //Room持久化
                    GlobalDataBase.database.userDao().deleteAll()
                    GlobalDataBase.database.userDao().insert(result.data)
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
    object CheckLoginState : LoginViewAction()
}

sealed class LoginViewEvent {
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
    object TransIntent : LoginViewEvent()
    data class ShowToast(val msg: String) : LoginViewEvent()
}