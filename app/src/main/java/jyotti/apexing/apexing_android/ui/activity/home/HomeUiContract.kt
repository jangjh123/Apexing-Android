package jyotti.apexing.apexing_android.ui.activity.home

import jyotti.apexing.apexing_android.base.BaseContract
import jyotti.apexing.apexing_android.ui.activity.home.HomeUiContract.UiEffect
import jyotti.apexing.apexing_android.ui.activity.home.HomeUiContract.UiState

interface HomeUiContract : BaseContract<UiState, UiEffect> {
    data class UiState(
        val id: String = ""
    )

    sealed interface UiEffect {

    }
}