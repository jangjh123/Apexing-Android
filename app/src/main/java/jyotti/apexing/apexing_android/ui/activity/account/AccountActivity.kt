package jyotti.apexing.apexing_android.ui.activity.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseActivity
import jyotti.apexing.apexing_android.databinding.ActivityAccountBinding
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity

@AndroidEntryPoint
class AccountActivity : BaseActivity<ActivityAccountBinding>(R.layout.activity_account) {
    private val viewModel: AccountViewModel by viewModels()

    override fun onStart() {
        super.onStart()
    }

    override fun startProcess() {
        showView()
        initKeyboard(binding.etId)
    }

    private fun showView() {
        val fadeIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in1)
        val fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2)
        val fadeIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in3)
    }

    private fun initKeyboard(view: View) {
        view.setOnKeyListener { _, p1, _ ->
            if (p1 == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
            return@setOnKeyListener false
        }
    }

    fun onClickHelp(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.web_help)))
        startActivity(intent)
    }

    fun onClickEnroll(view: View) {
//        val platform = when {
//            binding.rbPc.isChecked -> {
//                getString(R.string.pc)
//            }
//
//            binding.rbPs.isChecked -> {
//                getString(R.string.ps4)
//            }
//
//            binding.rbXbox.isChecked -> {
//                getString(R.string.xbox)
//            }
//
//            else -> {
//                ""
//            }
//        }
//
//        val id = binding.etId.text.toString()
//
//        if (platform.isEmpty() && id.isNotEmpty()) {
//            showSnackBar(getString(R.string.please_set_platform))
//        } else if (platform.isNotEmpty() && id.isEmpty()) {
//            showSnackBar(getString(R.string.please_set_id))
//        } else if (platform.isEmpty() && id.isEmpty()) {
//            showSnackBar(getString(R.string.please_set_all))
//        } else {
//            if (id.contains('.') || id.contains('#') || id.contains('[') || id.contains(']')) {
//                showSnackBar(getString(R.string.not_allowed_id))
//            } else {
//                showProgress()
//                viewModel.checkAccount(id, platform)
//            }
//        }
    }

    override fun setObservers() {
        viewModel.getMessageLiveData().observe(this) {
            dismissProgress()
            when (it) {
                AccountMessage.Success -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                AccountMessage.Null -> {
                    showSnackBar(getString(R.string.account_error))
                }

                AccountMessage.Error -> {
                    showSnackBar(getString(R.string.please_retry))
                }

                AccountMessage.NetworkError -> {
                    showSnackBar(getString(R.string.server_error))
                }
            }
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}