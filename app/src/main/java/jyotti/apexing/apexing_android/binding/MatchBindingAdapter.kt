package jyotti.apexing.apexing_android.binding

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.util.getTimestampToDate
import kotlin.math.roundToInt

private const val MAD_MAGGIE = "Mad Maggie"
private const val MAD_MAGGIE_IN_LOWERCASE = "madmaggie"
private const val MODE_BATTLE_ROYALE = "BATTLE_ROYALE"
private const val MODE_ARENA = "ARENAS"

@BindingAdapter("app:setLegendImage")
fun ImageView.setLegendImage(_name: String) {
    val name = if (_name == MAD_MAGGIE) MAD_MAGGIE_IN_LOWERCASE else _name.lowercase()

    Glide.with(context)
        .load(context.resources.getIdentifier(name.lowercase(), "drawable", "jyotti.apexing.apexing_android"))
        .into(this)
}

@BindingAdapter("app:setBackgroundWithMostLv")
fun View.setBackgroundWithMostLv(mostLv: Int) {
    background = when (mostLv) {
        0 -> AppCompatResources.getDrawable(context, R.drawable.most_0)
        1 -> AppCompatResources.getDrawable(context, R.drawable.most_1)
        2 -> AppCompatResources.getDrawable(context, R.drawable.most_2)
        3 -> AppCompatResources.getDrawable(context, R.drawable.most_3)
        else -> AppCompatResources.getDrawable(context, R.drawable.most_4)
    }
}

@BindingAdapter("app:setPieChartData")
fun PieChart.setPieChartData(pieChartData: PieData) {
    setTouchEnabled(false)
    data = pieChartData
    data.setValueTextColor(Color.TRANSPARENT)
    data.setValueTextSize(15f)
    data.dataSet.colors.apply {
        add(
            0,
            ContextCompat.getColor(
                context,
                R.color.most0
            )
        )
        add(1, ContextCompat.getColor(context, R.color.most1))
        add(2, ContextCompat.getColor(context, R.color.most2))
        add(3, ContextCompat.getColor(context, R.color.most3))
        add(4, ContextCompat.getColor(context, R.color.most4))
    }
    setCenterTextSize(15f)
    setCenterTextColor(Color.BLACK)
    setUsePercentValues(true)
    setDrawEntryLabels(false)
    description.isEnabled = false
    legend.isEnabled = false
    isDrawHoleEnabled = true
    setHoleColor(Color.TRANSPARENT)
    setTransparentCircleAlpha(50)
    setTransparentCircleColor(Color.WHITE)
    holeRadius = 60f
    invalidate()
}

@BindingAdapter("app:setPiePercentageAsString", "app:pieIndex")
fun TextView.setPiePercentageAsString(pieData: PieData?, pieIndex: Int) {
    pieData?.let {
        text = String.format("%.1f", (pieData.dataSets[0].getEntryForIndex(pieIndex).value / pieData.yValueSum.roundToInt()) * 100) + "%"
    }
}

@BindingAdapter("app:setModeText")
fun TextView.setModeText(mode: String) {
    text = when (mode) {
        MODE_BATTLE_ROYALE -> context.getString(R.string.battle_royal)
        MODE_ARENA -> context.getString(R.string.arena)
        else -> mode
    }
}

@BindingAdapter("app:setKorTimeText")
fun TextView.setKorTimeText(timeStampStr: String) {
    text = getTimestampToDate(timeStampStr)
}

@BindingAdapter("app:setSurvivalTime")
fun TextView.setSurvivalTime(secs: Int) {
    text = context.getString(R.string.survival_time, secs.div(60), secs.rem(60))
}