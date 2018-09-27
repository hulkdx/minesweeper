package minesweeper.hulkdx.com.minesweeper.views

import android.graphics.*
import minesweeper.hulkdx.com.minesweeper.data.Block

/**
 *
 *  Graphical part of Block
 */
class BlockView: Block {

    companion object {
        fun createBlocks(num_col: Int,
                         num_row: Int,
                         pixel:   Int,
                         firstBitmap: Bitmap?): Array<Array<BlockView>>
        {

            val blocks = Array(num_col) { col ->
                Array(num_row) {
                    BlockView(it, col, pixel, firstBitmap)
                }
            }

            blocks.forEach { it -> it.forEach {
                it.addNeighborBlocks(blocks)
            } }

            return blocks
        }
    }

    var currentBitmap: Bitmap?
    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectSprite  = Rect()
    private val rectDisplay = Rect()

    var isClickedBomb: Boolean = false


    constructor(arrayRow: Int = 0,
                arrayCol: Int = 0,
                px:    Int,
                firstBitmap: Bitmap?): super(arrayRow, arrayCol)
    {
        this.currentBitmap = firstBitmap

        rectSprite.set(0, 0, currentBitmap?.width ?: 0, currentBitmap?.height ?: 0)
        val shiftX = px * arrayRow
        val shiftY = px * arrayCol
        rectDisplay.set(shiftX, shiftY, px + shiftX, px + shiftY)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(currentBitmap, rectSprite, rectDisplay, paint)
    }

}

