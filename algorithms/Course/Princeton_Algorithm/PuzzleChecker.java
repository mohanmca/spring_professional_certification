/******************************************************************************
 *  Compilation:  javac-algs4 PuzzleChecker.java
 *  Execution:    java-algs4 PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java-algs4 PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PuzzleChecker {

    public static void main(String[] args) {

        // testSample();

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            System.out.printf("Manhattan vs Hamming for board %d, %d :: \n", initial.manhattan(),
                              initial.hamming());
            Solver solver = new Solver(initial);
            if (solver.isSolvable()) {
                int moves = solver.moves();
                StdOut.println("Has solutions! with number of moves " + moves);
                int i = 0;
                Iterable<Board> solution = solver.solution();
                for (Board board2 : solution)
                    StdOut.println("Moves : " + (i++) + "\n" + board2.toString());
            }
            else {
                StdOut.println("No solutions!");
            }
        }
    }

    private static void testSample() {
        int[][] tiles = new int[2][2];
        tiles[0][0] = 1;
        tiles[0][1] = 2;
        tiles[1][0] = 0;
        tiles[1][1] = 3;
        Board board = new Board(tiles);
        System.out.println(board.toString());
        Solver solver = new Solver(board);
        Iterable<Board> boards = solver.solution();
        for (Board board2 : boards) {
            StdOut.println("Moves : " + board2.toString());
        }
    }
}
