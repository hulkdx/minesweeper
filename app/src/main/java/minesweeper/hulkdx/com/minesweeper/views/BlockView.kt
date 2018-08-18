package minesweeper.hulkdx.com.minesweeper.views

import android.content.Context
import android.graphics.*
import minesweeper.hulkdx.com.minesweeper.MainActivity
import minesweeper.hulkdx.com.minesweeper.R
import minesweeper.hulkdx.com.minesweeper.R.attr.bitmap
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel

/**
 *  Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 *  Graphical part of Block
 */
class BlockView: Block {

    companion object {
        fun createBlocks(context: Context,
                         num_col: Int,
                         num_row: Int,
                         pixel: Int): Array<Array<BlockView>>
        {
            val blockBitmap  = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.block)

            return Array(num_col) { col -> Array(num_row) {
                    BlockView(context, it, col, blockBitmap, pixel)
                }
            }
        }
    }

    private val spriteBitmap: Bitmap
    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectSprite  = Rect()
    private val rectDisplay = Rect()


    constructor(context: Context,
                arrayRow: Int = 0,
                arrayCol: Int = 0,
                bitmap: Bitmap,
                px:    Int): super(arrayRow, arrayCol)
    {
        spriteBitmap = bitmap

        rectSprite.set(0, 0, spriteBitmap.width, spriteBitmap.height)
        val shiftX = px * arrayRow
        val shiftY = px * arrayCol
        rectDisplay.set(shiftX, shiftY, px + shiftX, px + shiftY)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(spriteBitmap, rectSprite, rectDisplay, paint)
    }

}

