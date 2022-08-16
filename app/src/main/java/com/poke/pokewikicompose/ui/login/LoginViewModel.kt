package com.poke.pokewikicompose.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.data.bean.LocalSetting
import com.poke.pokewikicompose.dataBase.data.bean.UserBean
import com.poke.pokewikicompose.dataBase.data.repository.LoginRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.md5
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository.getInstance()
    private val _viewStates = MutableStateFlow(LoginViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvent = SharedFlowEvents<LoginViewEvent>()
    val viewEvent = _viewEvent.asSharedFlow()

    var userResult: UserBean? = null

    fun dispatch(viewAction: LoginViewAction) {
        when (viewAction) {
            is LoginViewAction.UpdateEmail ->
                _viewStates.setState { copy(email = viewAction.email) }
            is LoginViewAction.UpdatePassword ->
                _viewStates.setState { copy(password = viewAction.password) }
            is LoginViewAction.ChangeErrorState -> _viewStates.setState { copy(error = viewAction.error) }
            is LoginViewAction.OnLoginClicked -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登陆成功")
            }.onStart {
                _viewEvent.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                userResult?.let { GlobalDataBase.database.userDao().insert(userResult!!) }
                _viewEvent.setEvent(LoginViewEvent.DismissLoadingDialog)
            }.catch {
                _viewEvent.setEvent(
                    LoginViewEvent.DismissLoadingDialog,
                    LoginViewEvent.ShowToast(it.message ?: "")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun loginLogic() {
        val email = viewStates.value.email
        val password = viewStates.value.password
        when (val result = repository.getAuth(email, md5(password))) {
            is NetworkState.Success -> {
                _viewEvent.setEvent(LoginViewEvent.TransIntent)
                //写入全局
                AppContext.userData = result.data
                //Room持久化
                GlobalDataBase.database.userDao().deleteAll()
                GlobalDataBase.database.userDao().insert(result.data)
                val setting = GlobalDataBase.database.localSettingDao()
                    .getLocalSettingWithUserID(result.data.userId)
                if (setting != null)
                    GlobalDataBase.database.localSettingDao().updateLocalSetting(setting)
                else
                    GlobalDataBase.database.localSettingDao().insertLocalSetting(
                        LocalSetting(
                            userId = result.data.userId,
                            isAutoCache = false
                        )
                    )
            }
            is NetworkState.NoNeedResponse -> throw Exception(result.msg)
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