package jyotti.apexing.apexing_android.ui.fragment.main

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragmentV2
import jyotti.apexing.apexing_android.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragmentV2 : BaseFragmentV2<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModelV2 by viewModels()
}