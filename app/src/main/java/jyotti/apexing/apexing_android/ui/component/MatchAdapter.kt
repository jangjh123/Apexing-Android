package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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
import jyotti.apexing.apexing_android.databinding.ItemMatchBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding
import jyotti.apexing.apexing_android.util.firstDecimalString
import jyotti.apexing.apexing_android.util.getTimestampToDate
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
        private val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 200 }
        private val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200 }

        fun bind(header: Header) = with(binding) {
            this.header = header
            this.vh = this@HeaderViewHolder
//            invalidateAll()
        }

        fun showLegendDetail(mostLv: Int) = with(binding) {
            this.mostLv = mostLv

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
            if (match.damage == 0 && match.kill == 0) {
                layMatch.alpha = 0.2f
                tvNote.visibility = View.VISIBLE
                tvNote.text = root.context.getString(R.string.please_set_tracker_or_play_hard)
            } else {
                layMatch.alpha = 1f
                tvNote.visibility = View.GONE
            }

//            ivLegend.setImageWithResourceId(if (match.legend == MAD_MAGGIE) MAD_MAGGIE_IN_LOWERCASE else match.legend.lowercase())
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
//            tvDamage.text = formatAmount(match.damage)
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