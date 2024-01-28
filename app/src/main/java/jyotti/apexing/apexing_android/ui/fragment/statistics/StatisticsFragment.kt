package jyotti.apexing.apexing_android.ui.fragment.statistics

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.databinding.FragmentStatisticsBinding
import jyotti.apexing.apexing_android.ui.component.MatchAdapter
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(FragmentStatisticsBinding::inflate) {
    override val viewModel: StatisticsViewModel by viewModels()
    private val statisticsAdapter = MatchAdapter(
        onClickRecordingDesc = {

        },
        onClickRefreshDesc = {

        }
    )

    override fun initBinding() {
        bind {
            vm = viewModel
            rvMatch.adapter = statisticsAdapter
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collect { uiEffect ->
                when (uiEffect) {
                    else -> Unit
                }
            }
        }
    }
}