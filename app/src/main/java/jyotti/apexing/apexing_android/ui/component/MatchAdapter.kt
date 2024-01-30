package jyotti.apexing.apexing_android.ui.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Header
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels.Match
import jyotti.apexing.apexing_android.databinding.ItemMatchBinding
import jyotti.apexing.apexing_android.databinding.ItemStatisticsHeaderBinding

class MatchAdapter(
    private val onClickRecordingDesc: () -> Unit,
    private val onClickRefreshDesc: () -> Unit
) : ListAdapter<MatchModels, RecyclerView.ViewHolder>(MatchDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> VIEW_TYPE_HEADER
            is Match -> VIEW_TYPE_MATCH
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                binding = ItemStatisticsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickRecordingDesc = onClickRecordingDesc,
                onClickRefreshDesc = onClickRefreshDesc
            )

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

    class HeaderViewHolder(
        private val binding: ItemStatisticsHeaderBinding,
        private val onClickRecordingDesc: () -> Unit,
        private val onClickRefreshDesc: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 200 }
        private val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200 }

        fun bind(header: Header) = with(binding) {
            this.header = header
            this.vh = this@HeaderViewHolder
        }

        fun onClickRecordingHelp() {
            onClickRecordingDesc()
        }

        fun onClickRefreshHelp() {
            onClickRefreshDesc()
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
        fun bind(match: Match) = with(binding) { this.match = match }
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