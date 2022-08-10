package com.poke.pokewikicompose.ui.searchMain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.SearchMainRepository
import com.poke.pokewikicompose.utils.INIT
import com.poke.pokewikicompose.utils.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchMainViewModel : ViewModel() {
    private val repository = SearchMainRepository.getInstance()
    var viewStates by mutableStateOf(SearchMainViewState())
        private set
    private val _viewEvent = Channel<SearchMainViewEvent>(Channel.BUFFERED)
    val viewEvent = _viewEvent.receiveAsFlow()

    fun dispatch(viewAction: SearchMainViewAction) {
        when (viewAction) {
            is SearchMainViewAction.GetDataWithState -> getDataWithState(viewAction.isRefresh)
            is SearchMainViewAction.ResetLoadingState -> viewStates =
                viewStates.copy(loadingState = INIT)

        }
    }

    private fun getDataWithState(isRefresh: Boolean) {
        viewModelScope.launch {
            flow {
                getDataWithStateLogic(isRefresh)
                emit("获取宝可梦数据")
            }.onStart {
                _viewEvent.send(SearchMainViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)
            }.catch { e ->
                _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)
                _viewEvent.send(SearchMainViewEvent.ShowToast(e.message ?: "未知异常，请联系管理员"))
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getDataWithStateLogic(isRefresh: Boolean) {
        if (isRefresh) {
            viewStates = viewStates.copy(page = 1, pokemonItemList = ArrayList())
        }
        val page = viewStates.page
        when (val result = repository.getAllPokemonWithPage(page)) {
            is NetworkState.Success -> {
                viewStates = viewStates.copy(page = page + 1)
                viewStates.pokemonItemList.addAll(result.data)
                _viewEvent.send(SearchMainViewEvent.UpdateDataList)
            }
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
    data class GetDataWithState(val isRefresh: Boolean) : SearchMainViewAction()
    object ResetLoadingState : SearchMainViewAction()
}

sealed class SearchMainViewEvent {
    object ShowLoadingDialog : SearchMainViewEvent()
    object DismissLoadingDialog : SearchMainViewEvent()
    data class ShowToast(val msg: String) : SearchMainViewEvent()
    object UpdateDataList : SearchMainViewEvent()
}
