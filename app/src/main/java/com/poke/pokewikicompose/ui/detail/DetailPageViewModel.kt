package com.poke.pokewikicompose.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poke.pokewikicompose.dataBase.data.bean.PokemonDetailBean
import com.poke.pokewikicompose.dataBase.data.repository.DetailRepository
import com.poke.pokewikicompose.utils.AppContext
import com.poke.pokewikicompose.utils.NetworkState
import com.zj.mvi.core.SharedFlowEvents
import com.zj.mvi.core.setEvent
import com.zj.mvi.core.setState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailPageViewModel : ViewModel() {
    private val repository = DetailRepository.getInstance()
    private val _viewStates = MutableStateFlow(DetailPageViewStates())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<DetailPageViewEvents>()
    val viewEvents = _viewEvents.asSharedFlow()

    private var pokemonID = 0

    fun dispatch(viewActions: DetailPageViewActions) {
        when (viewActions) {
            is DetailPageViewActions.ClickLike -> clickLike()
            is DetailPageViewActions.GetDetailWithID -> getDetail(viewActions.pokemonID)
        }
    }

    private fun clickLike() {
        _viewStates.setState { copy(isLike = !isLike) }
        viewModelScope.launch {
            flow {
                clickLikeLogic()
                emit("修改成功")
            }.onStart {
                _viewEvents.setEvent(DetailPageViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(DetailPageViewEvents.DismissLoadingDialog)
            }.catch {
                _viewEvents.setEvent(
                    DetailPageViewEvents.DismissLoadingDialog,
                    DetailPageViewEvents.ShowToast(it.message ?: "")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun clickLikeLogic() {
        val isLike = _viewStates.value.isLike
        val userID = AppContext.userData.userId.toString()
        when (repository.clickLike(userID, pokemonID, isLike)) {
            is NetworkState.Success -> _viewEvents.setEvent(DetailPageViewEvents.LikeActionSuccess)
            else -> {
                _viewStates.setState { copy(isLike = !isLike) }
                _viewEvents.setEvent(
                    DetailPageViewEvents.LikeActionFailure(
                        if (isLike) "收藏失败" else "取消收藏失败"
                    )
                )
            }
        }
    }

    private fun getDetail(id: Int) {
        pokemonID = id
        viewModelScope.launch {
            flow {
                getDetailLogic(id)
                emit("获取成功")
            }.onStart {
                _viewEvents.setEvent(DetailPageViewEvents.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(DetailPageViewEvents.DismissLoadingDialog)
            }.catch {
                _viewEvents.setEvent(
                    DetailPageViewEvents.DismissLoadingDialog,
                    DetailPageViewEvents.ShowToast(it.message ?: "")
                )
            }.flowOn(Dispatchers.IO).collect()
        }
    }

    private suspend fun getDetailLogic(id: Int) {
        val userID = AppContext.userData.userId.toString()
        when (val result = repository.getDetail(pokeID = id, userID = userID)) {
            is NetworkState.Success -> _viewEvents.setEvent(DetailPageViewEvents.TransDetail(result.data))
            is NetworkState.Error -> throw Exception(result.msg)
            is NetworkState.NoNeedResponse -> throw Exception(result.msg)
        }
    }
}

data class DetailPageViewStates(
    val isLike: Boolean = false
)

sealed class DetailPageViewActions {
    object ClickLike : DetailPageViewActions()
    data class GetDetailWithID(val pokemonID: Int) : DetailPageViewActions()
}

sealed class DetailPageViewEvents {
    object LikeActionSuccess : DetailPageViewEvents()
    data class LikeActionFailure(val msg: String) : DetailPageViewEvents()
    data class TransDetail(val pokeDetail: PokemonDetailBean) : DetailPageViewEvents()
    object ShowLoadingDialog : DetailPageViewEvents()
    object DismissLoadingDialog : DetailPageViewEvents()
    data class ShowToast(val msg: String) : DetailPageViewEvents()
}