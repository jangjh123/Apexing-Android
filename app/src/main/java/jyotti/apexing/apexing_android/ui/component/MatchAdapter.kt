package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Header
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Match
import jyotti.apexing.apexing_android.data.model.statistics.MostLegend
import jyotti.apexing.apexing_android.databinding.ItemMatchBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding
import jyotti.apexing.apexing_android.util.firstDecimalString
import jyotti.apexing.apexing_android.util.formatAmount
import jyotti.apexing.apexing_android.util.getTimestampToDate
import jyotti.apexing.apexing_android.util.setImageWithResourceId
import kotlin.math.roundToInt

class MatchAdapter(
    private inline val onClickRecordingDesc: () -> Unit,
    private inline val onClickRefreshDesc: () -> Unit
) : ListAdapter<MatchModels, RecyclerView.ViewHolder>(MatchDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> VIEW_TYPE_HEADER
            is Match -> VIEW_TYPE_MATCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(ItemStatisticsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_MATCH -> MatchViewHolder(ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_HEADER -> (holder as HeaderViewHolder).bind(getItem(position) as Header)
            VIEW_TYPE_MATCH -> (holder as MatchViewHolder).bind(getItem(position) as Match)
        }
    }

    class HeaderViewHolder(private val binding: ItemStatisticsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var mostLegends: List<Pair<String, MostLegend>>
        private val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 200 }
        private val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200 }

        fun bind(header: Header) = with(binding) {
            binding.viewHolder = this@HeaderViewHolder
            mostLegends = header.mostLegends

            /*
                UpdateTime
            */
            tvMyIndex.text = "${header.updateTimeLeft}"

            /*
                 PieChart
             */
            civMost0.setImageWithResourceId(mostLegends[0].first)
            civMost1.setImageWithResourceId(mostLegends[1].first)
            civMost2.setImageWithResourceId(mostLegends[2].first)
            civMost3.setImageWithResourceId(mostLegends[3].first)
            civMost4.setImageWithResourceId(mostLegends[4].first)
            chartPie.run {
                setTouchEnabled(false)
                data = header.pieData
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
                holeRadius = 60f

                tvPie0.text = data.dataSets[0].getEntryForIndex(0).label
                tvPie1.text = data.dataSets[0].getEntryForIndex(1).label
                tvPie2.text = data.dataSets[0].getEntryForIndex(2).label
                tvPie3.text = data.dataSets[0].getEntryForIndex(3).label
                tvPie4.text = data.dataSets[0].getEntryForIndex(4).label
                tvPieValue0.text = getPercentage(data.yValueSum.roundToInt(), data.dataSets[0].getEntryForIndex(0).value)
                tvPieValue1.text = getPercentage(data.yValueSum.roundToInt(), data.dataSets[0].getEntryForIndex(1).value)
                tvPieValue2.text = getPercentage(data.yValueSum.roundToInt(), data.dataSets[0].getEntryForIndex(2).value)
                tvPieValue3.text = getPercentage(data.yValueSum.roundToInt(), data.dataSets[0].getEntryForIndex(3).value)
                tvPieValue4.text = getPercentage(data.yValueSum.roundToInt(), data.dataSets[0].getEntryForIndex(4).value)
            }

            /*
                SimpleStatistics
             */
            tvAllGame0.text = root.context.getString(R.string.every_match, header.matches.size)
            tvAllGame1.text = root.context.getString(R.string.every_match, header.matches.size)
            tvKillAvgAll.text = root.context.getString(R.string.kill_with_string, header.killAvgAll.firstDecimalString())
            tvDamageAvgAll.text = root.context.getString(R.string.damage_with_string, header.damageAvgAll.firstDecimalString())
            tvKillAvgRecent.text = root.context.getString(R.string.kill_with_string, header.killAvgRecent.firstDecimalString())
            tvDamageAvgRecent.text = root.context.getString(R.string.damage_with_string, header.damageAvgRecent.firstDecimalString())

            /*
                RadarChart
             */
            chartRadar.apply {
                setTouchEnabled(false)
                setDrawMarkers(false)
                val radarData = RadarData(header.radarDataSet.apply {
                    color = ContextCompat.getColor(context, R.color.main)
                    lineWidth = 2f
                    setDrawFilled(true)
                    fillColor = ContextCompat.getColor(context, R.color.main)
                })
                data = radarData.apply {
                    setValueTextSize(15f)
                    setValueTextColor(ContextCompat.getColor(context, R.color.light_gray))
                }
                val valueList = mutableListOf<String>().apply {
                    add(0, root.context.getString(R.string.kill_catch))
                    add(1, root.context.getString(R.string.dealing))
                    add(2, root.context.getString(R.string.survival_ability))
                    add(3, root.context.getString(R.string.positiveness))
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

            invalidateAll()
        }

        private fun getPercentage(ySum: Int, yValue: Float): String = String.format("%.1f", (yValue / ySum) * 100) + "%"

        fun showLegendDetail(mostLv: Int) = with(binding) {
            viewDetailCircle.background = when (mostLv) {
                0 -> AppCompatResources.getDrawable(root.context, R.drawable.most_0)
                1 -> AppCompatResources.getDrawable(root.context, R.drawable.most_1)
                2 -> AppCompatResources.getDrawable(root.context, R.drawable.most_2)
                3 -> AppCompatResources.getDrawable(root.context, R.drawable.most_3)
                else -> AppCompatResources.getDrawable(root.context, R.drawable.most_4)
            }

            val (name, data) = mostLegends[mostLv]

            civLegendDetail.setImageWithResourceId(name)
            tvLegendKillAvg.text = data.getKillAvgString()
            tvLegendDamageAvg.text = data.getDamageAvgString()

            cvLegendDetail.run {
                visibility = View.VISIBLE
                startAnimation(fadeIn)
            }
        }

        fun hideLegendDetail() = with(binding) {
            cvLegendDetail.run {
                visibility = View.GONE
                startAnimation(fadeOut)
            }
        }
    }

    class MatchViewHolder(private val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Match) = with(binding) {
            with(binding) {
                if (match.damage == 0 && match.kill == 0) {
                    layMatch.alpha = 0.2f
                    tvNote.visibility = View.VISIBLE
                    tvNote.text = root.context.getString(R.string.please_set_tracker_or_play_hard)
                } else {
                    layMatch.alpha = 1f
                    tvNote.visibility = View.GONE
                }

                ivLegend.setImageWithResourceId(if (match.legend == MAD_MAGGIE) MAD_MAGGIE_IN_LOWERCASE else match.legend.lowercase())
                tvDate.text = getTimestampToDate(match.date.toString())
                tvSurvivalDuration.text = root.context.getString(R.string.survival_time, match.secs.div(60), match.secs.rem(60))
                when (match.mode) {
                    MODE_BATTLE_ROYALE -> {
                        tvMode.text = root.context.getString(R.string.battle_royal)
                    }

                    MODE_ARENA -> {
                        tvMode.text = root.context.getString(R.string.arena)
                    }

                    else -> {
                        tvMode.text = match.mode
                    }
                }
                tvKill.text = "${match.kill}"
                tvDamage.text = formatAmount(match.damage)
            }
        }

        companion object {
            private const val MAD_MAGGIE = "Mad Maggie"
            private const val MAD_MAGGIE_IN_LOWERCASE = "madmaggie"
            private const val MODE_BATTLE_ROYALE = "BATTLE_ROYALE"
            private const val MODE_ARENA = "ARENAS"
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_MATCH = 1
    }
}

/*
    sealed class 사용으로 인한 최적화 불가
*/
class MatchDiffUtil : DiffUtil.ItemCallback<MatchModels>() {
    override fun areItemsTheSame(oldItem: MatchModels, newItem: MatchModels): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: MatchModels, newItem: MatchModels): Boolean = oldItem == newItem
}