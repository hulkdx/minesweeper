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

    public Board createBoard(int row, int col, int bomb) {
        return new Board(row, col, bomb, null);
    }

}
