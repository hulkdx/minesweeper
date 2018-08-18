package minesweeper.hulkdx.com.minesweeper.views


/**
 *  Created by Mohammad Jafarzadeh Rezvan on 17/07/2018.
 *
 *  Separated logical and graphical part of Blocks:
 *
 */
open class Block {

    private val row: Int
    private val col: Int

    var isBomb  = false

    constructor(arrayRow: Int = 0,
                arrayCol: Int = 0)
    {
        row = arrayRow
        col = arrayCol
    }

}

