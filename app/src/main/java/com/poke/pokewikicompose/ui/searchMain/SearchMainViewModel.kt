package com.poke.pokewikicompose.ui.searchMain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchPagingResource
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchPagingResourceTest
import com.poke.pokewikicompose.dataBase.data.repository.SearchMainRepository
import com.poke.pokewikicompose.utils.INIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchMainViewModel : ViewModel() {
    private val repository = SearchMainRepository.getInstance()
    var viewStates by mutableStateOf(SearchMainViewState())
        private set
    private val _viewEvent = Channel<SearchMainViewEvent>(Channel.BUFFERED)
    val viewEvent = _viewEvent.receiveAsFlow()

    fun dispatch(viewAction: SearchMainViewAction) {
        when (viewAction) {
            is SearchMainViewAction.GetDataWithStateTest -> getDataWithPageTest()
            is SearchMainViewAction.GetDataWithState -> getData()
            is SearchMainViewAction.ResetLoadingState -> viewStates =
                viewStates.copy(loadingState = INIT)

        }
    }

    private fun getDataWithPageTest() {
        viewModelScope.launch {
            _viewEvent.send(SearchMainViewEvent.ShowLoadingDialog)
            delay(1_000)
            _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)

            val list = Pager(PagingConfig(pageSize = 1)) {
                PokemonSearchPagingResourceTest()
            }.flow.flowOn(Dispatchers.IO).collectAsLazyPagingItems()
            viewStates = viewStates.copy(pokemonItemList = list)
        }
    }

    private fun getData() {
        viewModelScope.launch {
            val list = Pager(PagingConfig(pageSize = 1)) {
                PokemonSearchPagingResource()
            }.flow.flowOn(Dispatchers.IO).collectAsLazyPagingItems()
            viewStates = viewStates.copy(pokemonItemList = list)
        }
    }
}

data class SearchMainViewState(
    val loadingState: Int = INIT,
    val pokemonItemList: LazyPagingItems<PokemonSearchBean>? = null
)

sealed class SearchMainViewAction {
    object GetDataWithState : SearchMainViewAction()
    object ResetLoadingState : SearchMainViewAction()
    object GetDataWithStateTest : SearchMainViewAction()
}

sealed class SearchMainViewEvent {
    object ShowLoadingDialog : SearchMainViewEvent()
    object DismissLoadingDialog : SearchMainViewEvent()
    data class ShowToast(val msg: String) : SearchMainViewEvent()
}
