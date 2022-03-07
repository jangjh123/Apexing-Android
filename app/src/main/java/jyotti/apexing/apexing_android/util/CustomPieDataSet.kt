package jyotti.apexing.apexing_android.util

import android.graphics.Color
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class CustomPieDataSet(yValues: List<PieEntry>, label: String) :
    PieDataSet(yValues, label) {

    override fun getEntryIndex(e: PieEntry?): Int {
        return getEntryIndex(e)
    }

    override fun getValueTextColor(index: Int): Int {
        return if (getEntryForIndex(index).value < 10f) {
            Color.parseColor("#00000000")
        } else {
            valueTextColor
        }
    }
}

