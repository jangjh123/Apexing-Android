package jyotti.apexing.apexing_android.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.UserInfo
import jyotti.apexing.apexing_android.databinding.FragmentMainBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.component.MapAdapter
import jyotti.apexing.apexing_android.ui.component.NewsAdapter
import jyotti.apexing.apexing_android.util.Utils.formatAmount
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModels()
    private val linearSnapHelper = LinearSnapHelper()

    val mapAdapter = MapAdapter()
    val newsAdapter = NewsAdapter(onClickNews = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        startActivity(intent)
    })

    override fun onStart() {
        super.onStart()
//        binding.fragment = this@MainFragment
    }

    override fun startProcess() {
        showUser()
        showMap()
        showCrafting()
        showNews()
        showNotice()
    }

    override fun setObservers() {
        viewModel.getUserLiveData().observe(viewLifecycleOwner) {
            setUserView(it)
            dismissProgress()
        }

        viewModel.getMapLiveData().observe(viewLifecycleOwner) {
            setMapView(it)
        }

        viewModel.getCraftingLiveData().observe(viewLifecycleOwner) {
            setCraftingView(it)
        }

        viewModel.getNewsLiveData().observe(viewLifecycleOwner) {
            setNewsView(it)
        }

        viewModel.getNoticeLiveData().observe(viewLifecycleOwner) {
            setNoticeView(it)
        }
    }

    private fun showUser() {
        viewModel.getUser()
    }

    private fun showMap() {
        viewModel.getMap()
    }

    private fun showCrafting() {
        viewModel.getCrafting()
    }

    private fun showNews() {
        viewModel.getNews()
    }

    private fun showNotice() {
        viewModel.getNotice()
    }

    @SuppressLint("SetTextI18n")
    private fun setUserView(userInfo: UserInfo) {
        with(binding) {
            if (userInfo.name.isNotEmpty()) {
                tvUserId.text = userInfo.name
            } else {
                tvUserId.text = getString(R.string.korean_nickname)
            }
            tvBrRankPoint.text = formatAmount(userInfo.brRankScore)
            tvArenaRankPoint.text = formatAmount(userInfo.arRankScore)

            if (userInfo.level <= 500) {
                tvUserLevel.text = "Lv.${userInfo.level}"
                tvCurLevel.text = "Lv.${userInfo.level}"
                tvNextLevel.text = "Lv.${userInfo.level + 1}"
            } else {
                tvUserLevel.text = 500.toString()
            }

            Glide.with(requireContext())
                .load(userInfo.bannerImg.replace("\"", ""))
                .centerCrop()
                .into(binding.ivBanner)

            pbLevel.progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main
                    ), BlendModeCompat.SRC_ATOP
                )

            pbLevel.progress = userInfo.toNextLevelPercent
        }

        Glide.with(requireContext())
            .load(userInfo.brRankImg)
            .into(binding.ivBrRank)

        Glide.with(requireContext())
            .load(userInfo.arRankImg)
            .into(binding.ivArenaRank)
    }

    private fun setMapView(mapList: List<Map>) {
        mapAdapter.submitList(mapList)
    }

    private fun setCraftingView(craftingList: List<Crafting>) {
        Glide.with(requireContext())
            .load(craftingList[0].asset)
            .centerCrop()
            .into(binding.ivCraftDaily0)
        Glide.with(requireContext())
            .load(craftingList[1].asset)
            .centerCrop()
            .into(binding.ivCraftDaily1)
        Glide.with(requireContext())
            .load(craftingList[2].asset)
            .centerCrop()
            .into(binding.ivCraftWeekly0)
        Glide.with(requireContext())
            .load(craftingList[3].asset)
            .centerCrop()
            .into(binding.ivCraftWeekly1)

        with(binding) {
            tvCostDaily0.text = craftingList[0].cost
            tvCostDaily1.text = craftingList[1].cost
            tvCostWeekly0.text = craftingList[2].cost
            tvCostWeekly1.text = craftingList[3].cost
        }
    }

    private fun setNewsView(newsList: List<News>) {
        newsAdapter.submitList(newsList)
        linearSnapHelper.run {
            attachToRecyclerView(binding.rvNews)
        }
    }

    private fun setNoticeView(notice: String) {
        if (notice.length == 1) {
            binding.cvNotice.visibility = View.GONE
        } else {
            binding.tvNotice.text = notice.replace("_n", "\n")
        }
    }

    fun onClickChangeAccount(view: View) {
        viewModel.removeAccount {
            finishAffinity(requireActivity())
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
            exitProcess(0)
        }
    }
}