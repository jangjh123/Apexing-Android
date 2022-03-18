package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.databinding.FragmentStatisticsBinding
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
        viewModel.setTimeOut()
    }

    override fun setObservers() {
        viewModel.getTimeOutMessage().observe(viewLifecycleOwner) {
            if (isProgressShowing()) {
                dismissProgress()
                setOnFailureView(failureView = binding.layoutNull, successView = binding.layoutView)
            }
        }

        viewModel.getNetworkMessage().observe(viewLifecycleOwner) {
            setOnFailureView(
                failureView = binding.layoutNull,
                successView = binding.layoutView
            )
            dismissProgress()
        }

        viewModel.getDatabaseMessage().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                viewModel.getMatch().collect {
                    matchAdapter.submitData(lifecycle, it)
                }
            }
            setOnSuccessView(
                successView = binding.layoutView,
                failureView = binding.layoutNull
            )
            dismissProgress()
        }
    }

    fun onClickRetry(view: View) {
        showProgress()
        startProcess()
    }

    fun onClickGoUp(view: View) {
        if ((binding.rvMatch.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 10) {
            binding.rvMatch.scrollToPosition(10)
        }
        binding.rvMatch.smoothScrollToPosition(0)
    }

    private fun showMatch() {
        viewModel.updateMatch()
    }
}