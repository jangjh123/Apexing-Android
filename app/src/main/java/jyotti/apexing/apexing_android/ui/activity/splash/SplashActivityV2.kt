package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivityV2 : BaseActivityV2<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override val viewModel: SplashViewModelV2 by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }
}