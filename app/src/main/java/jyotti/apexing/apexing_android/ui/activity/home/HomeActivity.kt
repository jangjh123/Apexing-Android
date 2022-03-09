package jyotti.apexing.apexing_android.ui.activity.home

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.apexing.apexing_android.R
import com.apexing.apexing_android.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.ui.fragment.main.MainFragment
import jyotti.apexing.apexing_android.ui.fragment.statistics.StatisticsFragment

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val mainFragment = MainFragment()
    private val statisticsFragment by lazy {
        StatisticsFragment()
    }

    override fun startProcess() {
        initView()
        showView()
    }

    private fun initView() {
        initTabLayout()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.layoutFrame.id, mainFragment)
            .commit()
    }

    private fun showView() {
        val fadeIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in1)
        val fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2)
        val fadeIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in3)

        binding.tvTitle.animation = fadeIn1
        binding.layoutTab.animation = fadeIn2
        binding.layoutFrame.animation = fadeIn3
    }

    private fun initTabLayout() {
        binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        showFragment(0)
                    }
                    else -> {
                        showFragment(1)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun showFragment(fragmentNumber: Int) {
        lateinit var fragment: Fragment

        when (fragmentNumber) {
            0 -> {
                fragment = mainFragment
            }
            1 -> {
                fragment = statisticsFragment
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.layoutFrame.id, fragment)
            .commit()
    }
}