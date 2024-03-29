package jyotti.apexing.apexing_android.ui.activity.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivityAccountBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.GoToHelpPage
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.GoToHome
import jyotti.apexing.apexing_android.ui.activity.account.AccountUiContract.UiEffect.ShowSnackBar
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class AccountActivity : BaseActivity<ActivityAccountBinding>(ActivityAccountBinding::inflate) {
    override val viewModel: AccountViewModel by viewModels()

    override fun initBinding() {
        bind {
            vm = viewModel
        }
    }

    override fun collectUiState() {
        repeatCallDefaultOnStarted {
            viewModel.uiState.collect { uiState ->
                setLoadingDialogVisibility(uiState.isLoading)
            }
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collect { uiEffect ->
                when (uiEffect) {
                    is GoToHelpPage -> goToHelpPage()

                    is ShowSnackBar -> showSnackBar(getString(uiEffect.stringId))

                    is GoToHome -> goToHome(uiEffect.id)
                }
            }
        }
    }

    private fun goToHelpPage() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_help))))
    }

    private fun goToHome(id: String) {
        startActivity(
            HomeActivity.newIntent(
                context = this@AccountActivity,
                id = id
            )
        )
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, AccountActivity::class.java)
    }
}