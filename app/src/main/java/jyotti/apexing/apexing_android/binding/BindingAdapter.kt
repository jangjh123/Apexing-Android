package jyotti.apexing.apexing_android.binding

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import jyotti.apexing.apexing_android.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object BindingAdapter {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @JvmStatic
    @BindingAdapter(
        "gradientStartColor",
        "gradientEndColor"
    )
    fun setGradient(
        view: TextView,
        startColor: Int,
        endColor: Int
    ) {
        val paint = view.paint
        val width = paint.measureText(view.text as String?)
        val shader = LinearGradient(
            0f, 0f, width, view.textSize,
            listOf(startColor, endColor).toIntArray(),
            null,
            Shader.TileMode.CLAMP
        )
        view.paint.shader = shader
    }

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
            val indicators = listOf(indicator0, indicator1, indicator2, indicator3, indicator4)
            view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val currentIndex =
                        (view.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    for (i in indicators.indices) {
                        if (i == currentIndex) {
                            indicators[i].setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.deep_gray
                                )
                            )
                        } else {
                            indicators[i].setColorFilter(
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