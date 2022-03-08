package jyotti.apexing.apexing_android.ui.activity.account

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.apexing.apexing_android.R
import com.apexing.apexing_android.databinding.ActivityAccountBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity

@AndroidEntryPoint
class AccountActivity : BaseActivity<ActivityAccountBinding>(R.layout.activity_account) {
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var platform: String
    private lateinit var id: String

    override fun onStart() {
        super.onStart()
        binding.activity = this@AccountActivity
    }

    override fun startProcess() {
        showView()
    }

    private fun showView() {
        val fadeIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in1)
        val fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2)
        val fadeIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in3)

        binding.tvTitle.animation = fadeIn1
        binding.etWrapper.animation = fadeIn2
        binding.spinnerPlatform.animation = fadeIn2
        binding.tvHelp.animation = fadeIn2
        binding.btnEnroll.animation = fadeIn3
    }

    fun onClickHelp(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.account_help_web)))
        startActivity(intent)
    }

    fun onClickEnroll(view: View) {
        platform = binding.spinnerPlatform.text.toString()
        id = binding.etId.text.toString()

        if (platform.isEmpty() && id.isNotEmpty()) {
            showSnackBar(getString(R.string.please_set_platform))
        } else if (platform.isNotEmpty() && id.isEmpty()) {
            showSnackBar(getString(R.string.please_set_id))
        } else if (platform.isEmpty() && id.isEmpty()) {
            showSnackBar(getString(R.string.please_set_all))
        } else {
            showProgress()
            checkAccount()

        }
    }

    private fun checkAccount() {
        viewModel.checkAccount(
            binding.spinnerPlatform.text.toString(),
            binding.etId.text.toString()
        )
    }

    override fun setObserver() {
        viewModel.getMessageLiveData().observe(this) {
            dismissProgress()
            when (it) {
                0 -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    showSnackBar(getString(R.string.account_error))
                }
                2 -> {
                    showSnackBar(getString(R.string.please_retry))
                }
                else -> {
                    showSnackBar(getString(R.string.network_error))
                }
            }
        }
    }

    private fun showSnackBar(text: String) =
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()

}