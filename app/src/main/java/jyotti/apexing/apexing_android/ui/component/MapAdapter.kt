package jyotti.apexing.apexing_android.ui.component

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apexing.apexing_android.databinding.ItemMapBinding
import com.bumptech.glide.Glide
import jyotti.apexing.apexing_android.data.model.main.map.Map
import jyotti.apexing.apexing_android.util.GenericDiffUtil
import jyotti.apexing.apexing_android.util.UnixConverter

class MapAdapter :
    ListAdapter<Map, RecyclerView.ViewHolder>(
        GenericDiffUtil()
    ) {

    private val unixConverter = UnixConverter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemMapBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val map = getItem(position)
            holder.bind(map)
        }
    }

    inner class ViewHolder(private val binding: ItemMapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(map: Map) {
            with(binding) {
                Glide.with(root)
                    .load(map.current.asset)
                    .fitCenter()
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(ivMap)

                tvMapType.text = map.type
                tvMapName.text = map.current.map

                if (map.current.end > 0) {
                    tvMapTime.text =
                        unixConverter.getTimestampToDate(map.current.end.toString()) + " 까지"
                }
            }
        }
    }
}