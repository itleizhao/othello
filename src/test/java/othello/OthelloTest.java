package othello;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class OthelloTest {

    private Othello othello;

    @Before
    public void setUp() {
        int size = 8;
        this.othello = new Othello(size);
    }

    @After
    public void tearDown() {
        this.othello = null;
    }

    @Test
    public void testOthello() {
        int size = 8;
        Othello othello = new Othello(size);
        // assert board size
        assertEquals("number of rows is not " + size, size, othello.board.length);
        for (int[] col : othello.board) {
            assertEquals("number of columns is not " + size, size, col.length);
        }
        // assert the initial positions
        // black r4c5, r5c4
        assertEquals("piece is not black", Othello.BLACK, othello.board[3][4]);
        assertEquals("piece is not black", Othello.BLACK, othello.board[4][3]);
        // white r4c4, r5c5
        assertEquals("piece is not white", Othello.WHITE, othello.board[3][3]);
        assertEquals("piece is not white", Othello.WHITE, othello.board[4][4]);
        othello.printBoard();
        // assert others are blank
        assertEquals("piece is not blank", Othello.NONE, othello.board[0][0]);
        // assert who is in play
        assertEquals("black is not in play", Othello.BLACK, othello.getTurn());
    }

    @Test
    public void testIsValid() {
        // assert within board
        assertFalse(this.othello.isValid(8, 0, Othello.BLACK));
//		assertTrue(this.othello.isValid(0, 0, Othello.BLACK));
        assertTrue(this.othello.withInBoard(new int[]{0, 0}));
        // assert is empty
        assertFalse(this.othello.isValid(3, 3, Othello.BLACK));
        assertTrue(this.othello.isValid(3, 2, Othello.BLACK));
        // assert bracketing opponent pieces
        // in a straight line
        assertFalse(this.othello.isValid(5, 5, Othello.BLACK));
        // is a straight line of continuous pieces
        assertFalse(this.othello.isValid(7, 4, Othello.BLACK));
        // capture at least one opponent piece
        assertFalse(this.othello.isValid(3, 5, Othello.BLACK));
    }

    @Test
    public void testHasValidMoves() {
        assertTrue(this.othello.hasValidMoves(Othello.BLACK));
    }

    @Test
    public void testMakeMove() {
        assertNotEquals("the piece r3c2 is already BLACK", Othello.BLACK, this.othello.board[3][2]);
        this.othello.printBoard();
        this.othello.makeMove(3, 2, Othello.BLACK);
        // assert the piece targeted by the move
        assertEquals("the targeted piece r3c2 is not BLACK", Othello.BLACK, this.othello.board[3][2]);
        // assert the pieces bracketed by the move
        assertEquals("the bracketed piece r3c3 is not BLACK", Othello.BLACK, this.othello.board[3][3]);
        this.othello.printBoard();
    }

    @Test
    public void testPlayGame() {
        othello.playGame((b -> othello.getRandomMove(b)), (w -> othello.getRandomMove(w)));
        assertFalse(othello.hasValidMoves(Othello.BLACK));
        assertFalse(othello.hasValidMoves(Othello.WHITE));
    }

    @Test
    public void testPrintBoard() {
        othello.printBoard();
    }

    //@Test // need user input to test. test disabled.
    public void testGetMove() {
        int[] rowCol = this.othello.getMove(Othello.BLACK);
        assertEquals(2, rowCol.length);
        System.out.println(Arrays.toString(rowCol));
    }

    @Test
    public void testParseUserInput() {
        int[] rowCol = this.othello.parseUserInput("2a");
        assertEquals(2, rowCol.length);
        int row = rowCol[0];
        assertEquals(1, row);
        int col = rowCol[1];
        assertEquals(0, col);
        // exception handling
        int[] invalid = this.othello.parseUserInput("!@#$");
        assertTrue(Arrays.equals(new int[]{-1, -1}, invalid));

    }

    @Test
    public void testHeaderToCol() {
        char[] header = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (int i = 0; i < header.length; i++) {
            assertEquals(i, this.othello.headerToCol(header[i]));
        }
    }

    @Test
    public void testPrintResult() {
        // black win
        deepFill(this.othello.board, Othello.BLACK);
        this.othello.printResult();

        // white win
        deepFill(this.othello.board, Othello.WHITE);
        this.othello.printResult();

        // even
        for (int i = 0; i < othello.board.length / 2; i++) {
            deepFill(this.othello.board[i], Othello.BLACK);
        }
        this.othello.printResult();
    }

    void deepFill(Object target, Integer value) {
        // leaf node
        if (!target.getClass().getComponentType().isArray()) {
            for (int i = 0; i < Array.getLength(target); i++) {
                Array.set(target, i, value);
            }
            // top or branch node
        } else {
            for (int i = 0; i < Array.getLength(target); i++) {
                deepFill(Array.get(target, i), value);
            }
        }
    }

    @Test
    public void testEndOfGame() {
        assertFalse(this.othello.endOfGame());
        deepFill(this.othello.board, Othello.BLACK);
        assertTrue(this.othello.endOfGame());
        // one empty position, no valid move
        this.othello.board[0][0] = Othello.NONE;
        assertTrue(this.othello.endOfGame());
        // one empty position, one valid move
        this.othello.board[0][1] = Othello.WHITE;
        assertFalse(this.othello.endOfGame());
    }

    @Test
    public void testDirection() {
        List<int[]> move =  new ArrayList(Arrays.asList(new int[][]{{0, 1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {-1, -1}, {-1, 0}, {-1, 1}}));
        Arrays.stream(Othello.Direction.values()).forEach(d -> move.removeIf(t -> Arrays.equals(t, d.step(new int[]{0,0}))));
        StringBuilder sb = new StringBuilder();
        move.forEach(rc -> sb.append(Arrays.toString(rc)).append(", "));
        assertTrue("Not all the directions are reached: " + sb.toString(), move.isEmpty());
    }
}
