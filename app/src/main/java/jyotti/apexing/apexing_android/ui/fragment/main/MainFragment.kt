package jyotti.apexing.apexing_android.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.base.BaseFragment
import jyotti.apexing.apexing_android.data.model.main.crafting.Crafting
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.data.model.main.user.User
import jyotti.apexing.apexing_android.databinding.FragmentMainBinding
import jyotti.apexing.apexing_android.ui.activity.account.AccountActivity
import jyotti.apexing.apexing_android.ui.component.MapAdapter
import jyotti.apexing.apexing_android.ui.component.NewsAdapter
import jyotti.apexing.apexing_android.util.Utils.getThumbnailWithCenterCrop
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModels()
    val mapAdapter = MapAdapter()
    val newsAdapter = NewsAdapter(onClickNews = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        startActivity(intent)
    })

    override fun onStart() {
        super.onStart()
        binding.fragment = this@MainFragment
    }

    override fun startProcess() {
        showUser()
        showMap()
        showCrafting()
        showNews()
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

    @SuppressLint("SetTextI18n")
    private fun setUserView(user: User) {
        with(binding) {
            if (user.global.name.isNotEmpty()) {
                tvUserId.text = user.global.name
            } else {
                tvUserId.text = getString(R.string.korean_nickname)
            }
            tvBrRankPoint.text = user.global.rank.rankScore.toString()
            tvArenaRankPoint.text = user.global.arena.rankScore.toString()

            if (user.global.level <= 500) {
                tvUserLevel.text = "Lv.${user.global.level}"
                tvCurLevel.text = "Lv.${user.global.level}"
                tvNextLevel.text = "Lv.${user.global.level + 1}"
            } else {
                tvUserLevel.text = 500.toString()
            }

            try {
                tvRecordDeal.text = "${user.total.damage.value}"
                tvRecordKill.text = "${user.total.kills.value}"
                tvRecordKd.text = "${user.total.kd.value}"
                tvRecordPlayedGames.text = "${user.total.gamesPlayed?.value}"
            } catch (exception: Exception) {

            }

            Glide.with(requireContext())
                .load(user.legends.selected.imageAsset.banner)
                .centerCrop()
                .into(binding.ivBanner)

            pbLevel.progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.main
                    ), BlendModeCompat.SRC_ATOP
                )

            pbLevel.progress = user.global.toNextLevelPercent
        }

        Glide.with(requireContext())
            .load(user.global.rank.rankImg)
            .into(binding.ivBrRank)

        Glide.with(requireContext())
            .load(user.global.arena.rankImg)
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

        with (binding) {
            tvCostDaily0.text = craftingList[0].cost
            tvCostDaily1.text = craftingList[1].cost
            tvCostWeekly0.text = craftingList[2].cost
            tvCostWeekly1.text = craftingList[3].cost
        }
    }

    private fun setNewsView(newsList: List<News>) {
        newsAdapter.submitList(newsList)
        if (binding.rvNews.onFlingListener == null) {
            LinearSnapHelper().run {
                attachToRecyclerView(binding.rvNews)
                binding.indicator.attachToRecyclerView(binding.rvNews, this)
            }
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