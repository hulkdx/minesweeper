package minesweeper.hulkdx.com.minesweeper.data

import minesweeper.hulkdx.com.minesweeper.views.BlockView


/**
 *  Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 *  Separated logical and graphical part of Blocks. Logical part in this class
 *  Graphical in BlockView class.
 *
 */
open class Block(val row: Int = 0, val col: Int = 0) {

    var numNeighborBombs = 0
    var isRevealed       = false
    var isBomb           = false
    var isGuessedBomb    = false

    private val neighborList = mutableListOf<Block>()


    /*
        Increase this block's neighbor bombs by 1.
     */
    fun increaseNeighborBombs() {
        numNeighborBombs += 1
    }
    
    fun decreaseNeighborBombs() {
        numNeighborBombs -= 1
    }

    

    //
    // Debug purposes:
    //
    
    fun dump() = println("neighborBombs:$numNeighborBombs, isBomb: $isBomb")


    override fun toString(): String =
            "row:$row col:$col bombs:$numNeighborBombs"

    fun addNeighborBlocks(allBlocks: Array<Array<BlockView>>) {
        for (i in row-1..row+1) {
            for (j in col-1..col+1) {
                if (i == row && j == col) continue
                val uBlock = allBlocks.getOrNull(j)?.getOrNull(i) ?: continue
                neighborList.add(uBlock)
            }
        }
    }

    fun getNeighborList(): List<Block> = neighborList

}

