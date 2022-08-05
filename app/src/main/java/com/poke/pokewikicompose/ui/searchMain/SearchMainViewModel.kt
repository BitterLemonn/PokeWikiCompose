package com.poke.pokewikicompose.ui.searchMain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.SearchMainRepository
import com.poke.pokewikicompose.utils.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
            is SearchMainViewAction.GetDataWithStateTest -> getDataWithPageTest(viewAction.isRefresh)
            is SearchMainViewAction.GetDataWithState -> getDataWithPage(viewAction.isRefresh)
            is SearchMainViewAction.ResetLoadingState -> viewStates =
                viewStates.copy(loadingState = INIT)

        }
    }

    private fun getDataWithPageTest(isRefresh: Boolean) {
        val searchItems = ArrayList<PokemonSearchBean>()
        val typeArrayList = ArrayList<String>()
        typeArrayList.add("草")
        typeArrayList.add("毒")
        for (i in 0 until 10) {
            searchItems.add(
                PokemonSearchBean(
                    img_url = "https://cdn.jsdelivr.net/gh/PokeAPI/sprites@master/sprites/pokemon/3.png",
                    pokemon_name = "妙蛙花",
                    pokemon_id = "#003",
                    pokemon_type = typeArrayList,
                    img_path = ""
                )
            )
        }
        viewModelScope.launch {
            _viewEvent.send(SearchMainViewEvent.ShowLoadingDialog)
            delay(1_000)
            _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)
            _viewEvent.send(SearchMainViewEvent.UpdateData(searchItems))
        }
    }

    private fun getDataWithPage(isRefresh: Boolean) {
        viewModelScope.launch {
            flow {
                if (isRefresh) viewStates = viewStates.copy(page = 1)
                getDataWithPageLogic()
                emit("获取成功")
            }.onStart {
                _viewEvent.send(SearchMainViewEvent.ShowLoadingDialog)
                viewStates = viewStates.copy(loadingState = LOADING)
            }.onEach {
                _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)
                viewStates = viewStates.copy(loadingState = SUCCESS)
            }.catch {
                _viewEvent.send(SearchMainViewEvent.DismissLoadingDialog)
                viewStates = viewStates.copy(loadingState = ERROR)
            }
        }
    }

    private suspend fun getDataWithPageLogic() {
        when (val result = repository.getAllPokemonWithPage(viewStates.page)) {
            is NetworkState.Success -> _viewEvent.send(SearchMainViewEvent.UpdateData(result.data))
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }
}

data class SearchMainViewState(
    val loadingState: Int = INIT,
    val page: Int = 1
)

sealed class SearchMainViewAction {
    data class GetDataWithState(val isRefresh: Boolean) : SearchMainViewAction()
    object ResetLoadingState : SearchMainViewAction()
    data class GetDataWithStateTest(val isRefresh: Boolean) : SearchMainViewAction()
}

sealed class SearchMainViewEvent {
    data class UpdateData(val pokemonDataList: ArrayList<PokemonSearchBean>) : SearchMainViewEvent()
    object ShowLoadingDialog : SearchMainViewEvent()
    object DismissLoadingDialog : SearchMainViewEvent()
    data class ShowToast(val msg: String) : SearchMainViewEvent()
}
