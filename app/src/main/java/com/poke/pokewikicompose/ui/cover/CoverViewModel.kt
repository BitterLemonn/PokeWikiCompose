package com.poke.pokewikicompose.ui.cover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.dataBase.GlobalDataBase
import com.poke.pokewikicompose.utils.AppCache
import com.poke.pokewikicompose.utils.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CoverViewModel : ViewModel() {

    private val _viewEvent = Channel<CoverViewEvent>(Channel.BUFFERED)
    val viewEvent = _viewEvent.receiveAsFlow()

    fun checkUserInfo() {
        viewModelScope.launch {
            flow {
                val user = GlobalDataBase.database.userDao().getAll()
                user?.let {
                    if (it.size == 1) {
                        _viewEvent.send(CoverViewEvent.GetLoginInfo)
                        AppContext.userData = it[0]

                        // 读取用户个人设定
                        val setting =
                            GlobalDataBase.database.localSettingDao().getLocalSettingWithUserID(
                                userID = it[0].userId
                            )
                        AppContext.localSetting = setting!!
                        // 读取缓存图片
                        val pathCache = GlobalDataBase.database.pokeImageCacheDao().getAllCache()
                        pathCache?.let { paths ->
                            AppCache.pokemonPathCache.addAll(paths)
                        }
                        // 读取缓存详细页
                        val detailCache = GlobalDataBase.database.pokeDetailCacheDao().getAll()
                        detailCache?.let { detail ->
                            AppCache.pokemonDetailCache.addAll(detail)
                        }
                    }
                }
                _viewEvent.send(CoverViewEvent.OverProcess)
                Logger.d("获取用户信息成功\nuser: $user")
                emit("获取用户信息成功")
            }.flowOn(Dispatchers.IO).collect()
        }
    }

}

sealed class CoverViewEvent {
    object OverProcess : CoverViewEvent()
    object GetLoginInfo : CoverViewEvent()
}