package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apexing.apexing_android.R
import com.apexing.apexing_android.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.ui.component.MatchAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()

    val matchAdapter = MatchAdapter(
        onClickRefresh = {
            showProgress()
            startProcess()
        })

    override fun onStart() {
        super.onStart()
        binding.fragment = this@StatisticsFragment
    }

    override fun startProcess() {
        showMatch()
    }

    override fun setObserver() {
        viewModel.getDatabaseMessage().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                viewModel.getMatch().collect {
                    matchAdapter.submitData(lifecycle, it)
                }
            }
            binding.rvMatch.visibility = View.VISIBLE
            dismissProgress()
        }
    }

    fun onClickRetry(view: View) {
        showProgress()
        startProcess()
    }

    fun onClickGoUp(view: View) {
        if ((binding.rvMatch.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 50) {
            binding.rvMatch.scrollToPosition(25)
        }
        binding.rvMatch.smoothScrollToPosition(0)
    }

    private fun showMatch() {
        viewModel.updateMatch()
    }
}