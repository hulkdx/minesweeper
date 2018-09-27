package minesweeper.hulkdx.com.minesweeper

import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import minesweeper.hulkdx.com.minesweeper.data.Board
import minesweeper.hulkdx.com.minesweeper.util.SmartTimer

class Game: Runnable, SurfaceHolder.Callback, View.OnTouchListener {


    companion object {
        internal const val LONG_CLICK_THRESHOLD_MILLIS = 500
        
        internal const val OPTIONAL_FPS = 30
        internal const val DEFAULT_ROW  = 8
        internal const val DEFAULT_COL  = 8
        internal const val DEFAULT_BOMB = 1
        
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
    private var mLastBlockPosX = -1
    private var mLastBlockPosY = -1
    private var mIgnoreTouch   = false

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
        Rendering and waiting and ...
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
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return false

        val eventX: Float = event.x
        val eventY: Float = event.y
        val px = Board.DEFAULT_BLOCK_WIDTH_PX
        // println("blockX=$blockX, blockY=$blockY")
        when (event.action) {

            MotionEvent.ACTION_UP -> {
                if (mIgnoreTouch) return false
                mIgnoreTouch = true

                // TODO add some conditions:
                val blockX = eventX.toInt() / px
                val blockY = eventY.toInt() / px

                if (mLastBlockPosX == blockX && mLastBlockPosY == blockY) {
                    val block = mBoard.getBlock(mLastBlockPosX, mLastBlockPosY)
                    if (System.currentTimeMillis() - mLastTimeMsClicked <= LONG_CLICK_THRESHOLD_MILLIS) {
                        mGameLogic.onClickBlock(block)
                    }
                    else {
                        mGameLogic.onLongClickBlock(block)
                    }
                    v?.performClick()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                // Log.d(TAG, "ACTION_DOWN")

                val blockX = eventX.toInt() / px
                val blockY = eventY.toInt() / px

                val blockSizeX = mBoard.getBlockSizeX()
                val blockSizeY = mBoard.getBlockSizeY()

                // Check out of bound:
                if (blockX < blockSizeX && blockY < blockSizeY) {
                    mLastBlockPosX = blockX
                    mLastBlockPosY = blockY
                    mLastTimeMsClicked = System.currentTimeMillis()
                    v?.postDelayed({
                        if (!mIgnoreTouch) {
                            Log.d(TAG, "postDelayed")
                            val block = mBoard.getBlock(mLastBlockPosX, mLastBlockPosY)
                            mGameLogic.onLongClickBlock(block)
                            mIgnoreTouch = true
                        }
                    }, LONG_CLICK_THRESHOLD_MILLIS.toLong())
                }
                mIgnoreTouch = false
                return true
            }
            else -> {
                if (mIgnoreTouch) return false

                val blockX = eventX.toInt() / px
                val blockY = eventY.toInt() / px
                if ( !(mLastBlockPosX == blockX && mLastBlockPosY == blockY) ) {
                    mIgnoreTouch = true
                }
            }
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
