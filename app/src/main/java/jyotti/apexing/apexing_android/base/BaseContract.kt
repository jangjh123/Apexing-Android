package jyotti.apexing.apexing_android.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE, EFFECT> {
    val uiState: StateFlow<STATE>
    val uiEffect: SharedFlow<EFFECT>
}