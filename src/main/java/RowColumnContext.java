import java.util.Arrays;

public class RowColumnContext {
    private RowColumnTuple rowColumnTuple;
    private int[] values;

    public RowColumnContext(int row, int column, int[] values) {
        this.rowColumnTuple = new RowColumnTuple(row, column);
        this.values = new int[values.length];
        for (int i=0; i<values.length; i++)
            this.values[i] = values[i];
    }

    @Override
    public String toString() {
        return "{" +
                "tuple=" + rowColumnTuple.toString() +
                ", values=" + Arrays.toString(values) +
                "}\n";
    }

    public RowColumnTuple getRowColumnTuple() {
        return rowColumnTuple;
    }

    public int[] getValues() {
        return values;
    }
}
