package jyotti.apexing.apexing_android.ui.activity.home

import jyotti.apexing.apexing_android.base.BaseContract

interface HomeUiContract : BaseContract<HomeUiContract.UiState, HomeUiContract.UiEffect> {
    data class UiState(
        val id: String = ""
    )

    sealed interface UiEffect {

    }
}