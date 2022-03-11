package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apexing.apexing_android.R
import com.apexing.apexing_android.databinding.ItemMatchBinding
import com.apexing.apexing_android.databinding.ItemStatisticsFooterBinding
import com.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import jyotti.apexing.apexing_android.data.model.statistics.MatchModelType
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.util.GenericDiffUtil
import jyotti.apexing.apexing_android.util.UnixConverter
import java.text.DecimalFormat
import java.util.*

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
            else -> {
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
            else -> {
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

                    "Mad Maggie" -> {
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
                    centerText = "Most 5"
                    setCenterTextSize(15f)
                    setCenterTextColor(Color.BLACK)
                    setUsePercentValues(true)
                    setDrawEntryLabels(false)
                    description.isEnabled = false
                    legend.isEnabled = false
                    animateY(750, Easing.EaseInOutQuad)
                    setTransparentCircleAlpha(50)
                    setTransparentCircleColor(Color.WHITE)
                    isDrawHoleEnabled = true
                    holeRadius = 60F
                    setHoleColor(Color.WHITE)
                    data = item.pieData
                    data.setValueTextColor(Color.WHITE)
                    data.setValueTextSize(15f)
                    data.dataSet.colors.apply {
                        add(0, ContextCompat.getColor(root.context, R.color.pie0))
                        add(1, ContextCompat.getColor(root.context, R.color.pie1))
                        add(2, ContextCompat.getColor(root.context, R.color.pie2))
                        add(3, ContextCompat.getColor(root.context, R.color.pie3))
                        add(4, ContextCompat.getColor(root.context, R.color.pie4))
                    }

                    tvPie0.text = item.pieData.dataSets[0].getEntryForIndex(0).label
                    tvPie1.text = item.pieData.dataSets[0].getEntryForIndex(1).label
                    tvPie2.text = item.pieData.dataSets[0].getEntryForIndex(2).label
                    tvPie3.text = item.pieData.dataSets[0].getEntryForIndex(3).label
                    tvPie4.text = item.pieData.dataSets[0].getEntryForIndex(4).label
                }

//                Basic Statistics
                tvKillAvgAll.text = String.format("%.2f", item.killRvgAll)
                tvDamageAvgAll.text = String.format("%.2f", item.damageRvgAll)
                tvKillAvgRecent.text = String.format("%.2f", item.killRvgRecent)
                tvDamageAvgRecent.text = String.format("%.2f", item.damageRvgRecent)

                val killIncrease = item.killRvgRecent - item.killRvgAll

                tvKillIncrease.text = String.format("(전체 평균 대비 %.2f)", killIncrease)
                if (killIncrease > 0) {
                    tvKillIncrease.setTextColor(Color.BLUE)
                } else {
                    tvKillIncrease.setTextColor(Color.RED)
                }

                val damageIncrease = item.damageRvgRecent - item.damageRvgAll

                tvDamageIncrease.text = String.format("(전체 평균 대비 %.2f)", damageIncrease)
                if (damageIncrease > 0) {
                    tvDamageIncrease.setTextColor(Color.BLUE)
                } else {
                    tvDamageIncrease.setTextColor(Color.RED)
                }

//                RadarChart
                chartRadar.apply {
                    setTouchEnabled(false)
                    setDrawMarkers(false)

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

                    val radarData = RadarData(item.radarDataSet.apply {
                        color = ContextCompat.getColor(root.context, R.color.main)
                        lineWidth = 2f
                        setDrawFilled(true)
                        fillColor = ContextCompat.getColor(root.context, R.color.lighter)
                    })

                    data = radarData.apply {
                        setValueTextSize(15f)
                    }

                    legend.isEnabled = false
                    description.isEnabled = false
                    webLineWidthInner = 1f
                    webColor = ContextCompat.getColor(root.context, R.color.radar_line)
                    webColorInner = ContextCompat.getColor(root.context, R.color.radar_line)
                    animateXY(1000, 1000, Easing.EaseInOutQuad)
                    invalidate()
                }

//                LineChart
                chartBar.apply {

                }


                btnRefreshMatch.setOnClickListener {
                    onClickRefresh()
                }

                val refreshedDate = mUnixConverter.getTimestampToDate(item.refreshedDate.toString())
                tvRefreshedDate.text = "$refreshedDate 갱신됨"
            }
        }
    }

    inner class FooterViewHolder(private val binding: ItemStatisticsFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchModels.Footer) {
            binding.tvFooter.text = item.text
        }
    }
}


