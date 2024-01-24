package jyotti.apexing.apexing_android.ui.fragment.main

import jyotti.apexing.apexing_android.base.BaseContract
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.UserInfo

interface MainUiContract : BaseContract<MainUiContract.UiState, MainUiContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val maps: List<Map> = emptyList(),
        val notice: String = "",
        val craftings: List<Crafting> = emptyList(),
        val userInfo: UserInfo? = null,
        val newses: List<News> = emptyList()
    )

    sealed interface UiEffect {

    }
}