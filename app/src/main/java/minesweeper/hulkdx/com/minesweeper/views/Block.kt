package minesweeper.hulkdx.com.minesweeper.views


/**
 *  Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 *  Separated logical and graphical part of Blocks. Logical part in this class
 *  Graphical in BlockView class.
 *
 */
open class Block {

    val row: Int
    val col: Int
    var numNeighborBombs: Int = 0 // TODO

    var isBomb  = false

    constructor(arrayRow: Int = 0,
                arrayCol: Int = 0)
    {
        row = arrayRow
        col = arrayCol
    }
    
    /* 
        Increase this block's neighbor bombs by 1.
     */
    fun increaseNeighborBombs() {
        numNeighborBombs += 1
    }
    
    fun decreaseNeighborBombs() {
        numNeighborBombs -= 1
    }
    
    fun reveal() {
        // TODO
    }
    

    //
    // Debug purposes:
    //
    
    fun dump() {
        /* println("row: $row, col:$col") */
        println("neighborBombs:$numNeighborBombs, isBomb: $isBomb")
    }
}

