package othello;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

interface Player {
    int[] getMove(int turn, int[][] board);

}


class RandomAI implements Player {

    private Othello othello;

    RandomAI(Othello othello) {
        this.othello = othello;
    }
    /**
     * Get a random position that is valid for the player who is in #turn to play.
     * <b> Call hasValidMoves before this method </b> to ensure there are valid moves for the player.
     * Otherwise, the method may throw NoSuchElementException.
     * @param othello
     * @param turn
     * @return
     */
    static int[] getRandomMove(Othello othello, int turn, int[][] board) {
        // get all empty position
        List<int[]> emptyPositions = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (board[row][col] == Othello.NONE) {
                    emptyPositions.add(new int[]{row,col});
                }
            }
        }
        // shuffle sort
        Collections.shuffle(emptyPositions);
        // iterate through and get the first valid move
        int[]move = emptyPositions.stream()
                .filter(xy -> othello.isValid(xy[0], xy[1], turn, board))
                .findAny()
                .get();
        // pretend we are humanbeing
        String player = turn == Othello.BLACK ? "X" : "O";
        System.out.printf("Player '%s' move: %s%s%n", player, othello.formatRow(move[0]), othello.formatCol(move[1]));
        return move;
    }

    @Override
    public int[] getMove(int turn, int[][] board) {

        return getRandomMove(othello, turn, board);
    }


}
