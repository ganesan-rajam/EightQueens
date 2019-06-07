import java.util.HashSet;
import java.util.List;

public class EightQueensRunner extends Thread {
    public EightQueens eightQueens = new EightQueens();
    private int row;
    private int column;

    public EightQueensRunner (int row, int column) {
        this.row = row;
        this.column = column;
    }

    public HashSet<Integer> getRunResults () {
        return eightQueens.getSolutions();
    }

    public void run(){
        System.out.println("Runner started for row: " + row + " column: " + column);
        eightQueens.solve(row, column);
    }
}
