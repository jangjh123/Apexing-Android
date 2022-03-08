package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.fragment.app.viewModels
import com.apexing.apexing_android.R
import com.apexing.apexing_android.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragment

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()
}