package jyotti.apexing.apexing_android.ui.activity.account

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivityAccountBinding
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class AccountActivityV2 : BaseActivityV2<ActivityAccountBinding>(ActivityAccountBinding::inflate) {
    override val viewModel: AccountViewModelV2 by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {

        }
    }
}