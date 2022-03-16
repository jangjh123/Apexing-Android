package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.statistics.MatchModelType
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.databinding.ItemMatchBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsFooterBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding
import jyotti.apexing.apexing_android.util.GenericDiffUtil
import jyotti.apexing.apexing_android.util.UnixConverter
import java.text.DecimalFormat
import java.util.*
import kotlin.math.absoluteValue


class MatchAdapter(private val onClickRefresh: () -> Unit) :
    PagingDataAdapter<MatchModels, RecyclerView.ViewHolder>(GenericDiffUtil()) {
    private val mUnixConverter = UnixConverter()

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

    inner class MatchViewHolder(
        private val binding: ItemMatchBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Match) {
            with(binding) {
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
                    .into(ivLegend)

                tvDate.text =
                    mUnixConverter.getTimestampToDate(item.gameStartTimestamp.toString())

                val minute = item.gameLengthSecs.div(60)
                val second = item.gameLengthSecs.rem(60)
                val length = "$minute 분 $second 초"

                tvLegend.text = item.legendPlayed
                tvSurvivalDuration.text = length

                when (item.gameMode) {
                    "BATTLE_ROYALE" -> {
                        tvMode.text = "배틀로얄"
                    }
                    "ARENAS" -> {
                        tvMode.text = "아레나"
                    }
                    else -> {
                        tvMode.text = item.gameMode
                    }
                }

                tvKill.text = item.kill.toString()

                val dec = DecimalFormat("#,###")

                tvDamage.text = dec.format(item.damage)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: ItemStatisticsHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Header) {

            item.pieData.setValueFormatter(PercentFormatter())

            with(binding) {

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
                                root.context,
                                R.color.pie0
                            )
                        )
                        add(1, ContextCompat.getColor(root.context, R.color.pie1))
                        add(2, ContextCompat.getColor(root.context, R.color.pie2))
                        add(3, ContextCompat.getColor(root.context, R.color.pie3))
                        add(4, ContextCompat.getColor(root.context, R.color.pie4))
                    }

                    centerText = "Most 5"
                    setCenterTextSize(15f)
                    setCenterTextColor(Color.BLACK)

                    setUsePercentValues(true)
                    setDrawEntryLabels(false)
                    description.isEnabled = false
                    legend.isEnabled = false
                    isDrawHoleEnabled = true

                    animateY(750, Easing.EaseInOutQuad)
                    setTransparentCircleAlpha(50)
                    setTransparentCircleColor(Color.WHITE)
                    holeRadius = 60F
                    setHoleColor(Color.WHITE)
                    invalidate()
                }

                chartPie.also {
                    tvPie0.text = it.data.dataSets[0].getEntryForIndex(0).label
                    tvPie1.text = it.data.dataSets[0].getEntryForIndex(1).label
                    tvPie2.text = it.data.dataSets[0].getEntryForIndex(2).label
                    tvPie3.text = it.data.dataSets[0].getEntryForIndex(3).label
                    tvPie4.text = it.data.dataSets[0].getEntryForIndex(4).label

                    tvPieValue0.text = getPercentage(it.data.yValueSum.toInt(), it.data.dataSets[0].getEntryForIndex(0).value)
                    tvPieValue1.text = getPercentage(it.data.yValueSum.toInt(), it.data.dataSets[0].getEntryForIndex(1).value)
                    tvPieValue2.text = getPercentage(it.data.yValueSum.toInt(), it.data.dataSets[0].getEntryForIndex(2).value)
                    tvPieValue3.text = getPercentage(it.data.yValueSum.toInt(), it.data.dataSets[0].getEntryForIndex(3).value)
                    tvPieValue4.text = getPercentage(it.data.yValueSum.toInt(), it.data.dataSets[0].getEntryForIndex(4).value)
                }

//                Basic Statistics
                tvKillAvgAll.text = String.format("%.2f", item.killRvgAll)
                tvDamageAvgAll.text = String.format("%.2f", item.damageRvgAll)
                tvKillAvgRecent.text = String.format("%.2f", item.killRvgRecent)
                tvDamageAvgRecent.text = String.format("%.2f", item.damageRvgRecent)

                val killIncrease = item.killRvgRecent - item.killRvgAll

                if (killIncrease > 0) {
                    tvKillIncrease.setTextColor(Color.BLUE)
                } else {
                    tvKillIncrease.setTextColor(Color.RED)
                }
                tvKillIncrease.text = String.format("(전체 평균 대비 %.2f)", killIncrease)

                val damageIncrease = item.damageRvgRecent - item.damageRvgAll

                if (damageIncrease > 0) {
                    tvDamageIncrease.setTextColor(Color.BLUE)
                } else {
                    tvDamageIncrease.setTextColor(Color.RED)
                }
                tvDamageIncrease.text = String.format("(전체 평균 대비 %.2f)", damageIncrease)

//                RadarChart
                chartRadar.apply {
                    setTouchEnabled(false)
                    setDrawMarkers(false)

                    val radarData = RadarData(item.radarDataSet.apply {
                        color = ContextCompat.getColor(
                            root.context,
                            R.color.main
                        )
                        lineWidth = 2f
                        setDrawFilled(true)
                        fillColor = ContextCompat.getColor(
                            root.context,
                            R.color.lighter
                        )
                    })

                    data = radarData.apply {
                        setValueTextSize(15f)
                    }

                    val valueList = ArrayList<String>().apply {
                        add(0, "킬 캐치")
                        add(1, "딜링")
                        add(2, "생존 능력")
                        add(3, "적극성")
                    }

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(valueList)
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
                    webLineWidthInner = 1f
                    webColor = ContextCompat.getColor(root.context, R.color.radar_line)
                    webColorInner = ContextCompat.getColor(root.context, R.color.radar_line)
                    animateXY(1000, 1000, Easing.EaseInOutQuad)
                    invalidate()
                }

//                BarChart
                chartBar.apply {
                    setTouchEnabled(false)
                    data = BarData(item.barDataSet)

                    item.barDataSet[0].apply {
                        color = ContextCompat.getColor(root.context, R.color.main)
                        valueTextSize = 9f
                    }
                    item.barDataSet[1].apply {
                        color = ContextCompat.getColor(root.context, R.color.main)
                        valueTextSize = 12f
                        setValueTextColors(
                            listOf(
                                ContextCompat.getColor(
                                    root.context,
                                    R.color.transparent
                                ), ContextCompat.getColor(root.context, R.color.white)
                            )
                        )
                    }

                    axisLeft.setDrawGridLines(false)
                    xAxis.setDrawGridLines(false)
                    xAxis.setDrawAxisLine(false)
                    xAxis.setDrawGridLinesBehindData(false)
                    xAxis.setDrawLabels(false)
                    xAxis.setDrawLimitLinesBehindData(false)

                    legend.isEnabled = false
                    description.isEnabled = false
                    animateXY(0, 500, Easing.EaseInOutQuad)
                    invalidate()
                }


                btnRefreshMatch.setOnClickListener {
                    onClickRefresh()
                }

                val refreshedDate = mUnixConverter.getTimestampToDate(item.refreshedDate.toString())
                tvRefreshedDate.text = "$refreshedDate 갱신됨"
            }
        }
    }

    private fun getPercentage(ySum: Int, yValue: Float) = String.format("%.1f", (yValue / ySum) * 100) + "%"

    inner class FooterViewHolder(private val binding: ItemStatisticsFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Footer) {
            binding.tvFooter.text = item.text
        }
    }
}


