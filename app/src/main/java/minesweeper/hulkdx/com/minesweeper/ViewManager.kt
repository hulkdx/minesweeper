package minesweeper.hulkdx.com.minesweeper

import android.graphics.Canvas
import minesweeper.hulkdx.com.minesweeper.data.Board
import minesweeper.hulkdx.com.minesweeper.views.MainView

/**
 * Created by Mohammad Jafarzadeh Rezvan on 18/08/2018.
 */
class ViewManager {

    private val mMainView: MainView
    private val mBoard:    Board


    constructor(mainActivity: MainActivity, board: Board)
    {
        mMainView           = MainView(mainActivity)
        mBoard              = board
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
                    val board = mBoard.getAllBlocks()
                    for (y in board) {
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
}
