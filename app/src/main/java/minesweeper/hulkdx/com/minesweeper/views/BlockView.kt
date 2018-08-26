package minesweeper.hulkdx.com.minesweeper.views

import android.content.Context
import android.graphics.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import minesweeper.hulkdx.com.minesweeper.MainActivity
import minesweeper.hulkdx.com.minesweeper.R
import minesweeper.hulkdx.com.minesweeper.R.attr.bitmap
import minesweeper.hulkdx.com.minesweeper.R.drawable.block
import minesweeper.hulkdx.com.minesweeper.ViewManager
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
            // Will be decoded once and won't be gced.
            val blockBitmap  = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.block)
            val zeroBitmap   = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open0)
            val oneBitmap    = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open1)    
            val twoBitmap    = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open2)   
            val threeBitmap  = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open3)  
            val fourBitmap   = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open4)  
            val fiveBitmap   = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open5)    
            val sixBitmap    = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open6)   
            val sevenBitmap  = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open7)   
            val eightBitmap  = BitmapFactory.decodeResource(context.resources,
                                                            R.drawable.open8)

            return Array(num_col) { col -> Array(num_row) {
                    BlockView(context, it, col, blockBitmap, 
                              zeroBitmap, oneBitmap, twoBitmap, threeBitmap, 
                              fourBitmap, fiveBitmap, sixBitmap, sevenBitmap, 
                              eightBitmap, pixel)
                }
            }
        }
    }
    
    // 
    // All bitmaps: (load them from createBlocks)
    // 
    private val blockBitmap: Bitmap
    private val zeroBitmap:  Bitmap
    private val oneBitmap:   Bitmap
    private val twoBitmap:   Bitmap
    private val threeBitmap: Bitmap
    private val fourBitmap:  Bitmap
    private val fiveBitmap:  Bitmap
    private val sixBitmap:   Bitmap
    private val sevenBitmap: Bitmap
    private val eightBitmap: Bitmap

    private var currentBitmap: Bitmap
    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectSprite  = Rect()
    private val rectDisplay = Rect()


    constructor(context: Context,
                arrayRow: Int = 0,
                arrayCol: Int = 0,
                blockBitmap: Bitmap,
                zeroBitmap:  Bitmap,
                oneBitmap:   Bitmap, 
                twoBitmap:   Bitmap, 
                threeBitmap: Bitmap, 
                fourBitmap:  Bitmap, 
                fiveBitmap:  Bitmap, 
                sixBitmap:   Bitmap, 
                sevenBitmap: Bitmap, 
                eightBitmap: Bitmap,
                px:    Int): super(arrayRow, arrayCol)
    {
        this.blockBitmap   = blockBitmap
        this.zeroBitmap    = zeroBitmap
        this.oneBitmap     = oneBitmap
        this.twoBitmap     = twoBitmap
        this.threeBitmap   = threeBitmap
        this.fourBitmap    = fourBitmap
        this.fiveBitmap    = fiveBitmap
        this.sixBitmap     = sixBitmap
        this.sevenBitmap   = sevenBitmap
        this.eightBitmap   = eightBitmap
        
        this.currentBitmap = blockBitmap

        rectSprite.set(0, 0, currentBitmap.width, currentBitmap.height)
        val shiftX = px * arrayRow
        val shiftY = px * arrayCol
        rectDisplay.set(shiftX, shiftY, px + shiftX, px + shiftY)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawBitmap(currentBitmap, rectSprite, rectDisplay, paint)
    }

    fun revealOnStart(viewManger: ViewManager) {
        reveal(viewManger)
        // TODO:
    }
    
    fun reveal(viewManger: ViewManager) {
        isRevealed = true

        // change bitmap based on numNeighborBombs
        when (numNeighborBombs) {
            0 -> {
                currentBitmap = zeroBitmap

                // Reveal the nearby blocks:
                for (j in col-1..col+1) {
                    for (i in row-1..row+1) {
                        if (i == row && j == col) continue
                        val neighborBlock = viewManger.getBlockOrNull(i, j) ?: continue

                        if (!neighborBlock.isRevealed) {
                            (neighborBlock as BlockView).reveal(viewManger)
                        }

                    }
                }
            }

            1 -> {
                currentBitmap = oneBitmap
            }
            
            2 -> {
                currentBitmap = twoBitmap
            }
            
            3 -> {
                currentBitmap = threeBitmap
            }
            
            4 -> {
                currentBitmap = fourBitmap
            }
            
            5 -> {
                currentBitmap = fiveBitmap
            }
            
            6 -> {
                currentBitmap = sixBitmap
            }
            
            7 -> {
                currentBitmap = sevenBitmap
            }
            
            8 -> {
                currentBitmap = eightBitmap
            }
        }

    }

}

