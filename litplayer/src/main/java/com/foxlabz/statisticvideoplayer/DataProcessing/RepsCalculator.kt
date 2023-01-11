package com.litmethod.android.DataProcessing

import android.app.Activity
import android.os.CountDownTimer
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt

object RepsCalculator {

    private const val BAND_THRESHOLD_VALUE = 2
    private var isBothBandUnderTension: Boolean = false
    var isLeftBandUnderTension: Boolean = false
    var isRightBandUnderTension: Boolean = false
    var noBandUnderTension: Boolean = false
    var timeUnderTension = 0
    var timeUnderTensionTimer: CountDownTimer? = null
    var FUTURE_TIME: Long = 20000

    var leftBandTensionTime = 0
    var totalBandTensionTime = 0
    var rightBandTensionTime = 0;
    var privateLastDataTimeStamp = System.currentTimeMillis()
    enum class BandDirection {
        Up,
        Down
    }


    private var maximumWeightPulledSoFar = 0
    private var maxLeftBandWeightPulled = 0
    private var maxRightBandWeightPulled = 0

    private var leftBandLoad = 0.0
    private var rightBandLoad = 0.0

    private var totalWeight = 0.0;
    private var weightCalculated = 0

    private var leftBandDataList = mutableListOf<Reps>()
    private var rightBandDataList = mutableListOf<Reps>()


    var activity: Activity? = null
    fun leftBandActivity(weight: Double) {
        this.leftBandLoad = weight
        loadActivity(this.leftBandLoad, this.rightBandLoad)
    }


    fun rightBandActivity(weight: Double) {
        this.rightBandLoad = weight
        loadActivity(this.leftBandLoad, this.rightBandLoad)
    }


    private fun loadActivity(leftBandLoad: Double, rightBandLoad: Double) {

        val timeStamp = System.currentTimeMillis()
        if (timeStamp - privateLastDataTimeStamp > 1000 && (leftBandLoad > BAND_THRESHOLD_VALUE || rightBandLoad > BAND_THRESHOLD_VALUE)) {
                LitVideoPlayerSDK.resistanceObservable.postValue(
                    Pair(
                        floor(
                            max(
                                leftBandLoad,
                                rightBandLoad
                            )
                        ).toInt(), floor(calculateTotalWeight(leftBandLoad, rightBandLoad)).toInt()
                    )
                )
                calculateRepsAndWeight(leftBandLoad, rightBandLoad)
                privateLastDataTimeStamp = timeStamp
                calculateTimeUnderTension(floor(leftBandLoad), floor(rightBandLoad))
            }
    }


    fun calculateLeftBand(currentWeight: Double) {

    }

    fun calculateRightBand(currentWeight: Double) {

    }

    fun calculateRepsAndWeight(leftWeight: Double, rightWeight: Double) {
        leftBandDataList.add(
            Reps(
                leftWeight,
                RepsConstant.POSITION_LEFT_BAND,
                System.currentTimeMillis()
            )
        )
        rightBandDataList.add(
            Reps(
                rightWeight,
                RepsConstant.POSITION_RIGHT_BAND,
                System.currentTimeMillis()
            )
        )
        calculateLeftBand(leftWeight)
        calculateRightBand(rightWeight)
    }

    fun calculateMaximumWeight(leftWeight: Float, rightWeight: Float): Int {
        return -1
    }

    fun calculateTotalWeight(leftWeight: Double, rightWeight: Double): Double {
        totalWeight += (leftWeight + rightWeight).roundToInt()
        return totalWeight
    }


    fun calculateTimeUnderTension(leftBandWeight: Double, rightBandWeight: Double) {

        isLeftBandUnderTension = leftBandWeight > BAND_THRESHOLD_VALUE
        isRightBandUnderTension = rightBandWeight > BAND_THRESHOLD_VALUE

        if (isLeftBandUnderTension && isRightBandUnderTension) {
            isBothBandUnderTension = true
            //Case when both the bands are in tension
            //Both bands are under tension
            this.timeUnderTensionTimer?.cancel()
            this.timeUnderTensionTimer = null
            if (this.timeUnderTensionTimer == null) {

                this.timeUnderTensionTimer = object : CountDownTimer(FUTURE_TIME, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        totalBandTensionTime = totalBandTensionTime + 1;
                        leftBandTensionTime = leftBandTensionTime + 1
                        rightBandTensionTime = rightBandTensionTime + 1
                    }

                    override fun onFinish() {

                    }
                }.start()

            }
        } else if (isLeftBandUnderTension) {
            //case when right band is only in tension
            if (this.timeUnderTensionTimer == null) {
                    this.timeUnderTensionTimer = object : CountDownTimer(FUTURE_TIME, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            leftBandTensionTime = leftBandTensionTime + 1
                            totalBandTensionTime = totalBandTensionTime + 1;
                        }

                        override fun onFinish() {

                        }
                    }.start()

            }
        } else if (isRightBandUnderTension) {

            //case when left band is only in tension
            if (this.timeUnderTensionTimer == null) {

                this.timeUnderTensionTimer = object : CountDownTimer(FUTURE_TIME, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        rightBandTensionTime = rightBandTensionTime + 1
                        totalBandTensionTime = totalBandTensionTime + 1;
                    }

                    override fun onFinish() {

                    }
                }.start()
            }
        }


        if (!isLeftBandUnderTension && !isRightBandUnderTension) {
            //No band is under tension
            //reset the timer
            //cancel the timer action
            this.timeUnderTensionTimer?.cancel()
            this.timeUnderTensionTimer = null

        }

        LitVideoPlayerSDK.timeUnderTensionObserver.postValue(
            Triple(
                this.leftBandTensionTime,
                this.rightBandTensionTime,
                this.totalBandTensionTime
            )
        )

        println("---------------------------BAND TENSION START------------------------------")
        println("Left Band Tension" + this.leftBandTensionTime)
        println("Right Band Tension" + this.rightBandTensionTime)
        println("Total Band Tension" + this.totalBandTensionTime)

        println("---------------------------BAND TENSION END------------------------------")

    }
}