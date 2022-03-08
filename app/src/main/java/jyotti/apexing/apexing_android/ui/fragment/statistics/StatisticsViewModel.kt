package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.repository.StatisticsRepository
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repository: StatisticsRepository) :
    ViewModel() {
}