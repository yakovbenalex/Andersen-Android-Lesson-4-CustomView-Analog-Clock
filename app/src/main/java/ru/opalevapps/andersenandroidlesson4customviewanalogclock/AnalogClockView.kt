package ru.opalevapps.andersenandroidlesson4customviewanalogclock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*

class AnalogClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mHeight = 0
    private var mWidth = 0
    private var mRadius = 0
    private var mAngle = 0.0
    private var mCentreX = 0
    private var mCentreY = 0
    private var mPadding = 0
    private var mIsInit = false
    private lateinit var mPaint: Paint
    private lateinit var mPath: Path
    private lateinit var mRect: Rect
    private lateinit var mNumbers: IntArray
    private var mMinimum = 0
    private var mHour = 0f
    private var mMinute = 0f
    private var mSecond = 0f
    private var mHourHandSize = 0
    private var mMinuteHandSize = 0
    private var mSecondHandSize = 0
    private var mHourHandWidth = 0
    private var mMinuteHandWidth = 0
    private var mSecondHandWidth = 0
    private var mFontSize = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!mIsInit) init()

        drawCircle(canvas!!)
        drawHands(canvas)
        drawNumerals(canvas)
        drawNumeralsLines(canvas)
        postInvalidateDelayed(500)
    }

    private fun drawCircle(canvas: Canvas) {
        mPaint.reset()
        setPaintAttributes(R.color.circleFrame, Paint.Style.STROKE, 8)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat() + 40, mPaint)
        // draw small circle point at hands center
        setPaintAttributes(R.color.circleFrame, Paint.Style.FILL, 8)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), 16F, mPaint)
    }

    private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
        mPaint.reset()
        mPaint.color = ContextCompat.getColor(context, colour)
        mPaint.style = stroke
        mPaint.strokeWidth = strokeWidth.toFloat()
        mPaint.setStrokeCap(Paint.Cap.ROUND) // round stroke line cap
        mPaint.isAntiAlias = true
    }

    private fun drawHands(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()
        mHour = calendar.get(Calendar.HOUR_OF_DAY).toFloat()
        //convert to 12hour format from 24 hour format
        mHour = if (mHour > 12) mHour - 12 else mHour
        mMinute = calendar.get(Calendar.MINUTE).toFloat()
        mSecond = calendar.get(Calendar.SECOND).toFloat()
        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute + (mSecond / 60))
        drawSecondsHand(canvas, mSecond)
    }

    private fun drawMinuteHand(canvas: Canvas, location: Float) {
        mPaint.reset()
        setPaintAttributes(R.color.minuteHand, Paint.Style.STROKE, mMinuteHandWidth)
        // rotate canvas to degree by minute count
        val angle = (location * 6F)
        canvas.rotate(angle, mCentreX.toFloat(), mCentreY.toFloat())
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            mCentreX.toFloat(),
            mCentreY.toFloat() - mMinuteHandSize,
            mPaint
        )
        // rotate canvas back
        canvas.rotate(-angle, mCentreX.toFloat(), mCentreY.toFloat())
    }

    private fun drawHourHand(canvas: Canvas, location: Double) {
        mPaint.reset()
        setPaintAttributes(R.color.hourHand, Paint.Style.STROKE, mHourHandWidth)
        // rotate canvas to degree by minute count
        val angle = (location * 6).toFloat()
        canvas.rotate(angle, mCentreX.toFloat(), mCentreY.toFloat())
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            mCentreX.toFloat(),
            mCentreY.toFloat() - mHourHandSize,
            mPaint
        )
        // rotate canvas back
        canvas.rotate(-angle, mCentreX.toFloat(), mCentreY.toFloat())
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        mPaint.reset()
        setPaintAttributes(R.color.secondHand, Paint.Style.STROKE, mSecondHandWidth)
        // rotate canvas to degree by seconds count
        val angle = (location * 6)
        canvas.rotate(angle, mCentreX.toFloat(), mCentreY.toFloat())
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            mCentreX.toFloat(),
            mCentreY.toFloat() - mSecondHandSize,
            mPaint
        )
        // rotate canvas back
        canvas.rotate(-angle, mCentreX.toFloat(), mCentreY.toFloat())
    }

    private fun drawNumerals(canvas: Canvas) {
        setPaintAttributes(R.color.numerals, Paint.Style.STROKE, 4)
        mPaint.textSize = mFontSize.toFloat()
        for (number in mNumbers) {
            val num = number.toString()
            mPaint.getTextBounds(num, 0, num.length, mRect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (mCentreX + Math.cos(angle) * (mRadius - 30) - mRect.width() / 2).toInt()
            val y = (mCentreY + Math.sin(angle) * (mRadius - 30) + mRect.height() / 2).toInt()
            canvas.drawText(num, x.toFloat(), y.toFloat(), mPaint)
        }
    }

    private fun drawNumeralsLines(canvas: Canvas) {
        mPaint.reset()
        // paint style for small lines
        setPaintAttributes(R.color.numeralLines, Paint.Style.STROKE, 4)
        mPaint.setStrokeCap(Paint.Cap.ROUND) // round stroke line cap
        for (i in 1..60) {
            canvas.rotate(6F, mCentreX.toFloat(), mCentreY.toFloat())
            if (i % 5 == 0) {
                // paint style for big lines each 5 minutes
                setPaintAttributes(R.color.numeralLines, Paint.Style.STROKE, 10)
                canvas.drawLine(
                    mCentreX.toFloat(),
                    50F,
                    mCentreX.toFloat(),
                    11F,
                    mPaint
                )
            } else {
                canvas.drawLine(
                    mCentreX.toFloat(),
                    30F,
                    mCentreX.toFloat(),
                    10F,
                    mPaint
                )
            }
            // paint style for small lines
            setPaintAttributes(R.color.numeralLines, Paint.Style.STROKE, 4)
        }
    }

    private fun init() {
        mHeight = height
        mWidth = width
        mPadding = 50
        mCentreX = mWidth / 2
        mCentreY = mHeight / 2
        mMinimum = Math.min(mHeight, mWidth)
        mRadius = mMinimum / 2 - mPadding
        mAngle = (Math.PI / 30 - Math.PI / 2)
        mPaint = Paint()
        mPath = Path()
        mRect = Rect()
        // hands size
        mHourHandSize = mRadius * resources.getInteger(R.integer.hourHandSize) / 100
        mMinuteHandSize = mRadius * resources.getInteger(R.integer.minuteHandSize) / 100
        mSecondHandSize = mRadius * resources.getInteger(R.integer.secondHandSize) / 100
        // hands width
        mHourHandWidth = (mRadius / 2) * resources.getInteger(R.integer.hourHandWidth) / 100
        mMinuteHandWidth = (mRadius / 2) * resources.getInteger(R.integer.minuteHandWidth) / 100
        mSecondHandWidth = (mRadius / 2) * resources.getInteger(R.integer.secondHandWidth) / 100

        mNumbers = (1..12).toList().toIntArray()
        mFontSize = 40
        mIsInit = true
    }
}