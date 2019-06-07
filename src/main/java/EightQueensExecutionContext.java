import java.util.*;

public class EightQueensExecutionContext {
    private int[] rows = {0,0,0,0,0,0,0,0};
    private HashSet<Integer> solutionSet;

    private Stack<RowColumnContext> rowColumnContexts;

    public EightQueensExecutionContext() {
        this.rowColumnContexts = new Stack<RowColumnContext>();
        this.solutionSet = new HashSet<Integer>();
    }

    static int power(int x, int y)
    {
        if (y == 0)
            return 1;
        else if (y % 2 == 0)
            return power(x, y / 2) * power(x, y / 2);
        else
            return x * power(x, y / 2) * power(x, y / 2);
    }

    private Integer getSolutionFromStack() {
        ListIterator<RowColumnContext> listIterator = rowColumnContexts.listIterator();
        int solution = 0;
        int mfactor = EightQueensExecutionConstants.M_SIZE-1;
        while (listIterator.hasNext()) {
            RowColumnContext context = listIterator.next();
            RowColumnTuple tuple = context.getRowColumnTuple();
            solution = solution + ((tuple.getColumn()+1) * power(10, (mfactor - tuple.getRow())));
        }

        return solution;
    }

    private void resetRowValues () {
        for (int i=0; i<rows.length; i++)
            rows[i] = 0;
    }

    private void copyRowValues (int[] values) {
        for (int i=0; i<values.length; i++)
            rows[i] = values[i];
    }

    private void clearRowColumnValues() {
        rowColumnContexts.clear();
    }

    private int getColumnMaskForCurrentRow(int row, int col, int currRow) {
        int colMask = 0;
        int distance = 0;

        // Mask the selected column first
        colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << col);

        // Find distance of current row from selected row
        if (row > currRow) {
            distance = row - currRow;
        } else if (row < currRow) {
            distance = currRow - row;
        } else {
            System.out.printf("getColumnMaskForCurrentRow: Invalid input. Selected and current rows are same.\n");
            System.exit(1);
        }

        // Set the right column
        if ((col + distance) <= 7) {
            colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << (col + distance));
        }

        // Set the left column
        if (col >= distance) {
            colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << (col - distance));
        }

        return colMask;
    }

    public int placeQueenAndCheckSelection (int selRowPos, int selColPos) {
        // All positions except selected row position should be marked
        int rowMask = EightQueensExecutionConstants.POS_SET_MASK << selColPos;
        rowMask = ~rowMask;
        rowMask = EightQueensExecutionConstants.INT_CLEAR_MASK & rowMask;
        rows[selRowPos] = rows[selRowPos] | rowMask;

        int checkCols = EightQueensExecutionConstants.ALL_POS_MASK;
        // Loop through the rows to mark the rest of the excluded positions
        for (int rPos = 0; rPos < EightQueensExecutionConstants.M_SIZE; rPos ++) {
            if (rPos != selRowPos) {
                int colMask = getColumnMaskForCurrentRow(selRowPos, selColPos, rPos);
                rows[rPos] = rows[rPos] | colMask;
                // If all bits are set in a row then selection is invalid.
                if (rows[rPos] == EightQueensExecutionConstants.ALL_POS_MASK) {
                    return EightQueensExecutionConstants.SELECTION_INVALID;
                }
            }
            checkCols = checkCols & rows[rPos];
        }

        // If all bits are set for a column in all rows then selection is invalid.
        if (checkCols != 0) {
            return EightQueensExecutionConstants.SELECTION_INVALID;
        }

        return EightQueensExecutionConstants.SELECTION_VALID;
    }

    // Find the first row with column not marked
    public int getFirstRowForColumn (int column) {
        int mask = EightQueensExecutionConstants.POS_SET_MASK << column;
        for (int rPos = 0; rPos < EightQueensExecutionConstants.M_SIZE; rPos ++) {
            if ((rows[rPos] & mask) == 0) {
                return rPos;
            }
        }
        return EightQueensExecutionConstants.ROW_INVALID;
    }

    // Find the next row with column not marked
    public int getNextRowForColumn (int column, int row) {
        int mask = EightQueensExecutionConstants.POS_SET_MASK << column;
        for (int rPos = row+1; rPos < EightQueensExecutionConstants.M_SIZE; rPos ++) {
            if ((rows[rPos] & mask) == 0) {
                return rPos;
            }
        }
        return EightQueensExecutionConstants.ROW_INVALID;
    }

    public void pushRowColumnValues(int rowPos, int colPos) {
        RowColumnContext context = new RowColumnContext(rowPos, colPos, rows);
        rowColumnContexts.push(context);
    }

    public void popAndCopyRowColumnValues() {
        RowColumnContext context = rowColumnContexts.pop();
        copyRowValues(context.getValues());
    }

    public void reset () {
        resetRowValues();
        clearRowColumnValues();
    }

    public void storeSolution() {
        Integer solution = getSolutionFromStack();
        solutionSet.add(solution);
    }

    public HashSet<Integer> getSolutionSet () { return solutionSet; }
}
