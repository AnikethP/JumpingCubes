# Rules of Jump61

The game board consists of an N×N array of squares, where N>1. At any time, each square may have one of three colors: red, blue, or white (neutral), and some number of spots (as on dice). Initially, all squares are white and have one spot.

For purposes of naming squares in this description, we'll use the following notation: r:c refers to the square at row r and column c, where 1≤r,c≤N. Rows are numbered from top to bottom (top row is row 1) and columns are numbered from the left. When entering commands, we replace the colon with a space (this being easier to type).

The neighbors of a square are the horizontally and vertically adjacent squares (diagonally adjacent squares are not neighbors). We say that a square is overfull if it contains more spots than it has neighbors. Thus, the four corner squares are overfull when they have more than two spots; other squares on the edge are overfull with more than three spots; and all others are overfull with more than four spots.

There are two players, whom we'll call Red and Blue. The players each move in turn, with Red going first. A move consists of adding one spot on any square that does not have the opponent's color (so Red may add a spot to either a red or white square). A spot placed on any square colors that square with the player's color.

![image](https://user-images.githubusercontent.com/66244944/140439741-ec53c2e7-4719-475b-9dc8-fa10cedcc40a.png)

![image](https://user-images.githubusercontent.com/66244944/140439753-ea4bfc27-a90b-4b03-89f9-fb6257484261.png)

**Figure 1.** Example of two moves. The board on the top is the starting position, and the board on the bottom is after red and blue have each made a move.

After the player has moved, we repeat the following steps until no square is overfull or either all squares are red or all blue:

- Pick an overfull square.
- For each neighbor of the overfull square, move one spot out of the square and into the neighbor (even if occupied by the opposing side).
- Give each of these neighboring squares the player's color (if they don't have it already).
- The order in which this happens, as it turns out, does not usually matter—that is, the end result will be the same regardless of which overfull square's spots are removed first, with the exception that the winning position might differ. A player wins when all squares are the player's color.

For example, given the board on the top (N=4) in Figure 2, if Red adds a spot to square 2:1, we get the board on the bottom after all the spots stop jumping.
