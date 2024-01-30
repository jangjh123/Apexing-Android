package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.databinding.FragmentStatisticsBinding
import jyotti.apexing.apexing_android.ui.component.MatchAdapter
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsUiContract.UiEffect.ScrollToTop
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(FragmentStatisticsBinding::inflate) {
    override val viewModel: StatisticsViewModel by viewModels()
    private val statisticsAdapter = MatchAdapter(
        onClickRecordingDesc = { goToRecordingHelpPage() },
        onClickRefreshDesc = { goToRefreshHelpPage() }
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
                    is ScrollToTop -> {

                    }
                }
            }
        }
    }

    private fun goToRecordingHelpPage() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_recording))))
    }

    private fun goToRefreshHelpPage() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_refresh))))
    }
}