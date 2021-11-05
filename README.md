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

The order in which this happens, as it turns out, does not usually matter—that is, the end result will be the same regardless of which overfull square's spots are removed first, with the exception that the winning position might differ. A player wins when all squares are the player's color.

For example, given the board on the top (N=4) in Figure 2, if Red adds a spot to square 2:1, we get the board on the bottom after all the spots stop jumping.

![image](https://user-images.githubusercontent.com/66244944/140439944-2841981f-6770-4a63-ab31-aeb1eecca91d.png)

![image](https://user-images.githubusercontent.com/66244944/140439952-dda2c675-3118-48fc-8b6d-4f660050e4fe.png)

**Figure 2.** Example of one jump creating another overfull cell. Square 2:2 becomes overfull after we add a spot to square 2:1 and perform the first jump, so we must jump again for square 2:2.

The rules hold that the game is over as soon as one player's color covers the board. This is a slightly subtle point: it is easy to set up situations where the procedure given above for dealing with overfull squares loops infinitely, swapping spots around in an endless cycle, unless one is careful to stop as soon as a winning position appears. It is acceptable, in fact, for you to report winning positions in which the redistribution procedure described above is prematurely terminated, so that some squares remain overfull.

For example, if, on Red's move, the board is as on the top of Figure 3 and Red moves to 3:1, then the board will turn entirely red. You are allowed to stop the process of redistributing spots when all squares are red, even though, depending on the order in which you do things, you could end up with the board on the bottom in the figure.

![image](https://user-images.githubusercontent.com/66244944/140439996-f55d2a1f-02d9-4872-bbf8-f972ff0e6fc4.png)

![image](https://user-images.githubusercontent.com/66244944/140440005-c218048e-4a40-41b4-a8da-233dd03a1d17.png)

**Figure 3.** The board on the top shows a position just before Red's last move (to 3:1). The board on the bottom is one possible stopping point for spot redistribution (all squares now being one color), even though the squares at 1:3 and 4:1 are still overfull.
