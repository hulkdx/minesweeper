package minesweeper.hulkdx.com.minesweeper

import android.graphics.Canvas

/**
 * Created by Mohammad Jafarzadeh Rezvan on 18/08/2018.
 */
class ViewManager {

    private val mMainView: MainView
    private val mBlocks: Array<Array<Block>>


    constructor(mainActivity: MainActivity,
                num_row:  Int,
                num_col:  Int,
                num_bomb: Int)
    {
        mMainView        = MainView(mainActivity)
        mBlocks          = Array(num_col) {
            col -> Array(num_row) {
                Block(mainActivity, it, col)
            }
        }
        val x = 2
    }

    fun lockCanvasAndDraw() {
        var canvas: Canvas? = null
        try {
            synchronized(mMainView.holder) {
                canvas = mMainView.holder.lockCanvas()
                if (canvas != null) {
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
                mMainView.holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    fun getMainView(): MainView {
        return mMainView
    }
}