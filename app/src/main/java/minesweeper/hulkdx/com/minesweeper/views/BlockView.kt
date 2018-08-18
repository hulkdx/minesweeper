package minesweeper.hulkdx.com.minesweeper.views

import android.content.Context
import android.graphics.*
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel

/**
 *  Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 *  Graphical part of Block
 */
class BlockView: Block {

    companion object {
        // the value in dp, width of block equals height
        const val DEFAULT_WIDTH_DP = 30
    }

    private val spriteBitmap: Bitmap
    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectSprite  = Rect()
    private val rectDisplay = Rect()


    constructor(context: Context,
                arrayRow: Int = 0,
                arrayCol: Int = 0,
                bitmap: Bitmap): super(arrayRow, arrayCol)
    {
        spriteBitmap = bitmap

        val px = convertDpToPixel(DEFAULT_WIDTH_DP, context).toInt()
        rectSprite.set(0, 0, spriteBitmap.width, spriteBitmap.height)
        val shiftX = px * arrayRow
        val shiftY = px * arrayCol
        rectDisplay.set(shiftX, shiftY, px + shiftX, px + shiftY)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(spriteBitmap, rectSprite, rectDisplay, paint)
    }

}

