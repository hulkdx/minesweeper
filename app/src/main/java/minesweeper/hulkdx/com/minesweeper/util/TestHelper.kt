package minesweeper.hulkdx.com.minesweeper.util

import minesweeper.hulkdx.com.minesweeper.data.Board
import minesweeper.hulkdx.com.minesweeper.views.BlockView


fun makeRandomBombBlocksStub(mNumBomb: Int, mBlocks: Array<Array<BlockView>>, board: Board) {

    val blockList = listOf(
            mBlocks[1][3],
            mBlocks[4][5],
            mBlocks[4][7],
            mBlocks[5][2],
            mBlocks[2][1],
            mBlocks[6][1],
            mBlocks[0][1],
            mBlocks[7][1],
            mBlocks[3][2])

    for (block in blockList) {
        // Update the neighbor bombs:
        for (x in block.getNeighborList()) {
            x.increaseNeighborBombs()
        }

        block.isBomb = true
    }

//    for (xx in mBlocks) {
//        for (yy in xx) {
//            yy.isRevealed = true
//            if (!yy.isBomb)
//                board.setBitmapForBlockView(yy)
//        }
//    }
}


fun makeRandomBombBlocksStubSingle(mBlocks: Array<Array<BlockView>>, board: Board) {

    val blockList = listOf(
            mBlocks[6][0])

    for (block in blockList) {
        // Update the neighbor bombs:
        for (x in block.getNeighborList()) {
            x.increaseNeighborBombs()
        }

        block.isBomb = true
    }

    //    for (xx in mBlocks) {
    //        for (yy in xx) {
    //            yy.isRevealed = true
    //            if (!yy.isBomb)
    //                board.setBitmapForBlockView(yy)
    //        }
    //    }
}

