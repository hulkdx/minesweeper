package minesweeper.hulkdx.com.minesweeper.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import minesweeper.hulkdx.com.minesweeper.Game
import minesweeper.hulkdx.com.minesweeper.util.BitmapHolder
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel
import minesweeper.hulkdx.com.minesweeper.util.makeRandomBombBlocksStub
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
            // TODO show it to users
            throw Exception("wrong argument!, row=$mNumRow, col=$mNumCol, bomb=$mNumBomb")
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
        makeRandomBombBlocks(mNumBomb)
        // makeRandomBombBlocksStub(mNumBomb, mBlocks, this)

    }

    fun getAllBlockViews(): Array<Array<BlockView>> {
        return mBlocks
    }


    fun getBlock(x: Int, y: Int): Block {
        return mBlocks[y][x]
    }

    fun getBlockOrNull(row: Int, col: Int): Block? {
        return mBlocks.getOrNull(col)?.getOrNull(row)
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
        Log.d(Game.TAG, "_______dumpBlocks()_______")
        for (j in 0 until getBlockSizeY()) {
            println("____________col:$j")
            for (i in 0 until getBlockSizeX()) {
                val block = getBlock(i, j)
                print("___row:${i}__|")
                block.dump()
            }
        }
        Log.d(Game.TAG, "_______dumpBlocks().exit_______")
    }

    fun reveal(blockView: BlockView) {
        blockView.isRevealed = true

        // change bitmap based on numNeighborBombs
        setBitmapForBlockView(blockView)

        if (blockView.numNeighborBombs == 0) {

            // Reveal the nearby blocks:
            for (neighborBlock in blockView.getNeighborList()) {
                if (!neighborBlock.isRevealed) reveal(neighborBlock as BlockView)
            }
        }

    }

    fun makeRandomBombBlocks(num_bomb: Int) {

        val blockList = mutableListOf<BlockView>()
        for (arrayBlocks in mBlocks) {
            for (b in arrayBlocks) {
                if (!b.isBomb) blockList.add(b)
            }
        }

        val random = Random()

        var k     = 0

        while (k < num_bomb) {
            val randomIndex = random.nextInt(blockList.size)

            val block = blockList[randomIndex]
            if (!block.isBomb) {
                Log.d(Game.TAG, "added a bomb in row:${block.row}, col:${block.col}")
                // Update the neighbor bombs:
                for (x in block.getNeighborList()) {
                    x.increaseNeighborBombs()
                }

                block.isBomb = true
                k++
                blockList.removeAt(randomIndex)
            }
            else {
                throw Exception("Cannot make this much bombs!")
            }
        }

        // dumpBlocks()
    }

    fun setBitmapForBlockView(blockView: BlockView) {
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
