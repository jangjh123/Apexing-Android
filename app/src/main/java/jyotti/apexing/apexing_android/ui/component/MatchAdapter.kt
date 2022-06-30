package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.Typeface
import android.transition.TransitionManager
import android.transition.TransitionManager.beginDelayedTransition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.statistics.MatchModelType
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.RefreshIndex
import jyotti.apexing.apexing_android.databinding.ItemMatchBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsFooterBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding
import jyotti.apexing.apexing_android.ui.activity.home.HomeActivity
import jyotti.apexing.apexing_android.util.GenericDiffUtil
import jyotti.apexing.apexing_android.util.Utils.formatAmount
import jyotti.apexing.apexing_android.util.Utils.getThumbnail
import jyotti.apexing.apexing_android.util.Utils.getTimestampToDate
import java.util.*


class MatchAdapter(
    private inline val onClickRecordingDesc: () -> Unit
) :
    PagingDataAdapter<MatchModels, RecyclerView.ViewHolder>(GenericDiffUtil()) {
    private val _curIndex = MutableLiveData<Int>()
    val curIndex: LiveData<Int>
        get() = _curIndex
    private val _curSize = MutableLiveData<Int>()
    val curSize: LiveData<Int>
        get() = _curSize
    private val _myIndex = MutableLiveData<Int>()
    val myIndex: LiveData<Int>
        get() = _myIndex

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                (holder as MatchViewHolder).bind(getItem(position) as MatchModels.Match)
            }
            1 -> {
                (holder as HeaderViewHolder).bind(getItem(position) as MatchModels.Header)
            }
            2 -> {
                (holder as FooterViewHolder).bind(getItem(position) as MatchModels.Footer)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)!!.type) {
            MatchModelType.DATA -> {
                0
            }
            MatchModelType.HEADER -> {
                1
            }
            MatchModelType.FOOTER -> {
                2
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                MatchViewHolder(
                    ItemMatchBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            1 -> {
                HeaderViewHolder(
                    ItemStatisticsHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                FooterViewHolder(
                    ItemStatisticsFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    fun setRefreshIndex(refreshIndex: RefreshIndex) {
        _curIndex.postValue(refreshIndex.curIndex)
        _curSize.postValue(refreshIndex.curSize)
        _myIndex.postValue(refreshIndex.myIndex)
    }

    inner class MatchViewHolder(
        private val binding: ItemMatchBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Match) {
            with(binding) {
                val context = root.context
                val imageName = item.legendPlayed.lowercase(Locale.getDefault())
                val imageId: Int = when (item.legendPlayed) {

                    "MadMaggie" -> {
                        root.resources.getIdentifier(
                            "mad_maggie",
                            "drawable",
                            "jyotti.apexing.apexing_android"
                        )
                    }
                    else -> {
                        root.resources.getIdentifier(
                            imageName,
                            "drawable",
                            "jyotti.apexing.apexing_android"
                        )
                    }
                }

                Glide.with(root)
                    .load(imageId)
                    .thumbnail(getThumbnail(context, imageId))
                    .into(ivLegend)

                tvDate.text =
                    getTimestampToDate(item.gameStartTimestamp.toString())

                val minute = item.gameLengthSecs.div(60)
                val second = item.gameLengthSecs.rem(60)
                val length = "$minute 분 $second 초 생존"

                tvSurvivalDuration.text = length

                when (item.gameMode) {
                    "BATTLE_ROYALE" -> {
                        tvMode.text = root.context.getString(R.string.battle_royal)
                    }
                    "ARENAS" -> {
                        tvMode.text = root.context.getString(R.string.arena)
                    }
                    else -> {
                        tvMode.text = item.gameMode
                    }
                }

                tvKill.text = item.kill.toString()

                tvDamage.text = formatAmount(item.damage)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemStatisticsHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Header) {
            with(binding) {
                itemView.doOnAttach {
                    val mLifecycleOwner = itemView.findViewTreeLifecycleOwner()
                    if (mLifecycleOwner != null) {
                        myIndex.observe(mLifecycleOwner) {
                            tvMyIndex.text = it.toString()
                        }
                        curIndex.observe(mLifecycleOwner) {
                            tvCurIndex.text = it.toString()
                        }
                        curSize.observe(mLifecycleOwner) {
                            tvSize.text = it.toString()
                        }
                    }
                }

//                PieChart
                chartPie.apply {
                    setTouchEnabled(false)

                    data = item.pieData
                    data.setValueTextColor(Color.TRANSPARENT)
                    data.setValueTextSize(15f)

                    data.dataSet.colors.apply {
                        add(
                            0,
                            ContextCompat.getColor(
                                context,
                                R.color.most0
                            )
                        )
                        add(1, ContextCompat.getColor(context, R.color.most1))
                        add(2, ContextCompat.getColor(context, R.color.most2))
                        add(3, ContextCompat.getColor(context, R.color.most3))
                        add(4, ContextCompat.getColor(context, R.color.most4))
                    }


                    setCenterTextSize(15f)
                    setCenterTextColor(Color.BLACK)
                    setUsePercentValues(true)
                    setDrawEntryLabels(false)
                    description.isEnabled = false
                    legend.isEnabled = false
                    isDrawHoleEnabled = true
                    setHoleColor(Color.TRANSPARENT)

                    animateY(750, Easing.EaseInOutQuad)
                    setTransparentCircleAlpha(50)
                    setTransparentCircleColor(Color.WHITE)
                    holeRadius = 60F
                    requestLayout()
                }

                chartPie.also {
                    val pieData = it.data
                    tvPie0.text = pieData.dataSets[0].getEntryForIndex(0).label
                    tvPie1.text = pieData.dataSets[0].getEntryForIndex(1).label
                    tvPie2.text = pieData.dataSets[0].getEntryForIndex(2).label
                    tvPie3.text = pieData.dataSets[0].getEntryForIndex(3).label
                    tvPie4.text = pieData.dataSets[0].getEntryForIndex(4).label

                    tvPieValue0.text = getPercentage(
                        pieData.yValueSum.toInt(),
                        pieData.dataSets[0].getEntryForIndex(0).value
                    )
                    tvPieValue1.text = getPercentage(
                        pieData.yValueSum.toInt(),
                        pieData.dataSets[0].getEntryForIndex(1).value
                    )
                    tvPieValue2.text = getPercentage(
                        pieData.yValueSum.toInt(),
                        pieData.dataSets[0].getEntryForIndex(2).value
                    )
                    tvPieValue3.text = getPercentage(
                        pieData.yValueSum.toInt(),
                        pieData.dataSets[0].getEntryForIndex(3).value
                    )
                    tvPieValue4.text = getPercentage(
                        pieData.yValueSum.toInt(),
                        pieData.dataSets[0].getEntryForIndex(4).value
                    )

                    val circleImageName0 =
                        pieData.dataSets[0].getEntryForIndex(0).label.lowercase(Locale.getDefault())
                    val circleImageName1 =
                        pieData.dataSets[0].getEntryForIndex(1).label.lowercase(Locale.getDefault())
                    val circleImageName2 =
                        pieData.dataSets[0].getEntryForIndex(2).label.lowercase(Locale.getDefault())
                    val circleImageName3 =
                        pieData.dataSets[0].getEntryForIndex(3).label.lowercase(Locale.getDefault())
                    val circleImageName4 =
                        pieData.dataSets[0].getEntryForIndex(4).label.lowercase(Locale.getDefault())

                    Glide.with(root.context)
                        .load(
                            root.resources.getIdentifier(
                                circleImageName0,
                                "drawable",
                                "jyotti.apexing.apexing_android"
                            )
                        )
                        .into(civMost0)

                    Glide.with(root.context)
                        .load(
                            root.resources.getIdentifier(
                                circleImageName1,
                                "drawable",
                                "jyotti.apexing.apexing_android"
                            )
                        )
                        .into(civMost1)

                    Glide.with(root.context)
                        .load(
                            root.resources.getIdentifier(
                                circleImageName2,
                                "drawable",
                                "jyotti.apexing.apexing_android"
                            )
                        )
                        .into(civMost2)

                    Glide.with(root.context)
                        .load(
                            root.resources.getIdentifier(
                                circleImageName3,
                                "drawable",
                                "jyotti.apexing.apexing_android"
                            )
                        )
                        .into(civMost3)

                    Glide.with(root.context)
                        .load(
                            root.resources.getIdentifier(
                                circleImageName4,
                                "drawable",
                                "jyotti.apexing.apexing_android"
                            )
                        )
                        .into(civMost4)
                }

//                Basic Statistics
                tvKillAvgAll.text = String.format("%.2f", item.killRvgAll)
                tvDamageAvgAll.text = String.format("%.2f", item.damageRvgAll)
                tvKillAvgRecent.text = String.format("%.2f", item.killRvgRecent)
                tvDamageAvgRecent.text = String.format("%.2f", item.damageRvgRecent)

                "기록된 ${item.matchCount} 매치 기준".run {
                    tvAllGame0.text = this
                    tvAllGame1.text = this
                }

//                RadarChart
                chartRadar.apply {
                    setTouchEnabled(false)
                    setDrawMarkers(false)

                    val radarData = RadarData(item.radarDataSet.apply {
                        color = ContextCompat.getColor(
                            context,
                            R.color.main
                        )
                        lineWidth = 2f
                        setDrawFilled(true)
                        fillColor = ContextCompat.getColor(
                            context,
                            R.color.main
                        )
                    })

                    data = radarData.apply {
                        setValueTextSize(15f)
                        setValueTextColor(ContextCompat.getColor(context, R.color.light_gray))
                    }

                    val valueList = ArrayList<String>().apply {
                        add(0, "킬 캐치")
                        add(1, "딜링")
                        add(2, "생존 능력")
                        add(3, "적극성")
                    }

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(valueList)
                        textColor = ContextCompat.getColor(context, R.color.white)
                        textSize = 15f
                    }

                    yAxis.apply {
                        setDrawLabels(false)
                        axisMinimum = 0f
                        axisMaximum = 100f
                        typeface = Typeface.DEFAULT_BOLD
                        setLabelCount(10, true)
                    }

                    legend.isEnabled = false
                    description.isEnabled = false
                    webLineWidthInner = 0f
                    webLineWidth = 0f
                    webColor = ContextCompat.getColor(context, R.color.main)
                    webColorInner = ContextCompat.getColor(context, R.color.divider)
                    animateXY(1000, 1000, Easing.EaseInOutQuad)
                    requestLayout()
                }

                btnHowToRecord.setOnClickListener {
                    onClickRecordingDesc()
                }
            }
        }
    }

    private fun getPercentage(ySum: Int, yValue: Float) =
        String.format("%.1f", (yValue / ySum) * 100) + "%"

    inner class FooterViewHolder(private val binding: ItemStatisticsFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Footer) {
            binding.tvFooter.text = item.text
        }
    }
}


