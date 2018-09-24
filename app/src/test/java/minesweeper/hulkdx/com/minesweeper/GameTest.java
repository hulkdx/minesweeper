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
    public void testClicks() {
        // TODO
        
    }

    private Board createBoard(int row, int col, int bomb) {
        return new Board(row, col, bomb, null);
    }

}
