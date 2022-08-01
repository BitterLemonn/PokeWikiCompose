package com.poke.pokewikicompose.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RegisterViewModel : ViewModel() {
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
            else -> {}
        }
    }

    private fun updateEmail(email: String) {

    }

    private fun updatePassword(password: String) {

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