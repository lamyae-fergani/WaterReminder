package com.example.waterreminder

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class WaterLevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var targetProgress = 0f
    private var animProgress = 0f

    private val paintBottle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1769AA")
        style = Paint.Style.STROKE
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintGlassBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EAF4F8")
        style = Paint.Style.FILL
    }

    private val paintWater1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#A4D7E1")
        style = Paint.Style.FILL
    }

    private val paintWater2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4A90E2")
        style = Paint.Style.FILL
    }

    private val paintWater3 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1769AA")
        style = Paint.Style.FILL
    }

    private val paintShine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#44FFFFFF")
        style = Paint.Style.FILL
    }

    private val paintBubble = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#66FFFFFF")
        style = Paint.Style.FILL
    }

    private var waveAnimPhase = 0f

    init {
        val waveAnimator = ValueAnimator.ofFloat(0f, (2 * Math.PI).toFloat()).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                waveAnimPhase = it.animatedValue as Float
                invalidate()
            }
        }
        waveAnimator.start()
    }

    fun animateToProgress(progress: Float) {
        targetProgress = progress.coerceIn(0f, 1f)
        ValueAnimator.ofFloat(animProgress, targetProgress).apply {
            duration = 800
            addUpdateListener {
                animProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val bottleWidth = width * 0.45f
        val bottleHeight = height * 0.85f
        val left = (width - bottleWidth) / 2f
        val top = (height - bottleHeight) / 2f + 10f
        val right = left + bottleWidth
        val bottom = top + bottleHeight
        val radius = 24f

        val neckWidth = bottleWidth * 0.3f
        val neckHeight = 25f
        val neckLeft = (width - neckWidth) / 2f
        val neckTop = top - neckHeight
        val neckRight = neckLeft + neckWidth

        canvas.drawRect(neckLeft, neckTop, neckRight, top, paintBottle)
        canvas.drawRect(neckLeft, neckTop, neckRight, top, paintGlassBg)

        canvas.drawRoundRect(left, top, right, bottom, radius, radius, paintGlassBg)
        canvas.drawRoundRect(left, top, right, bottom, radius, radius, paintBottle)

        if (animProgress > 0f) {
            canvas.save()
            val clipPath = Path().apply {
                addRoundRect(left + 3f, top + 3f, right - 3f, bottom - 3f, radius - 2f, radius - 2f, Path.Direction.CW)
            }
            canvas.clipPath(clipPath)

            val waterHeight = bottleHeight * animProgress
            val waterTop = bottom - waterHeight

            canvas.drawRect(left, waterTop + (waterHeight * 0.6f), right, bottom, paintWater3)
            canvas.drawRect(left, waterTop + (waterHeight * 0.3f), right, waterTop + (waterHeight * 0.6f), paintWater2)
            canvas.drawRect(left, waterTop, right, waterTop + (waterHeight * 0.3f), paintWater1)

            val wavePath = Path().apply {
                moveTo(left, waterTop)
                val waveWidth = right - left
                val waveHeight = 6f
                var x = left
                while (x <= right) {
                    val y = waterTop + sin((x / waveWidth * 4 * Math.PI) + waveAnimPhase).toFloat() * waveHeight
                    lineTo(x, y)
                    x += 5f
                }
                lineTo(right, bottom)
                lineTo(left, bottom)
                close()
            }
            canvas.drawPath(wavePath, paintWater1)

            canvas.drawRect(left + 8f, waterTop + 10f, left + 20f, bottom - 10f, paintShine)

            val bubbleX = left + bottleWidth * 0.3f
            val bubbleY1 = bottom - (waterHeight * 0.4f) + sin(waveAnimPhase) * 10f
            val bubbleY2 = bottom - (waterHeight * 0.7f) + cos(waveAnimPhase) * 8f
            canvas.drawCircle(bubbleX, bubbleY1, 6f, paintBubble)
            canvas.drawCircle(left + bottleWidth * 0.7f, bubbleY2, 4f, paintBubble)

            canvas.restore()
        }
    }
}
