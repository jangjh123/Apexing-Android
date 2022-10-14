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
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.ui.component.CustomDialogFragment
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
        },
        onClickRefreshDesc = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_refresh)))
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

        viewModel.refreshIndexLiveData.observe(viewLifecycleOwner) {
            matchAdapter.setRefreshIndex(it)
        }

        viewModel.getRatingMessage().observe(viewLifecycleOwner) {
            SuggestDialogFragment(
                getString(R.string.rating_suggestion),
                onClickConfirm = {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.app_url))
                        )
                    )
                    viewModel.setRatingState()
                }
            ).also {
                it.show(childFragmentManager, "rating_dialog")
            }
        }

        viewModel.getNoElementMessage().observe(viewLifecycleOwner) { // 기록된 매치 없을 때
            dismissProgress()
            val noElementDialog = CustomDialogFragment(
                getString(R.string.no_match),
                getString(R.string.confirm),
                onClickButton = {
                    CustomDialogFragment(
                        "한 시간당 45명의 전적이 갱신됩니다.",
                        getString(R.string.confirm),
                        onClickButton = {
                            (activity as HomeActivity).backToMainTab()
                        }
                    ).show(childFragmentManager, "index_dialog")
                }
            )

            if (!noElementDialog.isVisible) {
                noElementDialog.show(childFragmentManager, "no_element_dialog")
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