import java.util.*;

public class EightQueensExecutionContext {
    private int[] rows = {0,0,0,0,0,0,0,0};
    private int[] colValuesPerRow = {0,0,0,0,0,0,0,0};
    private List<String> solutions;
    private HashSet<String> solutionSet;
    private HashSet<String> totalSolutionSet;

    private Stack<RowColumnContext> rowColumnContexts;

    public EightQueensExecutionContext() {
        this.rowColumnContexts = new Stack<RowColumnContext>();
        this.solutionSet = new HashSet<String>();
        this.totalSolutionSet = new HashSet<String>();
        this.solutions = new ArrayList<String>();
    }

    private String getSolutionFromStack() {
        ListIterator<RowColumnContext> listIterator = rowColumnContexts.listIterator();

        while (listIterator.hasNext()) {
            RowColumnContext context = listIterator.next();
            RowColumnTuple tuple = context.getRowColumnTuple();
            colValuesPerRow[tuple.getRow()] = tuple.getColumn();
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i< EightQueensExecutionConstants.M_SIZE; i++) {
            sb.append(colValuesPerRow[i]+1);
        }

        return sb.toString();
    }

    public void prepareAndStoreSolution() {
        String solution = getSolutionFromStack();

        if (solution == null || solution.isEmpty()) {
            System.out.printf("Invalid solution string.");
            return;
        }

        System.out.printf("---------\n");
        System.out.printf("Solution : %s\n", solution);
        System.out.printf("---------\n");
        solutions.add(solution);
        solutionSet.add(solution);
    }

    private void printRowValues () {
        System.out.printf("ROW-VALUES: ");
        for (int i=0; i<rows.length; i++) {
            System.out.printf("%s ", Integer.toBinaryString(rows[i]));
        }
        System.out.printf("\n");
    }

    public void resetRowValues () {
        for (int i=0; i<rows.length; i++)
            rows[i] = 0;
    }

    public void copyRowValues (int[] values) {
        for (int i=0; i<values.length; i++)
            rows[i] = values[i];
    }

    private int getColumnMaskForCurrentRow(int selRowPos, int selColPos, int currRowPos) {
        int colMask = 0;
        int distance = 0;

        // Mask the selected column first
        colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << selColPos);

        // Find distance of current row from selected row
        if (selRowPos > currRowPos) {
            distance = selRowPos - currRowPos;
        } else if (selRowPos < currRowPos) {
            distance = currRowPos - selRowPos;
        } else {
            System.out.printf("Invalid input. Select and Current rows are same.");
            System.exit(1);
        }

        // Set the right column
        if ((selColPos + distance) <= 7) {
            colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << (selColPos + distance));
        }

        // Set the left column
        if (selColPos >= distance) {
            colMask = colMask | (EightQueensExecutionConstants.POS_SET_MASK << (selColPos - distance));
        }

        return colMask;
    }

    public int markPositionsAndCheckSelection (int selRowPos, int selColPos) {
        // All positions except selected row position should be marked

        System.out.printf("Stack Values : %s\n", rowColumnContexts.toString());
        System.out.printf("Marking positions: row: %d, column : %d : ", selRowPos+1, selColPos+1);

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
                    System.out.printf("SELECTION_ROW_INVALID\n");
                    printRowValues();
                    return EightQueensExecutionConstants.SELECTION_INVALID;
                }
            }
            checkCols = checkCols & rows[rPos];
        }

        // If all bits are set for a column in all rows then selection is invalid.
        if (checkCols != 0) {
            System.out.printf("SELECTION_COLUMN_INVALID\n");
            System.out.printf("ROW_VALUES: %s\n", Arrays.toString(rows));
            return EightQueensExecutionConstants.SELECTION_INVALID;
        }

        System.out.printf("SELECTION_VALID\n");
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

    public void emptyRowColumnValues() {
        rowColumnContexts.empty();
    }

    public List<String> getSolutions () {
        return solutions;
    }

    public HashSet<String> getSolutionSet () { return solutionSet; }

    public HashSet<String> getTotalSolutionSet () { return totalSolutionSet; }

    public void emptySolutionSet () { solutionSet.clear(); }

    public void copySolutionSetToTotalSet () {
        for (String sol : solutionSet) {
            totalSolutionSet.add(sol);
        }
    }
}
