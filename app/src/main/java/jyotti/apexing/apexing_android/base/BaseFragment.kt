package jyotti.apexing.apexing_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB : ViewDataBinding>(private val inflater: (LayoutInflater) -> VB) : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected abstract val viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflater(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    protected fun setLoadingDialogVisibility(visible: Boolean) {
        (requireActivity() as BaseActivity<*>).setLoadingDialogVisibility(visible)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}