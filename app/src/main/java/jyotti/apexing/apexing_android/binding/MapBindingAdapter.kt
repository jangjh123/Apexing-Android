package jyotti.apexing.apexing_android.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.util.getTimestampToDate

@BindingAdapter("app:setMapTimeLimit")
fun TextView.setMapTimeLimit(endTime: String) {
    text = this.context.getString(R.string.map_time_limit, getTimestampToDate(endTime))
}