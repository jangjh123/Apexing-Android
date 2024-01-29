package jyotti.apexing.apexing_android.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jyotti.apexing.apexing_android.util.getThumbnail
import java.text.DecimalFormat

@BindingAdapter("app:list")
fun <T, VH : RecyclerView.ViewHolder> RecyclerView.bindList(items: List<T>) {
    val adapter = this.adapter ?: return
    val listAdapter: ListAdapter<T, VH> = adapter as ListAdapter<T, VH>
    listAdapter.submitList(items)
}

@BindingAdapter("app:setSnapHelper")
fun RecyclerView.setSnapHelper(isNeeded: Boolean) {
    if (isNeeded) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }
}

val dec = DecimalFormat("###,###,###")

@BindingAdapter("app:formattedNumberText")
fun TextView.formattedNumberText(number: Int) {
    text = dec.format(number)
}

@BindingAdapter("app:setImage")
fun ImageView.setImage(srcUri: String) {
    val src = srcUri.replace("\"", "")
    Glide.with(this.context)
        .load(src)
        .thumbnail(
            getThumbnail(
                context = this.context,
                srcUri = src
            )
        )
        .centerCrop()
        .into(this)
}


@BindingAdapter("app:setStringResource")
fun TextView.setStringResource(resId: Int) {
    text = this.context.getString(resId)
}