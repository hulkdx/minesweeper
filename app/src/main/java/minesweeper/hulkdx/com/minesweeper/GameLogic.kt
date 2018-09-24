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
                makeRandomBombBlocks(1, mBoard.mNumRow, mBoard.mNumCol)
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

    //
    // TODO: is there a better way of doing this?
    //
    fun makeRandomBombBlocks(num_bomb: Int, num_row: Int, num_col: Int) {

        val random = Random()

        var k     = 0
        var retry = 0

        while (k < num_bomb) {
            val randomX = random.nextInt(num_row)
            val randomY = random.nextInt(num_col)

            val block = mBoard.getBlock(randomX, randomY)
            if (!block.isBomb) {
                Log.d(Game.TAG, "added a bomb in row:$randomX, col:$randomY")
                // Update the neighbor bombs:
                for (i in randomX-1..randomX+1) {
                    for (j in randomY-1..randomY+1) {
                        if (i == randomX && j == randomY) continue
                        val uBlock = mBoard.getBlockOrNull(i, j)
                        uBlock?.increaseNeighborBombs()
                    }
                }

                block.isBomb = true
                retry        = 0
                k++
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

        mBoard.dumpBlocks()
    }
}
