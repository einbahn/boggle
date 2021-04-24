import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.LinkedList;

public class BoggleSolver {

    private final Dictionary<Integer> dict = new Dictionary<Integer>();
    private BoggleBoard bb;

    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException("Constructor argument is null");
        
        for (String word : dictionary) {
            dict.put(word, 0);
        }
    }


    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException("getAllValidWords arg is null");
        this.bb = board;
        TST<Integer> result = new TST<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                StringBuilder acc = new StringBuilder();
                int[] s = { i, j };
                dfs(marked, s, acc, result);
            }
        }
        return result.keys();
    }

    private void dfs(boolean[][] marked,  int[] v, StringBuilder acc, TST<Integer> result) 
    {
        char letter = bb.getLetter(v[0], v[1]);
        int ll = 1;
        if (letter == 'Q') {
            acc.append('Q');
            acc.append('U');
            ll = 2;
        } else {
            acc.append(letter);
        }
        if (!dict.keysWithPrefix(acc.toString())) 
        {
            acc.deleteCharAt(acc.length()-ll);
            return;
        } 
        if (acc.toString().length() > 2 && dict.contains(acc.toString()) && !result.contains(acc.toString())) { 
            result.put(acc.toString(), 0); 
        }
        marked[v[0]][v[1]] = true;
        LinkedList<int[]> neighbors = neighbors(v[0], v[1], bb.rows(), bb.cols());
        for (int[] n : neighbors) {
            if (!marked[n[0]][n[1]]) {
                dfs(marked, n, acc, result);
            }
        }
        marked[v[0]][v[1]] = false;
        acc.deleteCharAt(acc.length()-ll);
    }

    public int scoreOf(String word) {
        if (word == null)
            throw new IllegalArgumentException("scoreOf received null argument");
        if (dict.contains(word)) {
            if (word.length() == 3 || word.length() == 4)
                return 1;
            else if (word.length() == 5)
                return 2;
            else if (word.length() == 6)
                return 3;
            else if (word.length() == 7)
                return 5;
            else if (word.length() >= 8)
                return 11;
        }
        return 0;
    }

    private LinkedList<int[]> neighbors(int x, int y, int row, int col) {
        LinkedList<int[]> result = new LinkedList<>();
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if ((x > -1 && row > x) && (y > -1 && col > y) && (x != i || y != j) && (i >= 0 && row > i)
                        && (j >= 0 && col > j)) {
                    result.add(new int[] { i, j });
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        // In in = new In(args[0]);
        // String[] dictionary = in.readAllStrings();
        // BoggleSolver solver = new BoggleSolver(dictionary);
        // long t = System.currentTimeMillis();
        // long end = t + 1000;
        // int n = 0;
        // while (System.currentTimeMillis() < end) {
        //     BoggleBoard board = new BoggleBoard();
        //     solver.getAllValidWords(board);
        //     n++;
        // }
        // StdOut.println("Score = " + n);
    }
}
