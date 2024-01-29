package jyotti.apexing.apexing_android.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivityHomeBinding
import jyotti.apexing.apexing_android.ui.activity.home.ApexingFragment.MAIN
import jyotti.apexing.apexing_android.ui.activity.home.ApexingFragment.STATISTICS
import jyotti.apexing.apexing_android.ui.fragment.main.MainFragment
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsFragment
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override val viewModel: HomeViewModel by viewModels()
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
        val fragmentClass = when (apexingFragment) {
            MAIN -> MainFragment::class.java
            STATISTICS -> StatisticsFragment::class.java
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.layoutFrame.id, fragmentClass, bundleOf(Pair(KEY_ID, intent.getStringExtra(KEY_ID))))
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