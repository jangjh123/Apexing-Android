package jyotti.apexing.apexing_android.ui.activity.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseActivityV2
import jyotti.apexing.apexing_android.databinding.ActivityAccountBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.GoToHelpPage
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.ShowSnackBar
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountActivity : BaseActivityV2<ActivityAccountBinding>(ActivityAccountBinding::inflate) {
    override val viewModel: AccountViewModel by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collectLatest { uiEffect ->
                when (uiEffect) {
                    is GoToHelpPage -> {
                        goToHelpPage()
                    }

                    is ShowSnackBar -> {
                        this@AccountActivity.showSnackBar(getString(uiEffect.stringId))
                    }
                }
            }
        }
    }

    private fun goToHelpPage() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_help))))
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AccountActivity::class.java)
    }
}