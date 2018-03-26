# Othello

*Goal:* _Implement a playable Othello game_

Two players 'O' and 'X'

Players take alternate turns.

'X' goes first and *must* place an 'X' on the board, in such a position that there exists at least one straight (horizontal, vertical, or diagonal) occupied line of 'O's between the new 'X' and another 'X' on the board.

After placing the piece, all 'O's lying on all straight lines between the new 'X' and any existing 'X' are captured (i.e. they turn into 'X's )

Now 'O' plays. 'O' operates under the same rules, with the roles reversed: 'O' places an 'O' on the board in such a position where *at least one* 'X' is captured

Starting position
````text
1 --------
2 --------
3 --------
4 ---OX---
5 ---XO---
6 --------
7 --------
8 --------
  abcdefgh
````

Moves are specified as coordinates. Column+row or row+column (e.g. 3d or d3)

If a player cannot make a valid move (_capturing at least one of the opposing player's pieces along a straight line_), play passes back to the other player.

The game ends when either
 1. neither player can make a valid move
 2. the board is full

The player with the most pieces on the board at the end of the game wins.

For more detail: https://en.wikipedia.org/wiki/Reversi
- translate dark = 'X' and light = 'O'
- ignore the bit about the clock

## Sample output

````text
1 --------
2 --------
3 --------
4 ---OX---
5 ---XO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'X' move: 3d
1 --------
2 --------
3 ---X----
4 ---XX---
5 ---XO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'O' move: c5
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'X' move: e7
Invalid move. Please try again.

Player 'X' move: e6
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOX---
6 ----X---
7 --------
8 --------
  abcdefgh

Player 'O' move: 5f
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOOO--
6 ----X---
7 --------
8 --------
  abcdefgh
````

_Many, many moves later..._

````text
Player 'O' move: a7
1 XXXXXXXX
2 XXXXXXOX
3 XXXXXXXX
4 XXXXXXXX
5 OXXXXXXX
6 OXXXXXXX
7 OOOOXXXX
8 OXXOXXXX
  abcdefgh

No further moves available
Player 'X' wins ( 55 vs 9 )
````
