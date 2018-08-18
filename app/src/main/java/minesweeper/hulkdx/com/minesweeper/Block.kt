package minesweeper.hulkdx.com.minesweeper

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.RequiresApi
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import minesweeper.hulkdx.com.minesweeper.util.convertDpToPixel
import minesweeper.hulkdx.com.minesweeper.util.safeLet

/**
 * Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 * This class represent empty block to be shown to the canvas
 */
class Block {

    companion object {
        // the value in dp, width of block equals height
        const val DEFAULT_WIDTH_DP = 30
    }

    var spriteBitmap: Bitmap? = null

    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectSprite  = Rect()
    private val rectDisplay = Rect()

    constructor(context: Context,
                arrayRow: Int = 0,
                arrayCol: Int = 0)
    {
        init(context, null, arrayRow, arrayCol)
    }

    private fun init(context: Context,
                     attrs: AttributeSet?,
                     arrayRow: Int,
                     arrayCol: Int) {

        // Attrs this class can be extended from Views and use Atts if needed:
        /* if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.Block)

            val refSprite = ta.getResourceId(0, -15)
            if (refSprite != -15) {
                val drawable = ResourcesCompat.getDrawable(context.resources, refSprite, null)
                if (drawable is BitmapDrawable) {
                    spriteBitmap = drawable.bitmap
                }
            }
            ta.recycle()
        } */

        // TODO: DO NOT LOAD BITMAP FOR EVERY blocks!!
        // load default bitmap
        if (spriteBitmap == null) {
            spriteBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.block)
        }

        val px = convertDpToPixel(DEFAULT_WIDTH_DP, context).toInt()
        rectSprite.set(0, 0, spriteBitmap!!.width, spriteBitmap!!.height)
        val shiftX = px * arrayRow
        val shiftY = px * arrayCol
        rectDisplay.set(shiftX, shiftY, px + shiftX, px + shiftY)
    }


    fun draw(canvas: Canvas?) {

        safeLet(canvas, spriteBitmap) { c, b ->
            c.drawBitmap(b,
                    rectSprite,
                    rectDisplay,
                    paint)
        }

    }

}

