package com.poke.pokewikicompose.ui.main.searchMain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.dataBase.GlobalDataBase.Companion.context
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.DownloadRepository
import com.poke.pokewikicompose.dataBase.data.repository.DownloadType
import com.poke.pokewikicompose.dataBase.data.repository.SearchMainRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.INIT
import com.poke.pokewikicompose.utils.NetworkState
import com.poke.pokewikicompose.utils.downloadWithType
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchMainViewModel : ViewModel() {
    private val repository = SearchMainRepository.getInstance()
    private val download = DownloadRepository.getInstance()
    private val _viewStates = MutableStateFlow(SearchMainViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<SearchMainViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()

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
                val page = _viewStates.value.page
                val list = GlobalDataBase.database.pokeSearchCacheDao().getWithPage((page - 1) * 10)
                Logger.d("cached search: $list")
                list?.let {
                    if (list.isNotEmpty()) {
                        Logger.d("get cache list: $list")
                        _viewStates.setState { copy(page = page + 1, pokemonItemUpdate = list) }
                    } else
                        getDataLogic()
                } ?: getDataLogic()
                emit("?????????????????????")
            }.onStart {
                _viewEvents.setEvent(SearchMainViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(SearchMainViewEvent.DismissLoadingDialog)
            }.catch { e ->
                _viewEvents.setEvent(
                    SearchMainViewEvent.DismissLoadingDialog,
                    SearchMainViewEvent.ShowToast(e.message ?: "?????????????????????????????????")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getDataLogic() {
        val page = _viewStates.value.page
        when (val result = repository.getAllPokemonWithPage(page)) {
            is NetworkState.Success -> {
                result.data?.let {
                    _viewStates.setState { copy(page = page + 1, pokemonItemUpdate = it) }
                    // ????????????
                    if (AppContext.localSetting.isAutoCache) {
                        it.forEach { item ->
                            downloadWithType(
                                pokeID = item.pokemon_id,
                                type = DownloadType.SMALL,
                                context = context
                            )
                            GlobalDataBase.database.pokeSearchCacheDao().insert(item)
                        }
                    }
                } ?: result.msg?.let { _viewEvents.setEvent(SearchMainViewEvent.ShowToast(it)) }
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }
}

data class SearchMainViewState(
    val page: Int = 1,
    val loadingState: Int = INIT,
    val pokemonItemUpdate: List<PokemonSearchBean> = ArrayList()
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
