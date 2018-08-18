package minesweeper.hulkdx.com.minesweeper

import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import minesweeper.hulkdx.com.minesweeper.R.drawable.block
import minesweeper.hulkdx.com.minesweeper.util.SmartTimer
import java.util.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
class Game: Runnable, SurfaceHolder.Callback, View.OnTouchListener {


    companion object {
        internal const val OPTIONAL_FPS = 30
        internal const val DEFAULT_ROW  = 4
        internal const val DEFAULT_COL  = 5
        internal const val DEFAULT_BOMB = 1
        internal const val TAG = "GAME_MAIN"
    }

    private val mViewManger: ViewManager
    private var mThread: Thread? = null
    private val mSmartTimer: SmartTimer
    private var mRunningThread = false
    private var mStopped       = false

    //
    // Constructors
    //
    
    constructor(mainActivity: MainActivity,
                num_row:  Int = DEFAULT_ROW,
                num_col:  Int = DEFAULT_COL,
                num_bomb: Int = DEFAULT_BOMB)
    {
        mViewManger      = ViewManager(mainActivity, num_row, num_col)
        mViewManger.getMainView().holder.addCallback(this)
        mSmartTimer      = SmartTimer()
        makeRandomBombBlocks(num_bomb, num_row, num_col)
    }

    // 
    // Game Loop: 
    //

    override fun run() {
        while (isRunning()) {
            mSmartTimer.start()

            // Rendering:
            mViewManger.lockCanvasAndDraw()

            // Waiting:
            val waitTime   = mSmartTimer.calculateWaitTime()
            if (waitTime > 0) {
                synchronized(this) {
                    try {
                        Thread.sleep(waitTime)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            mSmartTimer.calculateAverageFPS()
        }
        mStopped = true
    }


    private fun startThread() {
        if (isRunning()) {
            Log.e(TAG, "Thread is already running!!")
            // stop the thread here?!
            return
        }
        mThread  = Thread(this)
        setRunning(true)
        mStopped       = false
        mThread!!.start()
    }

    private fun stopThread() {
        Log.d(TAG, "stopThread")
        while (!mStopped) {
            try {
                // do we need to interrupt the thread?
                setRunning(false)
                mThread!!.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //
    // surface callbacks:
    //

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        stopThread()
        mViewManger.getMainView().setOnTouchListener(null)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        startThread()
        mViewManger.getMainView().setOnTouchListener(this)
    }

    // 
    // Misc
    //

    private fun makeRandomBombBlocks(num_bomb: Int, num_row: Int, num_col: Int) {

        val random = Random()

        var i     = 0
        var retry = 0

        while (i < num_bomb) {
            val randomX = random.nextInt(num_row)
            val randomY = random.nextInt(num_col)

            val block = mViewManger.getBlock(randomX, randomY)
            if (!block.isBomb) {
                block.isBomb = true
                i++
                retry = 0
            } else {
                retry++
                if (retry==50) {
                    throw Exception("Cannot make this much bombs!")
                }
            }
        }
    }
    
    fun onDestroy() {
        stopThread()
        mViewManger.getMainView().holder.removeCallback(this)
        mViewManger.getMainView().setOnTouchListener(null)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // TODO prevent spamming

        val eventX: Float = event?.x ?: 0F
        val eventY: Float = event?.y ?: 0F

        val px = mViewManger.defaultBlockWidthPx

        val blockX = eventX.toInt() / px
        val blockY = eventY.toInt() / px
        println("blockX=$blockX, blockY=$blockY")

        val blockSizeX = mViewManger.getBlockSizeX()
        val blockSizeY = mViewManger.getBlockSizeX()

        // Check out of bound:
        if (blockX < blockSizeX && blockY < blockSizeY) {
            val block = mViewManger.getBlock(blockX, blockY)

            // TODO should never clicks on bomb on first try: Maybe makeRandomBombBlocks() after first click?
            if (block.isBomb) {
                // TODO GameOver
            }
            else {
                // TODO
            }
        }

        return false
    }
    
    // 
    // Getters / Setters:
    //

    fun getMainView() : View {
        return mViewManger.getMainView()
    }

    private fun isRunning(): Boolean {
        synchronized(this) {
            return mRunningThread
        }
    }

    private fun setRunning(value: Boolean) {
        synchronized(this) {
            mRunningThread = value
        }
    }
}