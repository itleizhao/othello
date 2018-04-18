package othello;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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
        othello.printBoard(othello.board);
        // assert others are blank
        assertEquals("piece is not blank", Othello.NONE, othello.board[0][0]);
        // assert who is in play
        assertEquals("black is not in play", Othello.BLACK, othello.getTurn());
    }

    @Test
    public void testIsValid() {
        // assert within board
        assertFalse(this.othello.isValid(8, 0, Othello.BLACK, this.othello.board));
//		assertTrue(this.othello.isValid(0, 0, Othello.BLACK));
        assertTrue(this.othello.withInBoard(new int[]{0, 0}, this.othello.board));
        // assert is empty
        assertFalse(this.othello.isValid(3, 3, Othello.BLACK, this.othello.board));
        assertTrue(this.othello.isValid(3, 2, Othello.BLACK, this.othello.board));
        // assert bracketing opponent pieces
        // in a straight line
        assertFalse(this.othello.isValid(5, 5, Othello.BLACK, this.othello.board));
        // is a straight line of continuous pieces
        assertFalse(this.othello.isValid(7, 4, Othello.BLACK, this.othello.board));
        // capture at least one opponent piece
        assertFalse(this.othello.isValid(3, 5, Othello.BLACK, this.othello.board));
    }

    @Test
    public void testHasValidMoves() {
        assertTrue(this.othello.hasValidMoves(Othello.BLACK, this.othello.board));
    }

    @Test
    public void testMakeMove() {
        assertNotEquals("the piece r3c2 is already BLACK", Othello.BLACK, this.othello.board[3][2]);
        this.othello.printBoard(this.othello.board);
        this.othello.makeMove(3, 2, Othello.BLACK, this.othello.board);
        // assert the piece targeted by the move
        assertEquals("the targeted piece r3c2 is not BLACK", Othello.BLACK, this.othello.board[3][2]);
        // assert the pieces bracketed by the move
        assertEquals("the bracketed piece r3c3 is not BLACK", Othello.BLACK, this.othello.board[3][3]);
        this.othello.printBoard(this.othello.board);
    }

    @Test
    public void testPlayGame() {
        othello.playGame(((b, board) -> RandomAI.getRandomMove(othello, b, board)), ((w, board) -> RandomAI.getRandomMove(othello, w, board)));
        assertFalse(othello.hasValidMoves(Othello.BLACK, othello.board));
        assertFalse(othello.hasValidMoves(Othello.WHITE, othello.board));
    }

    @Test
    public void testPrintBoard() {
        othello.printBoard(othello.board);
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
        this.othello.printResult(this.othello.board);

        // white win
        deepFill(this.othello.board, Othello.WHITE);
        this.othello.printResult(this.othello.board);

        // even
        for (int i = 0; i < othello.board.length / 2; i++) {
            deepFill(this.othello.board[i], Othello.BLACK);
        }
        this.othello.printResult(this.othello.board);
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
        assertFalse(this.othello.endOfGame(this.othello.board));
        deepFill(this.othello.board, Othello.BLACK);
        assertTrue(this.othello.endOfGame(this.othello.board));
        // one empty position, no valid move
        this.othello.board[0][0] = Othello.NONE;
        assertTrue(this.othello.endOfGame(this.othello.board));
        // one empty position, one valid move
        this.othello.board[0][1] = Othello.WHITE;
        assertFalse(this.othello.endOfGame(this.othello.board));
    }

    @Test
    public void testDirection() {
        List<int[]> move =  new ArrayList(Arrays.asList(new int[][]{{0, 1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {-1, -1}, {-1, 0}, {-1, 1}}));
        Arrays.stream(Othello.Direction.values()).forEach(d -> move.removeIf(t -> Arrays.equals(t, d.step(new int[]{0,0}))));
        StringBuilder sb = new StringBuilder();
        move.forEach(rc -> sb.append(Arrays.toString(rc)).append(", "));
        assertTrue("Not all the directions are reached: " + sb.toString(), move.isEmpty());
    }

    @Test
    public void testUndo() {
        // isUnDo flag is false by default
        assertFalse(this.othello.isUnDo);
        // maximum number of moves within a round
        assertEquals(64, this.othello.history.length);
        // init with null value
        for (int[] move : this.othello.history) {
            assertNull(move);
        }
        this.othello.makeMove(3, 2, Othello.BLACK, this.othello.board);
        this.othello.recordMove(3, 2, Othello.BLACK, this.othello.history);
        int[] firstMove = this.othello.history[0];
        assertEquals(3, firstMove.length);
        assertEquals(3, firstMove[0]); // row
        assertEquals(2,firstMove[1]); // col
        assertEquals(Othello.BLACK,firstMove[2]); // player
        assertNull(this.othello.history[1]);

        int[][] tempBoard = this.othello.createBoard(this.othello.board.length);
        this.othello.replay(this.othello.history, 0, tempBoard);
        assertTrue(Arrays.deepEquals(tempBoard, this.othello.board));

        this.othello.makeMove(2, 2, Othello.WHITE, this.othello.board);
        this.othello.recordMove(2, 2, Othello.WHITE, this.othello.history);
        int[] secondMove = this.othello.history[1];
        assertEquals(3, secondMove.length);
        assertEquals(2, secondMove[0]); // row
        assertEquals(2,secondMove[1]); // col
        assertEquals(Othello.WHITE,secondMove[2]); // player
        assertNull(this.othello.history[2]);

        this.othello.isUnDo = true;

        assertEquals(this.othello.board[2][2], Othello.WHITE); // target piece
        assertEquals(this.othello.board[3][3], Othello.WHITE); // flipped piece
        // undo 1
        int lastMovePlayer = this.othello.history[1][2];
        this.othello.undo();
        assertFalse("isUnDo flag should be reset to false after the undo",this.othello.isUnDo);
        assertNull("latest history record should be set to null", this.othello.history[1]);
        assertEquals("player in turn should be set to the player last played", this.othello.getTurn(), lastMovePlayer);
        assertEquals(this.othello.board[2][2], Othello.NONE);
        assertEquals(this.othello.board[3][3], Othello.BLACK);
        // multiple undo
        this.othello.undo();
        assertNull("latest history record should be set to null", this.othello.history[0]);
        // TODO test undo history with 64 non-null records
    }
}
