package minesweeper.hulkdx.com.minesweeper

import android.view.View
import minesweeper.hulkdx.com.minesweeper.Game.Companion.OPTIONAL_FPS

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
class Game: Runnable {
    
    companion object {
        internal const val OPTIONAL_FPS = 30
        internal const val DEFAULT_ROW  = 4
        internal const val DEFAULT_COL  = 5
        internal const val DEFAULT_BOMB = 1
    }

    private val mViewManger: ViewManager
    private val mThread: Thread
    private var mRunningThread = true

    //
    // Constructors
    //
    
    constructor(mainActivity: MainActivity,
                num_row:  Int = DEFAULT_ROW,
                num_col:  Int = DEFAULT_COL,
                num_bomb: Int = DEFAULT_BOMB)
    {
        mViewManger      = ViewManager(mainActivity, num_row, num_col, num_bomb)
        mThread          = Thread(this)
    }

    fun start() {
        // Starting a thread in constructor is not safe, hence this function
        mThread.start()
    }

    // 
    // Game Loop: 
    //

    override fun run() {
        val targetTimePerFrame: Long = 1_000L / OPTIONAL_FPS

        while (running()) {
            val startTime = System.currentTimeMillis()

            // Rendering:
            mViewManger.lockCanvasAndDraw()

            // Waiting:
            val timePassed = System.currentTimeMillis() - startTime
            val waitTime   = (targetTimePerFrame - timePassed)
            if (waitTime > 0) {
                synchronized(this) {
                    try {
                        Thread.sleep(waitTime)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    // 
    // Misc
    //
    
    private fun running(): Boolean {
        return mRunningThread
    }
    
    // 
    // Getters / Setters:
    //

    fun getMainView() : View {
        return mViewManger.getMainView()
    }

}