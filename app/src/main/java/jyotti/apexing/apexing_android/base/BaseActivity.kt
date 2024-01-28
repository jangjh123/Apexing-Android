package jyotti.apexing.apexing_android.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<VB : ViewDataBinding>(private val inflater: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    lateinit var binding: VB

    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        setContentView(binding.root)
        initBinding()
    }

    override fun onStart() {
        super.onStart()
        collectUiEffect()
    }

    protected abstract fun initBinding()

    protected abstract fun collectUiEffect()

    protected fun bind(action: VB.() -> Unit) {
        binding.run(action)
    }

    protected fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}