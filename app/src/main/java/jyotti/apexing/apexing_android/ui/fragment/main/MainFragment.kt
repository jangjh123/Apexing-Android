package jyotti.apexing.apexing_android.ui.fragment.main

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.databinding.FragmentMainBinding
import jyotti.apexing.apexing_android.ui.component.MapAdapter
import jyotti.apexing.apexing_android.ui.component.NewsAdapter
import jyotti.apexing.apexing_android.util.repeatCallDefaultOnStarted

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()
    private val mapAdapter = MapAdapter()
    private val newsAdapter = NewsAdapter(
        onClickNews = { newsUrl ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl)))
        }
    )

    override fun initBinding() {
        bind {
            vm = viewModel
            rvMap.adapter = mapAdapter
            rvNews.adapter = newsAdapter
        }
    }

    override fun collectUiState() {
        repeatCallDefaultOnStarted {
            viewModel.uiState.collect { uiState ->
                setLoadingDialogVisibility(uiState.isLoading)
            }
        }
    }

    override fun collectUiEffect() = Unit
}