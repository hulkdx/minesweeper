package minesweeper.hulkdx.com.minesweeper;

import android.graphics.Rect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import minesweeper.hulkdx.com.minesweeper.data.Block;
import minesweeper.hulkdx.com.minesweeper.data.Board;
import minesweeper.hulkdx.com.minesweeper.views.BlockView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by Mohammad Jafarzadeh Rezvan on 24/09/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    @Before
    public void init() {
    }

    @Test
    public void testRules() {
        // row, col
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean shouldThrowException = false;
                try {
                    new Board(i, j, 1, null);
                } catch (Exception e) {
                    shouldThrowException = true;
                }
                assertTrue(shouldThrowException);
            }
        }

        // number of bombs
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                for (int x = i*j/3, len=x+50; x<len; x++) {
                    boolean shouldThrowException = false;
                    try {
                        new Board(i, j, 1, null);
                    } catch (Exception e) {
                        shouldThrowException = true;
                    }
                    assertTrue(shouldThrowException);
                }
            }
        }
    }

    @Test
    public void testBombs() {
        int maxI = 25; // change to 100 for testing all possible scenario
        for (int i = 8; i < maxI; i++) {
            for (int j = 8; j < maxI; j++) {
                for (int k = 1; k < i*j/3; k++) {
                    Board b = createBoard(j, i, k);
                    GameLogic g = new GameLogic(b);
                    BlockView[][] bv = b.getAllBlockViews();

                    int c = 0;
                    for (BlockView[] xx: bv) {
                        for (BlockView yy: xx) {
                            if (yy.isBomb()) {
                                c++;
                            }
                        }
                    }

                    assertEquals(c, k);
                }
            }
        }
    }

    @Test
    public void testNeighborList() {
        Board b = createRandomBoard();
        BlockView[][] bv = b.getAllBlockViews();
        for (BlockView[] xx: bv) {
            for (BlockView block: xx) {
                // Test neighbor
                List<Block> neighborList = block.getNeighborList();
                List<Block> actualNeighbor = new ArrayList<>();
                for (int j = block.getCol()-1; j <= block.getCol()+1; j++) {
                    for (int i = block.getRow()-1; i<= block.getRow()+1; i++) {
                        if (i == block.getRow() && j == block.getCol()) continue;
                        Block neighborBlock = b.getBlockOrNull(i, j);
                        if (neighborBlock != null) {
                            actualNeighbor.add(neighborBlock);
                        }
                    }
                }
                
                assertTrue(neighborList.containsAll(actualNeighbor));
            }
        }
    }

    @Test
    public void testClicksOnBomb() {
        Board b = createRandomBoard();
        GameLogic gameLogic = new GameLogic(b);

        // test if it clicks on first bomb
        BlockView bomb = null;
        for (BlockView[] yy: b.getAllBlockViews()) {
            for (BlockView xx: yy) {
                if (xx.isBomb()) {
                    bomb = xx;
                    break;
                }
            }
        }
        testBombsAndNeighbor(bomb, b, gameLogic);
    }

    @Test
    public void testClicksOnNonBomb() {
        Board b = createRandomBoard();
        GameLogic gameLogic = new GameLogic(b);

        // test if it clicks on first bomb
        BlockView bomb = null;
        for (BlockView[] yy: b.getAllBlockViews()) {
            for (BlockView xx: yy) {
                if (!xx.isBomb()) {
                    bomb = xx;
                    break;
                }
            }
        }
        testBombsAndNeighbor(bomb, b, gameLogic);
    }

    private void testBombsAndNeighbor(BlockView bomb, Board b, GameLogic gameLogic) {
        assertTrue(bomb != null);
        gameLogic.onClickBlock(bomb);
        // Test all of the bombs and neighbor bombs
        int numBombs = 0;
        for (BlockView[] yy: b.getAllBlockViews()) {
            for (BlockView xx: yy) {
                if (xx.isBomb()) {
                    numBombs++;
                }
                int expectedNeighborBomb = xx.getNumNeighborBombs();
                int actualNeighborBomb   = 0;
                for (Block ne: xx.getNeighborList()) {
                    if (ne.isBomb()) {
                        actualNeighborBomb++;
                    }
                }
                assertEquals(expectedNeighborBomb, actualNeighborBomb);
            }
        }
        assertEquals(numBombs, b.getMNumBomb());
    }

    private Board createBoard(int row, int col, int bomb) {
        return new Board(row, col, bomb, null);
    }

    private Board createRandomBoard() {
        Random random = new Random();
        int min = 8;
        int max = 100;
        int row = random.nextInt((max - min) + 1) + min;
        int col = random.nextInt((max - min) + 1) + min;
        int bomb = random.nextInt(row*col/3) + 1;
        return createBoard(row, col, bomb);
    }

}
