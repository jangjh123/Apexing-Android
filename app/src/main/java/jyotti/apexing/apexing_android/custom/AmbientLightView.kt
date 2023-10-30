package jyotti.apexing.apexing_android.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import jyotti.apexing.apexing_android.R
import kotlin.math.roundToInt

const val DEF_light_COLOR = "#FF000000"
const val DEF_light_LENGTH = 20f
const val DEF_RADIUS = 16f
const val DEF_INSIDE_COLOR = "#FFFFFFFF"

class AmbientLightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var lightColor = Color.parseColor(DEF_light_COLOR)
    private var lightLength = DEF_light_LENGTH
    private var radius = DEF_RADIUS
    private var insideColor = Color.parseColor(DEF_INSIDE_COLOR)

    init {
        setAttributeSet(attrs!!)
    }

    private fun setAttributeSet(attrs: AttributeSet) {
        context.obtainStyledAttributes(
            attrs, R.styleable.AmbientLightView
        ).run {
            lightColor = getColor(
                R.styleable.AmbientLightView_setLightColor, Color.parseColor(
                    DEF_light_COLOR
                )
            )
            lightLength =
                getDimension(R.styleable.AmbientLightView_setLightLength, DEF_light_LENGTH)
            radius = getDimension(R.styleable.AmbientLightView_setRadius, DEF_RADIUS)
            insideColor = getColor(
                R.styleable.AmbientLightView_setInsideColor, Color.parseColor(
                    DEF_INSIDE_COLOR
                )
            )
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setDefaultPadding()
        drawLight(canvas)
        drawInside(canvas)
    }

    private fun setDefaultPadding() {
        this.setPadding(
            lightLength.roundToInt(),
            lightLength.roundToInt(),
            lightLength.roundToInt(),
            lightLength.roundToInt()
        )
    }

    private fun drawLight(canvas: Canvas) {
        val paint = Paint().apply {
            isAntiAlias = true
            color = lightColor
            strokeWidth = lightLength / 100
            style = Paint.Style.STROKE
        }

        var calLightLength = lightLength

        for (i in 0 until 100) {
            paint.alpha = 100 - (1 * i)
            canvas.drawRoundRect(
                calLightLength,
                calLightLength,
                measuredWidth.toFloat() - calLightLength,
                measuredHeight.toFloat() - calLightLength,
                radius,
                radius,
                paint
            )

            calLightLength -= (lightLength / 100)
        }
    }

    private fun drawInside(canvas: Canvas) {
        val paint = Paint().apply {
            isAntiAlias = true
            color = insideColor
            style = Paint.Style.FILL
        }

        canvas.drawRoundRect(
            lightLength,
            lightLength,
            measuredWidth - lightLength,
            measuredHeight - lightLength,
            radius,
            radius,
            paint
        )
    }

    fun setLightColor(color: Int) {
        lightColor = color
    }
}