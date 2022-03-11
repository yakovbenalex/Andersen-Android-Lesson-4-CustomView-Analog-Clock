package ru.opalevapps.andersenandroidlesson4customviewanalogclock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
    private var mHandSize = 0
    private var mFontSize = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!mIsInit) {
            init()
        }

        drawCircle(canvas!!)
        drawHands(canvas)
        drawNumerals(canvas)
        drawNumeralsLines(canvas)
        postInvalidateDelayed(500)
    }

    fun drawCircle(canvas: Canvas) {
        mPaint.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8)
        canvas.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat() + 40, mPaint)
    }

    private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
        mPaint.reset()
        mPaint.color = colour
        mPaint.style = stroke
        mPaint.strokeWidth = strokeWidth.toFloat()
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
        drawMinuteHand(canvas, mMinute)
        drawSecondsHand(canvas, mSecond)
    }

    fun drawMinuteHand(canvas: Canvas, location: Float) {
        mPaint.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + Math.cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + Math.sin(mAngle) * mHourHandSize).toFloat(),
            mPaint
        )
    }

    private fun drawHourHand(canvas: Canvas, location: Double) {
        mPaint.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 10)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + Math.cos(mAngle) * mHourHandSize).toFloat(),
            (mCentreY + Math.sin(mAngle) * mHourHandSize).toFloat(),
            mPaint
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        mPaint.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            mCentreX.toFloat(),
            mCentreY.toFloat(),
            (mCentreX + Math.cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + Math.sin(mAngle) * mHourHandSize).toFloat(),
            mPaint
        )
    }

    private fun drawNumerals(canvas: Canvas) {
        setPaintAttributes(Color.BLUE, Paint.Style.STROKE, 4)
        mPaint.textSize = mFontSize.toFloat()
        for (number in mNumbers) {
            val num = number.toString()
            mPaint.getTextBounds(num, 0, num.length, mRect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (mCentreX + Math.cos(angle) * (mRadius - 15) - mRect.width() / 2).toInt()
            val y = (mCentreY + Math.sin(angle) * (mRadius - 15) + mRect.height() / 2).toInt()
            canvas.drawText(num, x.toFloat(), y.toFloat(), mPaint)
        }
    }

    fun drawNumeralsLines(canvas: Canvas) {
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8)

        for (i in 1..12) {
            canvas.rotate(30F, (width/2).toFloat(), (height/2).toFloat())
            canvas.drawLine(
                mCentreX.toFloat(),
                35F,
                mCentreX.toFloat(),
                7F,
                mPaint
            )
        }
    }

    fun init() {
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
        mHourHandSize = mRadius - mRadius / 2
        mHandSize = mRadius - mRadius / 4
        mNumbers = (1..12).toList().toIntArray()
        mFontSize = 36
        mIsInit = true
    }
}