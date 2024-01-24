package jyotti.apexing.apexing_android.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivityHomeBinding
import jyotti.apexing.apexing_android.ui.activity.home.ApexingFragment.MAIN
import jyotti.apexing.apexing_android.ui.activity.home.ApexingFragment.STATISTICS
import jyotti.apexing.apexing_android.ui.fragment.main.MainFragment
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsFragment
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class HomeActivity : BaseActivityV2<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override val viewModel: HomeViewModel by viewModels()

    private val mainFragment = MainFragment()
    private val statisticsFragment by lazy { StatisticsFragment() }

    override fun initBinding() {
        bind {
            vm = viewModel
        }

        showFragment(MAIN)
        initTabLayout()
    }

    private fun initTabLayout() {
        binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showFragment(MAIN)
                    1 -> showFragment(STATISTICS)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showFragment(apexingFragment: ApexingFragment) {
        val fragment = when (apexingFragment) {
            MAIN -> mainFragment
            STATISTICS -> statisticsFragment
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.layoutFrame.id, MainFragment::class.java, bundleOf(Pair(KEY_ID, intent.getStringExtra(KEY_ID))))
            .addToBackStack(null)
            .commit()
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

    companion object {
        const val KEY_ID = "key_id"

        fun newIntent(
            context: Context,
            id: String
        ): Intent = Intent(context, HomeActivity::class.java).apply {
            putExtra(KEY_ID, id)
        }
    }
}