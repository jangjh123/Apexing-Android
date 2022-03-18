package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.ui.component.CustomDialogFragment

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private lateinit var customDialog: CustomDialogFragment
    private val viewModel: SplashViewModel by viewModels()

    override fun startProcess() {
        checkVersion()
    }

    private fun checkAccount() {
        viewModel.getStoredPlatform()
    }

    private fun checkVersion() {
        viewModel.getNewVersionCode()
    }

    override fun setObservers() {
        viewModel.getVersionLiveData().observe(this) {
            when (it) {
                true -> {
                    customDialog = CustomDialogFragment(
                        getString(R.string.new_version),
                        getString(R.string.play_store),
                        onClickButton = {
                            // go to playStore
                        }).also { dialog ->
                        dialog.show(supportFragmentManager, "new_version_dialog")
                    }
                }
                false -> {
                    checkAccount()
                }
            }
        }

        viewModel.getNetworkMessage().observe(this) {
            customDialog = CustomDialogFragment(
                getString(R.string.not_connected_to_internet),
                getString(R.string.quit),
                onClickButton = {
                    finish()
                }
            ).also { dialog ->
                dialog.show(supportFragmentManager, "network_error_dialog")
            }
        }

        viewModel.getPlatformLiveData().observe(this) {
            val intent: Intent = if (it.isEmpty()) {
                Intent(this, AccountActivity::class.java)
            } else {
                Intent(this, HomeActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}