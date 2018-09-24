package minesweeper.hulkdx.com.minesweeper.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import minesweeper.hulkdx.com.minesweeper.Game
import minesweeper.hulkdx.com.minesweeper.util.BitmapHolder
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel
import minesweeper.hulkdx.com.minesweeper.views.BlockView
import java.util.*

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/09/2018.
 */

@Suppress("UNCHECKED_CAST" /*, "unused"*/)
class Board(val mNumRow: Int,
            val mNumCol: Int,
            val mNumBomb: Int,
            context: Context?) {

    companion object {
        const val DEFAULT_BLOCK_WIDTH_DP = 30
              var DEFAULT_BLOCK_WIDTH_PX = 0
    }

//    val defaultBlockWidthPx: Int

    private val mBlocks:       Array<Array<BlockView>>
    private var mBitmapHolder: BitmapHolder? = null

    init {
        // rules based on a website:
        // width: 8-100
        // height: 8-100
        // bombs: 1-1/3 squares
        if (mNumRow  !in 8..100 ||
            mNumCol  !in 8..100 ||
            mNumBomb !in 1..(mNumRow*mNumCol/3))
        {
            throw Exception("wrong argument!")
        }

        if (context != null) {
            DEFAULT_BLOCK_WIDTH_PX = convertDpToPixel(DEFAULT_BLOCK_WIDTH_DP, 
                                                      context).toInt()
            mBitmapHolder = BitmapHolder(context)
            mBlocks       = BlockView.createBlocks(mNumCol,
                                                   mNumRow,
                                                   DEFAULT_BLOCK_WIDTH_PX,
                                                   mBitmapHolder!!.blockBitmap)


        }
        // For testing context is null
        else {
            mBlocks       = BlockView.createBlocks(mNumCol,
                                                   mNumRow,
                                                   DEFAULT_BLOCK_WIDTH_PX,
                                                   null)
        }
        makeRandomBombBlocks()
    }

    fun getAllBlockViews(): Array<Array<BlockView>> {
        return mBlocks
    }


    fun getBlock(x: Int, y: Int): Block {
        return mBlocks[y][x]
    }

    fun getBlockOrNull(x: Int, y: Int): Block? {
        val col = mBlocks.getOrNull(y)

        return col?.getOrNull(x)
    }

    fun getBlockSizeX() : Int {
        return mBlocks[0].size
    }

    fun getBlockSizeY() : Int {
        return mBlocks.size
    }

    //
    // Debug purposes:
    //

    fun dumpBlocks() {
        println("_______dumpBlocks()_______")
        for (j in 0 until getBlockSizeY()) {
            println("____________col:$j")
            for (i in 0 until getBlockSizeX()) {
                val block = getBlock(i, j)
                print("___row:${i}__|")
                block.dump()
            }
        }
        println("_______dumpBlocks().exit_______")
    }

    fun reveal(blockView: BlockView) {
        blockView.isRevealed = true

        // change bitmap based on numNeighborBombs
        setBitmapForBlockView(blockView)

        if (blockView.numNeighborBombs == 0) {
            val col = blockView.col
            val row = blockView.row

            // Reveal the nearby blocks:
            for (j in col-1..col+1) {
                for (i in row-1..row+1) {
                    if (i == row && j == col) continue
                    val neighborBlock = getBlockOrNull(i, j) ?: continue

                    if (!neighborBlock.isRevealed) {
                        reveal(neighborBlock as BlockView)
                    }

                }
            }
        }

    }

    fun makeRandomBombBlocks() {
        makeRandomBombBlocks(mNumBomb, mNumRow, mNumCol)
    }

    fun makeRandomBombBlocks(num_bomb: Int, num_row: Int, num_col: Int) {

        val random = Random()

        var k     = 0
        var retry = 0

        while (k < num_bomb) {
            val randomX = random.nextInt(num_row)
            val randomY = random.nextInt(num_col)

            val block = getBlock(randomX, randomY)
            if (!block.isBomb) {
                Log.d(Game.TAG, "added a bomb in row:$randomX, col:$randomY")
                // Update the neighbor bombs:
                for (i in randomX-1..randomX+1) {
                    for (j in randomY-1..randomY+1) {
                        if (i == randomX && j == randomY) continue
                        val uBlock = getBlockOrNull(i, j)
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

//        dumpBlocks()
    }

    private fun setBitmapForBlockView(blockView: BlockView) {
        var bitmap: Bitmap? = null
        when (blockView.numNeighborBombs) {
            0 -> {
                bitmap = mBitmapHolder?.zeroBitmap
            }

            1 -> {
                bitmap = mBitmapHolder?.oneBitmap
            }

            2 -> {
                bitmap = mBitmapHolder?.twoBitmap
            }

            3 -> {
                bitmap = mBitmapHolder?.threeBitmap
            }

            4 -> {
                bitmap = mBitmapHolder?.fourBitmap
            }

            5 -> {
                bitmap = mBitmapHolder?.fiveBitmap
            }

            6 -> {
                bitmap = mBitmapHolder?.sixBitmap
            }

            7 -> {
                bitmap = mBitmapHolder?.sevenBitmap
            }

            8 -> {
                bitmap = mBitmapHolder?.eightBitmap
            }
        }
        if (bitmap != null)
            blockView.currentBitmap = bitmap
    }
}
