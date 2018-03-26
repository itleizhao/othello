package othello;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


interface Player {
    int[] getMove(int turn);
}


public class Othello {

    public static final int NONE = 0;
    /**
     * X
     */
    public static final int BLACK = 1;
    /**
     * O
     */
    public static final int WHITE = 2;

    private Scanner scan;

    private static Logger LOGGER;

    /**
     * Direction stores the relative coordinate of all the adjacent pieces on 8 directions.
     */
    enum Direction {
        Right(0, 1), BottomRight(1, 1), Down(1, 0), BottomLeft(1, -1), Left(0, -1), UpperLeft(-1, -1), Up(-1, 0), UpperRight(-1, 1);
        int rc[] = {0, 0};

        Direction(int row, int col) {
            this.rc = new int[]{row, col};
        }

        /**
         * Given #pos as a position on the board, move one step toward the <b>Direction</b>.
         * @param pos The give position. A int[] array of size 2, where the first element is
         *            the row index, and the second is the column index.
         * @return The new position.
         */
        int[] step(int[] pos) {
            int row = pos[0] + rc[0];
            int col = pos[1] + rc[1];
            return new int[]{row, col};
        }
    }

    /**
     * The board. The two dimension int array board[rows][columns] stores the color of all the pieces on the board.
     * Value of the int element could be either 0 (NONE), 1 (BLACK), or 2(WHITE).
     */
    int[][] board;

    private int inTurn = BLACK;

    int getTurn() {
        return inTurn;
    }

    public Othello(int size) {
        init(size);

    }

    // reset the board and initialize the class
    private void init(int size) {
        // create the initial state of the game
        board = new int[size][size];
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[3][3] = WHITE;
        board[4][4] = WHITE;
        inTurn = BLACK;
        // init the logger
        LOGGER = Logger.getLogger(this.getClass().getSimpleName());
        // initialize the scanner for reading user input
        scan = new Scanner(System.in);
    }

    /**
     * Check if the target position at the specified #row index and # column index is a valid move for the #player.
     * @param row Row index.
     * @param col Column index.
     * @param player The player in play. BLACK 1 or WHITE 0.
     * @return true if the move is valid.
     */
    public boolean isValid(int row, int col, int player) {
        int[] pos = {row, col};
        if (!withInBoard(pos)) return false;
        if (!checkColor(pos, NONE)) return false;
        for (Direction direction : Direction.values()) {
            // find the bracketing piece on the direction
            int[] bracketingPos = findBracketingPiece(pos, player, direction);
            if (bracketingPos != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <b> The method contains the algorithm for the game.</b>
     * Given a target position #pos, the #player in player, and the #direction,
     * Find the position of an existing piece of the same color as the #player,
     * on the #direction where there exists at least one straight occupied line
     * of pieces of opponent color between the target position and bracketing piece.
     * @param pos The target position
     * @param player The player who is play. BLACK 1 or WHITE 2
     * @param direction The direction. See #Direction.
     * @return The position of the bracketing piece. An int[] array of size 2,
     * where the first element is the row index, and the second element is the column index.
     * Index starts from 0.
     */
    private int[] findBracketingPiece(int[] pos, int player, Direction direction) {
        pos = direction.step(pos);
        // at least one piece of opponent color
        if (withInBoard(pos) && checkColor(pos, opponent(player))) {
            // step through the line of opponent color
            int opponent = opponent(player);
            while (withInBoard(pos) && checkColor(pos, opponent)) {
                pos = direction.step(pos);
            }
            // the bracketing piece
            if (withInBoard(pos) && checkColor(pos, player)) {
                return pos;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private int opponent(int player) {
        int opponent = player == BLACK ? WHITE : BLACK;
        return opponent;
    }

    private boolean checkColor(int[] pos, int opponent) {
        return board[pos[0]][pos[1]] == opponent;
    }

    boolean withInBoard(int[] pos) {
        return pos[0] > -1 && pos[0] < board.length && pos[1] > -1 && pos[1] < board.length;
    }

    /**
     * Check if the #player has any valid moves available on the board.
     * @param player The player who is play. BLACK 1 or WHITE 2
     * @return
     */
    public boolean hasValidMoves(int player) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (isValid(row, col, player)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Make the actual move to fill the target position with the #player's color.
     * @param row Row index of the target position
     * @param col Column index of the target position
     * @param player The player who is play. BLACK 1 or WHITE 2
     */
    public void makeMove(int row, int col, int player) {
        int[] pos = {row, col};
        for (Direction direction : Direction.values()) {
            // find the bracketing piece on the direction
            int[] bracketingPos = findBracketingPiece(pos, player, direction);
            if (bracketingPos != null) {
                // fill the move targeted piece
                board[row][col] = player;
                // flip the bracketed pieces
                int[] toFlip = direction.step(new int[]{row, col});
                while (!Arrays.equals(toFlip, bracketingPos)) {
                    board[toFlip[0]][toFlip[1]] = player;
                    toFlip = direction.step(toFlip);
                }
            }
        }
    }

    /**
     * Get a random position that is valid for the player who is in #turn to play.
     * <b> Call hasValidMoves before this method </b> to ensure there are valid moves for the player.
     * Otherwise, the method may throw NoSuchElementException.
     * @param turn
     * @return
     */
    int[] getRandomMove(int turn) {
        // get all empty position
        List<int[]> emptyPositions = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (board[row][col] == NONE) {
                    emptyPositions.add(new int[]{row,col});
                }
            }
        }
        // shuffle sort
        Collections.shuffle(emptyPositions);
        // iterate through and get the first valid move
        int[]move = emptyPositions.stream()
                .filter(xy -> isValid(xy[0], xy[1], turn))
                .findAny()
                .get();
        // pretend we are humanbeing
        String player = turn == BLACK ? "X" : "O";
        System.out.printf("Player '%s' move: %s%s%n", player, formatRow(move[0]), formatCol(move[1]));
        return move;
    }

    /**
     * Print the result of a game. Called by #playGame after the end of a game.
     */
    void printResult() {
        // count the pieces
        int black = 0;
        int white = 0;
        for (int[] row : board) {
            for (int col : row) {
                if (col == BLACK) {
                    black++;
                } else if (col == WHITE) {
                    white++;
                }
            }
        }
        // print the result
        System.out.println("No further moves available");
        if (black > white) {
            String winner = "X";
            System.out.printf("Player '%s' wins ( %d vs %d)%n", winner, black, white);
        } else if (black < white) {
            String winner = "O";
            System.out.printf("Player '%s' wins ( %d vs %d)%n", winner, white, black);
        } else if (black == white) {
            System.out.printf("Game even ( %d vs %d)%n", black, white);
        }

    }

    /**
     * Check if it is the end of game. Game ends when:
     *  a. board is full
     *  or
     *  b. there are no valid moves for both players
      * @return
     */
    boolean endOfGame() {
        // board is full
        if (boardFull(board)) return true;
        // no valid moves for both players
        if (hasValidMoves(BLACK) || hasValidMoves(WHITE)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if the board is full.
     * @param board the board
     * @return
     */
    boolean boardFull(int[][] board) {
        for (int[] row : board) {
            for (int col : row) {
                if (col == NONE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method prints the board to the console
     */
    public void printBoard() {
        int numBlacks = 0;
        int numWhites = 0;
        printHeader();
        // print each line
        for (int i = 0; i < this.board.length; i++) {
            System.out.printf("%s ",formatRow(i));
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == WHITE) {
                    System.out.printf("O");
                    numWhites++;
                } else if (this.board[i][j] == BLACK) {
                    System.out.printf("X");
                    numBlacks++;
                } else {
                    System.out.printf("-");
                }
            }
            System.out.println();
        }
        printHeader();
        printFooter(numBlacks, numWhites);

    }

    // print footer
    private void printFooter(int numBlacks, int numWhites) {
        System.out.println();
        System.out.println("Black: " + numBlacks + " - " + "White: " + numWhites);
        System.out.println();
    }

    // print header
    private void printHeader() {
        System.out.printf("  ");
        for (int i = 0; i < this.board.length; i++) {
            System.out.printf(formatCol(i));
        }
        System.out.println();
    }

    // format row index to human readable number(starting from 1)
    private String formatRow(int i) {
        return (i + 1) + "";
    }

    // format column index to human readable header
    private String formatCol(int i) {
        int first = 'a';
        char colChar = (char) (first + i);
        return String.valueOf(colChar);
    }

    int headerToCol(char c) {
        char first = 'a';
        int col = c - first;
        return col;
    }

    /**
     * Get move from a human player. The method get user input and parse the input as
     * an int[] array of size 2, where the first element is the row index, and
     * the second element is the column index. Index starts from 0.
     * @param turn BLACK 1 or WHITE 2
     * @return The move.
     */
    int[] getMove(int turn) {
        String s = getUserInput(turn);
        return parseUserInput(s);
    }

    String getUserInput(int turn) {
        String s = null;
        // promote for user input
        String player = turn == BLACK ? "X" : "O";
        System.out.printf("Player '%s' move: ", player);
        s = scan.next();
        return s;
    }

    int[] parseUserInput(String s) {
        try {
            char[] rc = s.toCharArray();
            int row = Integer.parseInt(String.valueOf(rc[0])) - 1;
            int col = headerToCol(rc[1]);

            return new int[]{row, col};
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,"Failed to parse user input. '" + s + "'.", e);
            return new int[]{-1, -1};
        }

    }

    /**
     * The major method that defines the flow of a round of the Othello game. Player #black moves first.
     * @param black The #black Player who takes first move.
     * @param white The #other player.
     */
    void playGame(Player black, Player white) {
        printBoard();
        while (true) {
            if (endOfGame()) break;
            // check who is in turn
            int turn = getTurn();
            turn = hasValidMoves(turn) ? turn : opponent(turn);

            int[] xy = {-1,-1};
            if (BLACK == turn) {
                xy = black.getMove(turn);
            } else if (WHITE == turn) {
                xy = white.getMove(turn);
            } else {
                LOGGER.warning("Unexpected 'turn' value: " + turn + " . Game ended.");
                break;
            }
            if (!isValid(xy[0], xy[1], turn)) {
                System.out.println("Invalid move. Please try again.");
                continue;
            } else {
                makeMove(xy[0], xy[1], turn);
                this.inTurn = opponent(turn);
            }
            printBoard();
        }
        printResult();
    }

    private void playGame() {
        // AI vs AI
        playGame((b-> getRandomMove(b)), (w -> getRandomMove(w)));
        // human vs AI
        init(8);
        playGame((b-> getMove(b)), (w -> getRandomMove(w)));
        // human player vs human player
        init(8);
        playGame((b-> getMove(b)), (w -> getMove(w)));
    }

    /**
     * Main method to run a round of othello
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        Othello game = new Othello(8);
        try {
            game.playGame();
        } finally {
            if (game.scan != null) {
                game.scan.close();
            }
        }
    }


}