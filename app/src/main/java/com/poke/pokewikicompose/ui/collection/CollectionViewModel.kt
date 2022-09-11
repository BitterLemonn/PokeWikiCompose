package com.poke.pokewikicompose.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.PokemonSearchBean
import com.poke.pokewikicompose.dataBase.data.repository.CollectionRepository
import com.poke.pokewikicompose.utils.*
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {
    private val repository = CollectionRepository.getInstance()
    private val _viewStates = MutableStateFlow(CollectionViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<CollectionViewEvents>()
    val viewEvents = _viewEvents.asSharedFlow()

    fun dispatch(viewActions: CollectionViewActions) {
        when (viewActions) {
            is CollectionViewActions.ResetState -> _viewStates.setState { copy(delState = INIT) }
            is CollectionViewActions.GetCollection -> getCollection()
            is CollectionViewActions.DelCollection -> delCollection(viewActions.pokeID)
        }
    }

    private fun getCollection() {
        viewModelScope.launch {
            flow {
                getCollectionLogic()
                emit("获取成功")
            }.catch { e ->
                _viewEvents.setEvent(
                    CollectionViewEvents.ShowToast(e.message ?: "未知异常,请联系管理员")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getCollectionLogic() {
        val userID = AppContext.userData.userId
        when (val result = repository.getCollection(userID.toString())) {
            is NetworkState.Success -> {
                result.data?.let {
                    _viewStates.setState { copy(collection = it) }
                } ?: throw Exception(result.msg)
            }
            is NetworkState.Error -> throw Exception(result.msg)
        }
    }

    private fun delCollection(pokeID: Int) {
        viewModelScope.launch {
            flow {
                delCollectionLogic(pokeID)
                emit("删除成功")
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun delCollectionLogic(pokeID: Int) {
        val userID = AppContext.userData.userId
        when (repository.delCollection(userID.toString(), pokeID)) {
            is NetworkState.Success -> _viewStates.setState { copy(delState = SUCCESS) }
            is NetworkState.Error -> _viewStates.setState { copy(delState = ERROR) }
        }
    }
}

data class CollectionViewStates(
    val collection: ArrayList<PokemonSearchBean> = ArrayList(),
    val delState: Int = INIT
)

sealed class CollectionViewActions {
    object ResetState : CollectionViewActions()
    object GetCollection : CollectionViewActions()
    data class DelCollection(val pokeID: Int) : CollectionViewActions()
}

sealed class CollectionViewEvents {
    data class ShowToast(val msg: String) : CollectionViewEvents()
}