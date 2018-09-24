package minesweeper.hulkdx.com.minesweeper

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import minesweeper.hulkdx.com.minesweeper.data.Board
import minesweeper.hulkdx.com.minesweeper.util.SmartTimer

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
class Game: Runnable, SurfaceHolder.Callback, View.OnTouchListener {


    companion object {
        internal const val DELAY_CLICK_MILLIS = 50
        
        internal const val OPTIONAL_FPS = 30
        internal const val DEFAULT_ROW  = 4
        internal const val DEFAULT_COL  = 5
        internal const val DEFAULT_BOMB = 5
        
        internal const val TAG = "GAME_MAIN"
    }

    private val mViewManager: ViewManager
    private var mThread: Thread? = null
    private val mSmartTimer: SmartTimer
    private val mGameLogic: GameLogic
    private val mBoard: Board
    private var mRunningThread = false
    private var mStopped       = false
    private var mLastTimeMsClicked = 0L

    //
    // Constructors
    //


    constructor(mainActivity: MainActivity,
                num_row:  Int = DEFAULT_ROW,
                num_col:  Int = DEFAULT_COL,
                num_bomb: Int = DEFAULT_BOMB)
    {
        mBoard = Board(num_row, num_col, num_bomb, mainActivity)

        mViewManager      = ViewManager(mainActivity, mBoard)
        mViewManager.getMainView().holder.addCallback(this)
        mSmartTimer      = SmartTimer()
        mGameLogic       = GameLogic(mBoard)
    }

    // 
    // Game Loop: 
    //

    /* 
        Rendering and waiting and ... logics.
     */
    override fun run() {
        while (isRunning()) {
            mSmartTimer.start()

            // Rendering:
            mViewManager.lockCanvasAndDraw()

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


    /* 
        Start the thread
     */
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

    /* 
        Stop the thread
     */
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
    // Surface callbacks:
    //

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    /* 
        onDestroy surfaceView (MainView),
     */
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        stopThread()
        mViewManager.getMainView().setOnTouchListener(null)
    }

    /* 
        onCreate surfaceView (MainView), start thread and register touch 
        listener.
        Note: Thread should be start here.
     */
    override fun surfaceCreated(holder: SurfaceHolder?) {
        startThread()
        mViewManager.getMainView().setOnTouchListener(this)
    }

    // 
    // Misc
    //

    /* 
        onDestroy MainActivity... unregistered callbacks and other resources.
     */
    fun onDestroy() {
        stopThread()
        mViewManager.getMainView().holder.removeCallback(this)
        mViewManager.getMainView().setOnTouchListener(null)
    }

    /* 
        Touching events registered by MainView.
        It will start registering when surface created. And unregistered after 
        its destroyed.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (System.currentTimeMillis() - mLastTimeMsClicked <= DELAY_CLICK_MILLIS) {
            Log.d(TAG, "Touched before ${DELAY_CLICK_MILLIS}ms, skippin.")
            return false
        }
        mLastTimeMsClicked = System.currentTimeMillis()

        val eventX: Float = event?.x ?: 0F
        val eventY: Float = event?.y ?: 0F

        val px = Board.DEFAULT_BLOCK_WIDTH_PX

        val blockX = eventX.toInt() / px
        val blockY = eventY.toInt() / px
        // println("blockX=$blockX, blockY=$blockY")

        val blockSizeX = mBoard.getBlockSizeX()
        val blockSizeY = mBoard.getBlockSizeY()

        // Check out of bound:
        if (blockX < blockSizeX && blockY < blockSizeY) {
            val block = mBoard.getBlock(blockX, blockY)
            mGameLogic.onClickBlock(block)
        }

        return false
    }
    
    // 
    // Getters / Setters:
    //

    fun getMainView() : View {
        return mViewManager.getMainView()
    }

    /* 
        Synchronized check for thread running.
     */
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
