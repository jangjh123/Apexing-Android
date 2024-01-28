package jyotti.apexing.apexing_android.ui.fragment.store

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.databinding.FragmentStoreBinding
import jyotti.apexing.apexing_android.ui.component.ItemDetailFragment
import jyotti.apexing.apexing_android.ui.component.StoreAdapter

@AndroidEntryPoint
class StoreFragment : BaseFragment<FragmentStoreBinding>(R.layout.fragment_store) {
    private val viewModel: StoreViewModel by viewModels()
    val storeAdapter = StoreAdapter(
        onClickItem = {
            ItemDetailFragment(it.first, it.second).show(childFragmentManager, "item_detail_dialog")
        }
    )

    override fun onStart() {
        super.onStart()
        binding.fragment = this@StoreFragment
    }

    override fun setObservers() {
        viewModel.getStoreLiveData().observe(viewLifecycleOwner) {
            storeAdapter.submitList(it)
            dismissProgress()
        }
    }
}