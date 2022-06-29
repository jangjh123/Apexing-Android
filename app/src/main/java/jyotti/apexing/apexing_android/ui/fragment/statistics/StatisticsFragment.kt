package jyotti.apexing.apexing_android.ui.fragment.statistics

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.databinding.FragmentStatisticsBinding
import jyotti.apexing.apexing_android.ui.component.MatchAdapter
import jyotti.apexing.apexing_android.ui.component.SuggestDialogFragment
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()

    val matchAdapter = MatchAdapter(
        onClickRecordingDesc = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_recording)))
            startActivity(intent)
        })

    override fun onStart() {
        super.onStart()
        binding.fragment = this@StatisticsFragment
    }

    override fun startProcess() {
        showMatch()
    }

    override fun setObservers() {
        viewModel.getDatabaseMessage().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                viewModel.getMatch().collect {
                    matchAdapter.submitData(lifecycle, it)
                }
            }

            viewModel.suggestRating()
            dismissProgress()
        }

        viewModel.getRatingMessage().observe(viewLifecycleOwner) {
            SuggestDialogFragment(
                getString(R.string.rating_suggestion),
                onClickConfirm = {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_url))))
                    viewModel.setRatingState()
                }
            ).also {
                it.show(childFragmentManager, "rating_dialog")
            }
        }
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