package jyotti.apexing.apexing_android.binding

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import jyotti.apexing.apexing_android.R
import kotlin.math.roundToInt

@BindingAdapter("app:setLegendImage")
fun ImageView.setLegendImage(name: String) {
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

@BindingAdapter("app:setRadarChartData")
fun RadarChart.setRadarChartData(radarDataSet: RadarDataSet) {
    setTouchEnabled(false)
    setDrawMarkers(false)
    val radarData = RadarData(radarDataSet.apply {
        color = ContextCompat.getColor(context, R.color.main)
        lineWidth = 2f
        setDrawFilled(true)
        fillColor = ContextCompat.getColor(context, R.color.main)
    })
    data = radarData.apply {
        setValueTextSize(15f)
        setValueTextColor(ContextCompat.getColor(context, R.color.light_gray))
    }
    val valueList = mutableListOf<String>().apply {
        add(0, context.getString(R.string.kill_catch))
        add(1, context.getString(R.string.dealing))
        add(2, context.getString(R.string.survival_ability))
        add(3, context.getString(R.string.positiveness))
    }
    xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(valueList)
        textColor = ContextCompat.getColor(context, R.color.white)
        textSize = 15f
    }
    yAxis.apply {
        setDrawLabels(false)
        axisMinimum = 0f
        axisMaximum = 100f
        typeface = Typeface.DEFAULT_BOLD
        setLabelCount(10, true)
    }
    legend.isEnabled = false
    description.isEnabled = false
    webLineWidthInner = 0f
    webLineWidth = 0f
    webColor = ContextCompat.getColor(context, R.color.main)
    webColorInner = ContextCompat.getColor(context, R.color.divider)
    invalidate()
}