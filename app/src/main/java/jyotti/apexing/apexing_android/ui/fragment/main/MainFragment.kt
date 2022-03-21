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
import jyotti.apexing.apexing_android.util.Utils.getThumbnail
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
        viewModel.setTimeOut()
    }

    override fun setObservers() {
        viewModel.getTimeOutMessage().observe(viewLifecycleOwner) {
            if (isProgressShowing()) {
                dismissProgress()
                setOnFailureView(failureView = binding.layoutNull, successView = binding.layoutView)
            }
        }

        viewModel.getUserLiveData().observe(viewLifecycleOwner) {
            setUserView(it)
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

        viewModel.getContentsCount().observe(viewLifecycleOwner) {
            if (it > 1) {
                setOnSuccessView(
                    successView = binding.layoutView,
                    failureView = binding.layoutNull
                )
                dismissProgress()
            }
        }

        viewModel.getNetworkMessage().observe(viewLifecycleOwner) {
            setOnFailureView(
                failureView = binding.layoutNull,
                successView = binding.layoutView
            )
            dismissProgress()
        }
    }

    fun onClickRetry(view: View) {
        showProgress()
        startProcess()
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
        try {
            with(binding) {
                if (user.global.name.isNotEmpty()) {
                    tvName.text = user.global.name
                } else {
                    tvName.text = getString(R.string.korean_nickname)
                }
                tvBrPoint.text = user.global.rank.rankScore.toString()
                tvArPoint.text = user.global.arena.rankScore.toString()

                if (user.global.level <= 500) {
                    tvLevel.text = "Lv. ${user.global.level}"
                } else {
                    tvLevel.text = 500.toString()
                }

                tvRecordValue0.text = user.total.damage.value.toString()
                tvRecordValue1.text = user.total.kills.value.toString()
                tvRecordValue2.text = user.total.kd.value.toString()
                tvRecordValue3.text = user.total.gamesPlayed?.value.toString()

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
                .load(user.legends.selected.imageAsset.banner)
                .centerCrop()
                .thumbnail(
                    getThumbnailWithCenterCrop(requireContext(), user.legends.selected.imageAsset.banner)
                )
                .listener(imageLoadingListener())
                .into(binding.ivBanner)

            Glide.with(requireContext())
                .load(user.global.rank.rankImg)
                .thumbnail(
                    getThumbnail(requireContext(), user.global.rank.rankImg)
                )
                .listener(imageLoadingListener())
                .into(binding.ivBrRank)

            Glide.with(requireContext())
                .load(user.global.arena.rankImg)
                .thumbnail(
                    getThumbnail(requireContext(), user.global.arena.rankImg)
                )
                .listener(imageLoadingListener())
                .into(binding.ivArRank)
        } catch (exception: Exception) {
            exception.printStackTrace()
            dismissProgress()
        }
    }

    private fun setMapView(mapList: List<Map>) {
        mapList[0].type = getString(R.string.battle_royal)
        mapList[1].type = "${getString(R.string.rank)} ${getString(R.string.battle_royal)}"
        mapList[2].type = getString(R.string.arena)
        mapList[3].type = "${getString(R.string.rank)} ${getString(R.string.arena)}"
        mapAdapter.submitList(mapList)
    }

    private fun setCraftingView(craftingList: List<Crafting>) {
        Glide.with(requireContext())
            .load(craftingList[0].bundleContents[0].itemType.asset)
            .centerCrop()
            .thumbnail(
                getThumbnailWithCenterCrop(requireContext(),
                craftingList[0].bundleContents[0].itemType.asset)
            )
            .listener(imageLoadingListener())
            .into(binding.ivCraft0)
        Glide.with(requireContext())
            .load(craftingList[0].bundleContents[1].itemType.asset)
            .centerCrop()
            .thumbnail(
                getThumbnailWithCenterCrop(requireContext(),
                    craftingList[0].bundleContents[1].itemType.asset)
            )
            .listener(imageLoadingListener())
            .into(binding.ivCraft1)
        Glide.with(requireContext())
            .load(craftingList[1].bundleContents[0].itemType.asset)
            .centerCrop()
            .thumbnail(
                getThumbnailWithCenterCrop(requireContext(),
                    craftingList[1].bundleContents[0].itemType.asset)
            )
            .listener(imageLoadingListener())
            .into(binding.ivCraft2)
        Glide.with(requireContext())
            .load(craftingList[1].bundleContents[1].itemType.asset)
            .centerCrop()
            .thumbnail(
                getThumbnailWithCenterCrop(requireContext(),
                    craftingList[1].bundleContents[1].itemType.asset)
            )
            .listener(imageLoadingListener())
            .into(binding.ivCraft3)

        with(binding) {
            tvCraftingCost0.text = craftingList[0].bundleContents[0].cost.toString()
            tvCraftingCost1.text = craftingList[0].bundleContents[1].cost.toString()
            tvCraftingCost2.text = craftingList[1].bundleContents[0].cost.toString()
            tvCraftingCost3.text = craftingList[1].bundleContents[1].cost.toString()
        }
    }

    private fun setNewsView(newsList: List<News>) {
        newsAdapter.submitList(newsList)
    }

    fun onClickChangeAccount(view: View) {
        viewModel.removeAccount {
            finishAffinity(requireActivity())
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
            exitProcess(0)
        }
    }

    private fun imageLoadingListener(): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                viewModel.addContentsCount()
                return false
            }
        }
    }
}