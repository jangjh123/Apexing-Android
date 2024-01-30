package jyotti.apexing.apexing_android.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import jyotti.apexing.apexing_android.ui.component.LoadingDialog

abstract class BaseActivity<VB : ViewDataBinding>(private val inflater: (LayoutInflater) -> VB) : AppCompatActivity() {
    lateinit var binding: VB
    protected abstract val viewModel: BaseViewModel
    private val loadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        setContentView(binding.root)
        initBinding()
    }

    override fun onStart() {
        super.onStart()
        collectUiState()
        collectUiEffect()
    }

    protected abstract fun initBinding()

    protected abstract fun collectUiState()

    protected abstract fun collectUiEffect()

    protected fun bind(action: VB.() -> Unit) {
        binding.run(action)
    }

    protected fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

     fun setLoadingDialogVisibility(visible: Boolean) {
        if (visible) {
            if (!loadingDialog.isAdded) {
                supportFragmentManager
                    .beginTransaction()
                    .add(loadingDialog, TAG_LOADING_DIALOG)
                    .commit()
            }
        } else {
            if (loadingDialog.isAdded) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(loadingDialog)
                    .commit()
            }
        }
    }

    companion object {
        private const val TAG_LOADING_DIALOG = "tag_loading_dialog"
    }
}