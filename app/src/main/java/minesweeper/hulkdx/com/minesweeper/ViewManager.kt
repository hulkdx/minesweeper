package minesweeper.hulkdx.com.minesweeper

import android.graphics.BitmapFactory
import android.graphics.Canvas
import minesweeper.hulkdx.com.minesweeper.views.Block
import minesweeper.hulkdx.com.minesweeper.views.BlockView
import minesweeper.hulkdx.com.minesweeper.views.MainView

/**
 * Created by Mohammad Jafarzadeh Rezvan on 18/08/2018.
 */
class ViewManager {

    private val mMainView: MainView
    private val mBlocks: Array<Array<BlockView>>


    constructor(mainActivity: MainActivity,
                num_row:  Int,
                num_col:  Int)
    {
        val blockBitmap  = BitmapFactory.decodeResource(mainActivity.resources,
                                                        R.drawable.block)
        mMainView        = MainView(mainActivity)
        mBlocks          = Array(num_col) {
            col -> Array(num_row) {
                BlockView(mainActivity, it, col, blockBitmap)
            }
        }
    }

    fun lockCanvasAndDraw() {
        var canvas: Canvas? = null
        try {
            synchronized(mMainView.holder) {
                canvas = mMainView.holder.lockCanvas()
                if (canvas != null) {

                    //
                    // Draw views here:
                    //

                    // Draw MainView:
                    mMainView.draw(canvas)
                    // Draw Blocks:
                    for (y in mBlocks) {
                        for (x in y) {
                            x.draw(canvas)
                        }
                    }
                }
            }
        } finally {
            if (canvas != null) {
                try {
                    mMainView.holder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getMainView(): MainView {
        return mMainView
    }

    fun getBlock(x: Int, y: Int): Block {
        return mBlocks[y][x]
    }
}