package jyotti.apexing.apexing_android.binding

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.apexing.apexing_android.R
import jyotti.apexing.apexing_android.data.model.statistics.MatchModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object BindingAdapter {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @JvmStatic
    @BindingAdapter("setAdapter")
    fun setAdapter(
        view: RecyclerView,
        adapter: ListAdapter<Any?, RecyclerView.ViewHolder>
    ) {
        view.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("setLayoutManager")
    fun setLayoutManager(view: RecyclerView, orientation: Int) {
        view.layoutManager = LinearLayoutManager(view.context, orientation, false)
    }

    @JvmStatic
    @BindingAdapter("setSnapHelper")
    fun setSnapHelper(view: RecyclerView, boolean: Boolean) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(view)
    }

    @JvmStatic
    @BindingAdapter("setAutoScroll")
    fun setAutoScroll(view: RecyclerView, boolean: Boolean) {
        if (boolean) {
            try {
                coroutineScope.launch {
                    if (boolean) {
                        while (!view.isComputingLayout) {
                            delay(3000L)
                            val currentItemPosition =
                                (view.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            when (currentItemPosition) {
                                (view.adapter?.itemCount?.minus(1)) -> {
                                    view.smoothScrollToPosition(0)
                                }
                                else -> {
                                    view.smoothScrollToPosition(currentItemPosition + 1)
                                }

                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("exception", e.toString())
            }
        }
    }

    @JvmStatic
    @BindingAdapter(
        "context",
        "indicator0",
        "indicator1",
        "indicator2",
        "indicator3",
        "indicator4",
        "setIndicator"
    )
    fun setIndicator(
        view: RecyclerView,
        context: Context,
        indicator0: ImageView,
        indicator1: ImageView,
        indicator2: ImageView,
        indicator3: ImageView,
        indicator4: ImageView,
        boolean: Boolean
    ) {
        if (boolean) {
            val indicatorList = listOf(indicator0, indicator1, indicator2, indicator3, indicator4)
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val currentIndex =
                        (view.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    for (i in indicatorList.indices) {
                        if (i == currentIndex) {
                            indicatorList[i].setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.deep_gray
                                )
                            )
                        } else {
                            indicatorList[i].setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.gray
                                )
                            )
                        }
                    }
                }
            })
        }
    }
}