package jyotti.apexing.apexing_android.ui.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.databinding.ItemMapBinding


class MapAdapter : ListAdapter<Map, RecyclerView.ViewHolder>(MapDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemMapBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val map = getItem(position)
            holder.bind(map)
        }
    }

    class ViewHolder(private val binding: ItemMapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(map: Map) = with(binding) { this.map = map }
    }
}

class MapDiffUtil : DiffUtil.ItemCallback<Map>() {
    override fun areItemsTheSame(oldItem: Map, newItem: Map): Boolean = oldItem.endTime == newItem.endTime
    override fun areContentsTheSame(oldItem: Map, newItem: Map): Boolean = oldItem == newItem
}