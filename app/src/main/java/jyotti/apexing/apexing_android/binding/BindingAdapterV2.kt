package jyotti.apexing.apexing_android.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

@BindingAdapter("list")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.bindList(items: List<T>) {
    val adapter = this.adapter ?: return
    val listAdapter: ListAdapter<T, VH> = adapter as ListAdapter<T, VH>
    listAdapter.submitList(items)
}

@BindingAdapter("setSnapHelper")
fun RecyclerView.setSnapHelper(isNeeded: Boolean) {
    if (isNeeded) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }
}

@BindingAdapter("setImage")
fun ImageView.setImage(imageSource: String) {
    Glide.with(this.context)
        .load(imageSource.replace("\"", ""))
        .into(this)
}

@BindingAdapter("formattedNumberText")
fun TextView.formattedNumberText(number: Int) {
    val dec = DecimalFormat("###,###,###")
    text = dec.format(number)
}