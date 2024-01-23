package jyotti.apexing.apexing_android.ui.fragment.main

import jyotti.apexing.apexing_android.base.BaseContract

interface MainUiContract : BaseContract<MainUiContract.UiState, MainUiContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false
    )

    sealed interface UiEffect {

    }
}