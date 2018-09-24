package minesweeper.hulkdx.com.minesweeper

import android.util.Log
import minesweeper.hulkdx.com.minesweeper.data.Board
import minesweeper.hulkdx.com.minesweeper.data.Block
import minesweeper.hulkdx.com.minesweeper.views.BlockView
import java.util.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/09/2018.
 */
class GameLogic(private val mBoard: Board) {

    private var mFirstTimeBlockClicked = true

    companion object {
        const val TAG = "GameLogic"
    }


    /*
       On Clicking block:
     */
    fun onClickBlock(block: Block) {
        // Log.d(TAG, "onClickBlock")
        val blockView: BlockView = block as BlockView

        // Should never starts with a bomb:
        if (mFirstTimeBlockClicked) {
            Log.d(TAG, "firstTime clicked, isBomb: ${block.isBomb}, " +
                    "row:${block.row}, col:${block.col}, numBombs: ${block.numNeighborBombs}")
            mFirstTimeBlockClicked = false
            if (block.isBomb) {
                //
                // First time clicked the block is a bomb!
                // Make a new bomb and remove the bomb from this block
                //
                mBoard.makeRandomBombBlocks(1, mBoard.mNumRow, mBoard.mNumCol)
                // Set bomb false and decrease neighbor bombs
                block.isBomb = false

                for (j in block.col-1..block.col+1) {
                    for (i in block.row-1..block.row+1) {
                        if (i == block.row && j == block.col) continue
                        val neighborBlock = mBoard.getBlockOrNull(i, j)
                        neighborBlock?.decreaseNeighborBombs()
                    }
                }
                mBoard.dumpBlocks()
            }
            mBoard.reveal(blockView)
            return
        }
        if (block.isBomb) {
            gameOver()
        }
        else {
            mBoard.reveal(blockView)
            // TODO reveal 0 bombs and reveal itself:
        }
    }

    /*
        GAME OVER
     */
    private fun gameOver() {
        // TODO Render GameOver
    }
}
