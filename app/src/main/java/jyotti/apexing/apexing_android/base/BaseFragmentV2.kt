package jyotti.apexing.apexing_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragmentV2<VB : ViewDataBinding>(private val inflater: (LayoutInflater) -> VB) : Fragment() {
    lateinit var binding: VB

    protected abstract val viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflater(layoutInflater)
        return binding.root
    }
}