package com.poke.pokewikicompose.ui.search.searchDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.SearchDetailRepository
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.PokemonGenList
import com.poke.pokewikicompose.utils.PokemonSearchMode
import com.poke.pokewikicompose.utils.PokemonTypeList
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchDetailViewModel : ViewModel() {
    private val repository = SearchDetailRepository.getInstance()
    private val _viewStates = MutableStateFlow(SearchDetailViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<SearchDetailViewEvents>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewAction: SearchDetailViewAction) {
        when (viewAction) {
            is SearchDetailViewAction.UpdateSearchKey ->
                _viewStates.setState { copy(searchKey = viewAction.searchKey) }
            is SearchDetailViewAction.UpdateSearchMode ->
                _viewStates.setState { copy(searchMode = viewAction.searchMode) }
            is SearchDetailViewAction.GetSearch -> getSearch()
        }
    }

    private fun getSearch() {
        viewModelScope.launch {
            flow {
                getSearchLogic()
                emit("获取成功")
            }.onStart {
                _viewEvents.setEvent(SearchDetailViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(SearchDetailViewEvents.DismissLoadingDialog)
            }.catch { e ->
                _viewEvents.setEvent(
                    SearchDetailViewEvents.DismissLoadingDialog,
                    SearchDetailViewEvents.ShowToast(e.message ?: "未知错误,请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getSearchLogic() {
        val searchKey = _viewStates.value.searchKey
        var searchMode = _viewStates.value.searchMode
        if (searchKey in PokemonGenList) searchMode = PokemonSearchMode.GEN
        if (searchKey in PokemonTypeList) searchMode = PokemonSearchMode.TYPE
        when (val result = repository.getSearch(searchKey, searchMode)) {
            is NetworkState.Success -> result.data?.let {
                _viewStates.setState { copy(searchResult = it) }
            } ?: result.msg?.let { throw Exception(it) }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }
}

data class SearchDetailViewStates(
    val searchKey: String = "",
    val searchMode: PokemonSearchMode = PokemonSearchMode.NAME,
    val searchResult: List<PokemonSearchBean> = ArrayList()
)

sealed class SearchDetailViewAction {
    data class UpdateSearchKey(val searchKey: String) : SearchDetailViewAction()
    data class UpdateSearchMode(val searchMode: PokemonSearchMode) : SearchDetailViewAction()
    object GetSearch : SearchDetailViewAction()
}

sealed class SearchDetailViewEvents {
    object ShowLoadingDialog : SearchDetailViewEvents()
    object DismissLoadingDialog : SearchDetailViewEvents()
    data class ShowToast(val msg: String) : SearchDetailViewEvents()
}