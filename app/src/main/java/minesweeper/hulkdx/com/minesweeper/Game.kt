package minesweeper.hulkdx.com.minesweeper

import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import minesweeper.hulkdx.com.minesweeper.R.drawable.block
import minesweeper.hulkdx.com.minesweeper.util.SmartTimer
import minesweeper.hulkdx.com.minesweeper.views.Block
import minesweeper.hulkdx.com.minesweeper.views.BlockView
import java.util.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/07/2018.
 */
class Game: Runnable, SurfaceHolder.Callback, View.OnTouchListener {


    companion object {
        internal const val DELAY_CLICK_MILIS = 500 // Is this too much? 
        
        internal const val OPTIONAL_FPS = 30
        internal const val DEFAULT_ROW  = 4
        internal const val DEFAULT_COL  = 5
        internal const val DEFAULT_BOMB = 2
        
        internal const val TAG = "GAME_MAIN"
    }

    private val mViewManger: ViewManager
    private var mThread: Thread? = null
    private val mSmartTimer: SmartTimer
    private var mRunningThread = false
    private var mStopped       = false
    private var mFirstTimeBlockClicked = true
    private val mNumRow:  Int
    private val mNumCol:  Int
    private val mNumBomb: Int
    private var mLastTimeMsClicked = 0L

    //
    // Constructors
    //
    
    constructor(mainActivity: MainActivity,
                num_row:  Int = DEFAULT_ROW,
                num_col:  Int = DEFAULT_COL,
                num_bomb: Int = DEFAULT_BOMB)
    {
        // TODO add these rules: 
        // rules based on one website: 
        // width: 8-100
        // height: 8-100
        // bombs: 1-1/3square
        mNumRow  = num_row
        mNumCol  = num_col
        mNumBomb = num_bomb
         
        mViewManger      = ViewManager(mainActivity, num_row, num_col)
        mViewManger.getMainView().holder.addCallback(this)
        mSmartTimer      = SmartTimer()
        makeRandomBombBlocks(num_bomb, num_row, num_col)
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
    // Game Logics:
    //
    
    /*
        On Clicking block:
     */
    private fun onClickBlock(block: Block) {
        // Log.d(TAG, "onClickBlock")
        val blockView: BlockView = block as BlockView

        // Should never starts with a bomb:
        if (mFirstTimeBlockClicked) {
            Log.d(TAG, "firstTime: $mFirstTimeBlockClicked, isBomb: ${block.isBomb}, clicked, row:${block.row}, col:${block.col}")
            mFirstTimeBlockClicked = false
            if (block.isBomb) {
                // 
                // First time clicked the block is a bomb!
                // Make a new bomb and remove the bomb from this block
                // 
                makeRandomBombBlocks(1, mNumRow, mNumCol)
                // Set bomb false and decrease neighbor bombs 
                block.isBomb = false

                for (j in block.col-1..block.col+1) {
                    for (i in block.row-1..block.row+1) {
                        if (i == block.row && j == block.col) continue
                        val neighborBlock = mViewManger.getBlockOrNull(i, j)
                        neighborBlock?.decreaseNeighborBombs()
                    }
                }
                makeRandomBombBlocks(0, mNumRow, mNumCol)
            }
            blockView.revealOnStart(mViewManger)
            return
        }
        if (block.isBomb) {
            gameOver()
        }
        else {
            blockView.reveal(mViewManger)
            // TODO reveal 0 bombs and reveal itself:
        }
    }
    
    /*  
        GAME OVER 
     */
    fun gameOver() {
        // TODO Render GameOver
    }

    //
    // Surface callbacks:
    //

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    /* 
        onDestory surfaceView (MainView), 
     */
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        stopThread()
        mViewManger.getMainView().setOnTouchListener(null)
    }

    /* 
        onCreate surfaceView (MainView), start thread and register touch 
        listener.
        Note: Thread should be start here.
     */
    override fun surfaceCreated(holder: SurfaceHolder?) {
        startThread()
        mViewManger.getMainView().setOnTouchListener(this)
    }

    // 
    // Misc
    //

    // 
    // TODO: is there a better way of doing this? 
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
                // Update the neighbor bombs:
                for (i in randomX-1..randomX+1) {
                    for (j in randomY-1..randomY+1) {
                        val block = mViewManger.getBlockOrNull(i, j)
                        block?.increaseNeighborBombs()
                    }
                }
                
                block.isBomb = true
                retry        = 0
                i++
            } 
            else {
                retry++
                // Note: this should never happens.
                // Note: another way of doing it is, remove the bomb blocks from 
                //       the array.
                if (retry == 50) {
                    throw Exception("Cannot make this much bombs!")
                }
            }
        }

        mViewManger.dumpBlocks()
    }
    
    /* 
        onDestroy MainActivity... unregistered callbacks and other resources.
     */
    fun onDestroy() {
        stopThread()
        mViewManger.getMainView().holder.removeCallback(this)
        mViewManger.getMainView().setOnTouchListener(null)
    }

    /* 
        Touching events registered by MainView.
        It will start registering when surface created. And unregistered after 
        its destroyed.
     */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (System.currentTimeMillis() - mLastTimeMsClicked <= DELAY_CLICK_MILIS) {
            Log.d(TAG, "Touched before ${DELAY_CLICK_MILIS}ms, skippin.")
            return false
        }
        mLastTimeMsClicked = System.currentTimeMillis()

        val eventX: Float = event?.x ?: 0F
        val eventY: Float = event?.y ?: 0F

        val px = mViewManger.defaultBlockWidthPx

        val blockX = eventX.toInt() / px
        val blockY = eventY.toInt() / px
        // println("blockX=$blockX, blockY=$blockY")

        val blockSizeX = mViewManger.getBlockSizeX()
        val blockSizeY = mViewManger.getBlockSizeY()

        // Check out of bound:
        if (blockX < blockSizeX && blockY < blockSizeY) {
            val block = mViewManger.getBlock(blockX, blockY)    
            onClickBlock(block)
        }

        return false
    }
    
    // 
    // Getters / Setters:
    //

    fun getMainView() : View {
        return mViewManger.getMainView()
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
