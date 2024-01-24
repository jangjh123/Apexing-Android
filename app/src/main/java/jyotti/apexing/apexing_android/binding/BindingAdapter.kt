package jyotti.apexing.apexing_android.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*

@BindingAdapter("setLayoutManager")
fun setLayoutManager(view: RecyclerView, orientation: Int) {
    view.layoutManager = LinearLayoutManager(view.context, orientation, false)
}

@BindingAdapter("setGridLayoutManager")
fun setGridLayoutManager(view: RecyclerView, span: Int) {
    view.layoutManager = GridLayoutManager(view.context, span, RecyclerView.VERTICAL, false)
}

@BindingAdapter("setSnapHelper")
fun setSnapHelper(view: RecyclerView, boolean: Boolean) {
    if (boolean) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(view)
    }
}