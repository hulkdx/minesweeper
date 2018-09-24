package minesweeper.hulkdx.com.minesweeper.data

import android.content.Context
import android.graphics.Bitmap
import minesweeper.hulkdx.com.minesweeper.util.BitmapHolder
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel
import minesweeper.hulkdx.com.minesweeper.views.BlockView

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/09/2018.
 */
class Board(val mNumRow: Int,
            val mNumCol: Int,
            val mNumBomb: Int,
            context: Context?) {

    companion object {
        const val DEFAULT_BLOCK_WIDTH_DP = 30
              var DEFAULT_BLOCK_WIDTH_PX = 0
    }

//    val defaultBlockWidthPx: Int

    private val mBlocks:       Array<Array<Block>>?
    private var mBitmapHolder: BitmapHolder? = null

    init {

        if (context != null) {
            DEFAULT_BLOCK_WIDTH_PX = convertDpToPixel(DEFAULT_BLOCK_WIDTH_DP, 
                                                      context).toInt()
            mBitmapHolder = BitmapHolder(context)
            mBlocks       = BlockView.createBlocks(mNumCol,
                                                   mNumRow,
                                                   DEFAULT_BLOCK_WIDTH_PX,
                                                   mBitmapHolder!!.blockBitmap) as Array<Array<Block>>


        }
        // For testing context is null
        else {
            mBlocks = Block.createBlocks(mNumCol,
                                         mNumRow)
        }
    }

    fun getAllBlocks(): Array<Array<BlockView>> {
        return mBlocks!! as Array<Array<BlockView>>
    }


    fun getBlock(x: Int, y: Int): Block {
        return mBlocks!![y][x]
    }

    fun getBlockOrNull(x: Int, y: Int): Block? {
        val col = mBlocks!!.getOrNull(y)

        return col?.getOrNull(x)
    }

    fun getBlockSizeX() : Int {
        return mBlocks!![0].size
    }

    fun getBlockSizeY() : Int {
        return mBlocks!!.size
    }

    //
    // Debug purposes:
    //

    fun dumpBlocks() {
        println("_______dumpBlocks()_______")
        for (j in 0..getBlockSizeY()-1) {
            println("____________col:$j")
            for (i in 0..getBlockSizeX()-1) {
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
