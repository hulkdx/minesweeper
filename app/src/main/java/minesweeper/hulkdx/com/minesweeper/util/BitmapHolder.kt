package minesweeper.hulkdx.com.minesweeper.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import minesweeper.hulkdx.com.minesweeper.R

/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/09/2018.
 */

@Suppress("JoinDeclarationAndAssignment")
class BitmapHolder(context: Context) {

    //
    // All bitmaps: (load them from createBlocks)
    //
    val blockBitmap: Bitmap
    val zeroBitmap:  Bitmap 
    val oneBitmap:   Bitmap 
    val twoBitmap:   Bitmap 
    val threeBitmap: Bitmap 
    val fourBitmap:  Bitmap 
    val fiveBitmap:  Bitmap 
    val sixBitmap:   Bitmap 
    val sevenBitmap: Bitmap 
    val eightBitmap: Bitmap
    val bombRevealed: Bitmap
    val bombDeath:    Bitmap
    val bombFlagged:  Bitmap

    init {
        // Will be decoded once and won't be gced.
        blockBitmap  = BitmapFactory.decodeResource(context.resources,
                R.drawable.block)
        zeroBitmap   = BitmapFactory.decodeResource(context.resources,
                R.drawable.open0)
        oneBitmap    = BitmapFactory.decodeResource(context.resources,
                R.drawable.open1)
        twoBitmap    = BitmapFactory.decodeResource(context.resources,
                R.drawable.open2)
        threeBitmap  = BitmapFactory.decodeResource(context.resources,
                R.drawable.open3)
        fourBitmap   = BitmapFactory.decodeResource(context.resources,
                R.drawable.open4)
        fiveBitmap   = BitmapFactory.decodeResource(context.resources,
                R.drawable.open5)
        sixBitmap    = BitmapFactory.decodeResource(context.resources,
                R.drawable.open6)
        sevenBitmap  = BitmapFactory.decodeResource(context.resources,
                R.drawable.open7)
        eightBitmap  = BitmapFactory.decodeResource(context.resources,
                R.drawable.open8)
        bombRevealed = BitmapFactory.decodeResource(context.resources,
                R.drawable.bombrevealed)
        bombDeath    = BitmapFactory.decodeResource(context.resources,
                R.drawable.bombdeath)
        bombFlagged  = BitmapFactory.decodeResource(context.resources,
                R.drawable.bombflagged)
    }
}