# Othello part 2

*Goal:* _allow moves to be undone_

1. Add an 'undo' feature that allows moves to be undone. 
2. In addition to entering a board coordinate (e.g. 3D, f6, etc..), 'u' will undo the previous move.
3. Multiple moves can be undone.


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

Player 'X' move: 3D
1 --------
2 --------
3 ---X----
4 ---XX---
5 ---XO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'O' move: C5
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'X' move: E6
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOX---
6 ----X---
7 --------
8 --------
  abcdefgh

Player 'O' move: u
1 --------
2 --------
3 ---X----
4 ---XX---
5 --OOO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'X' move: u
1 --------
2 --------
3 ---X----
4 ---XX---
5 ---XO---
6 --------
7 --------
8 --------
  abcdefgh

Player 'O' move:
````
