package com.poke.pokewikicompose.ui.searchMain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.SearchMainRepository
import com.poke.pokewikicompose.utils.INIT
import com.poke.pokewikicompose.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchMainViewModel : ViewModel() {
    private val repository = SearchMainRepository.getInstance()
    private val _viewStates = MutableStateFlow(SearchMainViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvent = SharedFlowEvents<SearchMainViewEvent>()
    val viewEvent = _viewEvent.asSharedFlow()

    fun dispatch(viewAction: SearchMainViewAction) {
        when (viewAction) {
            is SearchMainViewAction.GetData -> getData()
            is SearchMainViewAction.ResetLoadingState -> _viewStates.value =
                _viewStates.value.copy(loadingState = INIT)

        }
    }

    private fun getData() {
        viewModelScope.launch {
            flow {
                getDataLogic()
                emit("获取宝可梦数据")
            }.onStart {
                _viewEvent.setEvent(SearchMainViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvent.setEvent(SearchMainViewEvent.DismissLoadingDialog)
            }.catch { e ->
                _viewEvent.setEvent(
                    SearchMainViewEvent.DismissLoadingDialog,
                    SearchMainViewEvent.ShowToast(e.message ?: "未知异常，请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getDataLogic() {
        val page = _viewStates.value.page
        when (val result = repository.getAllPokemonWithPage(page)) {
            is NetworkState.Success ->
                _viewStates.setState { copy(page = page + 1, pokemonItemList = result.data) }
            is NetworkState.Error -> throw Exception(result.msg)
            else -> {}
        }
    }
}

data class SearchMainViewState(
    val page: Int = 1,
    val loadingState: Int = INIT,
    val pokemonItemList: ArrayList<PokemonSearchBean> = ArrayList()
)

sealed class SearchMainViewAction {
    object GetData : SearchMainViewAction()
    object ResetLoadingState : SearchMainViewAction()
}

sealed class SearchMainViewEvent {
    object ShowLoadingDialog : SearchMainViewEvent()
    object DismissLoadingDialog : SearchMainViewEvent()
    data class ShowToast(val msg: String) : SearchMainViewEvent()
}
