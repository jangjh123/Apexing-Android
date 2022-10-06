package jyotti.apexing.apexing_android.ui.activity.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivitySplashBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.ui.component.CustomDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private lateinit var customDialog: CustomDialogFragment
    private val viewModel: SplashViewModel by viewModels()

    override fun setObservers() {
        viewModel.nextScreenInfo.observe(this) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                when (it) {
                    "newVersion" -> {
                        showNewVersionDialog()
                    }
                    "home" -> {
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    }
                    "account" -> {
                        startActivity(Intent(this@SplashActivity, AccountActivity::class.java))
                    }
                    "error" -> {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun showNewVersionDialog() {
        customDialog = CustomDialogFragment(
            getString(R.string.new_version),
            getString(R.string.play_store),
            onClickButton = {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.app_url))
                    )
                )
            }).also { dialog ->
            dialog.show(supportFragmentManager, "new_version_dialog")
        }
    }

    private fun showErrorDialog() {
        customDialog = CustomDialogFragment(
            getString(R.string.not_connected_to_internet),
            getString(R.string.quit),
            onClickButton = {
                finish()
            }
        ).also { dialog ->
            dialog.show(supportFragmentManager, "server_error_dialog")
        }
    }

    override fun onBackPressed() {

    }
}