package jyotti.apexing.apexing_android.ui.fragment.statistics

import jyotti.apexing.apexing_android.base.BaseContract
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels

interface StatisticsUiContract : BaseContract<StatisticsUiContract.UiState, StatisticsUiContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val statistics: List<MatchModels> = emptyList()
    )

    sealed interface UiEffect {

    }
}