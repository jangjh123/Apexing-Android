package jyotti.apexing.apexing_android.ui.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.store.StoreItem
import jyotti.apexing.apexing_android.databinding.ItemStoreBinding
import jyotti.apexing.apexing_android.util.GenericDiffUtil
import jyotti.apexing.apexing_android.util.Utils.formatAmount
import jyotti.apexing.apexing_android.util.Utils.getThumbnail
import jyotti.apexing.apexing_android.util.Utils.getTimestampToDate

class StoreAdapter :
    ListAdapter<StoreItem, RecyclerView.ViewHolder>(GenericDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemStoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val storeItem = getItem(position)
            holder.bind(storeItem)
        }
    }

    inner class ViewHolder(private val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storeItem: StoreItem) {
            with(binding) {
                Glide.with(root.context)
                    .load(storeItem.asset)
                    .thumbnail(getThumbnail(root.context, storeItem.asset))
                    .into(ivItem)

                when (storeItem.shopTime) {
                    "specials" -> {
                        alv.setLightColor(ContextCompat.getColor(root.context, R.color.specials))
                    }
                    "shop" -> {
                        alv.setLightColor(ContextCompat.getColor(root.context, R.color.white))
                    }
                }

                tvItemName.text = storeItem.title

                when (storeItem.pricing.size) {
                    1 -> {
                        when (storeItem.pricing[0].ref) {
                            "Legend Tokens" -> {
                                ivPayment1.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.legend_token))
                            }
                            "Apex Coins" -> {
                                ivPayment1.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.apex_coin))
                            }
                        }
                        tvPayment1Amount.text = formatAmount(storeItem.pricing[0].quantity)
                        ivPayment2.visibility = View.GONE
                        tvPayment2Amount.text = ""
                    }
                    2 -> {
                        ivPayment2.visibility = View.VISIBLE
                        ivPayment1.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.legend_token))
                        tvPayment1Amount.text = formatAmount(storeItem.pricing[0].quantity)
                        ivPayment2.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.apex_coin))
                        tvPayment2Amount.text = formatAmount(storeItem.pricing[1].quantity)
                    }
                }

                tvItemExpirationDate.text = "${getTimestampToDate(storeItem.expireTimeStamp.toString())} 까지"
            }
        }
    }
}