package minesweeper.hulkdx.com.minesweeper.util

import android.util.Log
import minesweeper.hulkdx.com.minesweeper.Game.Companion.OPTIONAL_FPS

/**
 * Created by Mohammad Jafarzadeh Rezvan on 18/08/2018.
 */
class SmartTimer {

    companion object {
        internal const val TAG = "SmartTimer"
    }
    
    private val targetTimePerFrame = 1_000L / OPTIONAL_FPS.toLong()
    private var startTime          = 0L
    private var totalTime          = 0L
    private var frameCount         = 0
    
    fun start() {
        startTime = System.currentTimeMillis()
    }
    
    fun calculateWaitTime(): Long {
        val timePassed = System.currentTimeMillis() - startTime
        return (targetTimePerFrame - timePassed)
    }

    fun calculateAverageFPS() {
        totalTime += System.currentTimeMillis() - startTime
        frameCount++
        if (frameCount == OPTIONAL_FPS) {
            val averageFPS = 1000L * frameCount / totalTime
            // Log.d(TAG, "averageFPS: $averageFPS")
            frameCount = 0
            totalTime  = 0
        }
    }
}