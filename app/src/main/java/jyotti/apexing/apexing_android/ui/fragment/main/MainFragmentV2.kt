package jyotti.apexing.apexing_android.ui.fragment.main

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragmentV2
import jyotti.apexing.apexing_android.databinding.FragmentMainBinding
import jyotti.apexing.apexing_android.ui.component.MapAdapter
import jyotti.apexing.apexing_android.ui.component.NewsAdapter
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class MainFragmentV2 : BaseFragmentV2<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModelV2 by viewModels()
    private val mapAdapter = MapAdapter()
    private val newsAdapter = NewsAdapter(
        onClickNews = {

        }
    )

    override fun initBinding() {
        bind {
            vm = viewModel
            rvMap.adapter = mapAdapter
            rvNews.adapter = newsAdapter
        }
    }

    override fun collectUiEffect() {
        repeatCallDefaultOnStarted {
            viewModel.uiEffect.collect { uiEffect ->
                when (uiEffect) {
                    else -> Unit
                }
            }
        }
    }
}