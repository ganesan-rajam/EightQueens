import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class EightQueens {

    private static final int ALL_POS_MASK = 0xFF;
    private static final int POS_SET_MASK = 0x01;
    private static final int INT_CLEAR_MASK = 0x000000ff;

    private static final int SELECTION_INVALID = -1;
    private static final int SELECTION_VALID = 1;
    private static final int ROW_INVALID = -1;

    private static final int M_SIZE = 8;

    private static int[] rows;
    private List<String> results = new ArrayList<String>();
    private Stack<RowColumnContext> rowColumnContexts;
    int[] colListPerRow = {0,0,0,0,0,0,0,0};


    public EightQueens() {
        rowColumnContexts = new Stack<RowColumnContext>();
        this.rows = new int[M_SIZE];
    }

    private void storeResultFromStack() {
        ListIterator<RowColumnContext> listIterator = rowColumnContexts.listIterator();
        while (listIterator.hasNext()) {
            RowColumnContext context = listIterator.next();
            RowColumnTuple tuple = context.getRowColumnTuple();
            colListPerRow[tuple.getRow()] = tuple.getColumn();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i< M_SIZE; i++) {
            sb.append(colListPerRow[i]+1);
        }
        results.add(sb.toString());
    }

    private void resetRowValues (int[] rows) {
        for (int i=0; i<rows.length; i++)
            rows[i] = 0;
    }

    private void copyRowValues (int[] values) {
        for (int i=0; i<values.length; i++)
            rows[i] = values[i];
    }

    private int getColumnMaskForCurrentRow(int selRowPos, int selColPos, int currRowPos) {
        int colMask = 0;
        int distance = 0;

        // Mask the selected column first
        colMask = colMask | (POS_SET_MASK << selColPos);

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
            colMask = colMask | (POS_SET_MASK << (selColPos + distance));
        }

        // Set the left column
        if (selColPos >= distance) {
            colMask = colMask | (POS_SET_MASK << (selColPos - distance));
        }

        return colMask;
    }

    private int markPositionsAndCheckSelections (int selRowPos, int selColPos) {
        // All positions except selected row position should be marked
//        System.out.printf("Marking positions for row: %d column: %d \n", selRowPos, selColPos);

        int rowMask = POS_SET_MASK << selColPos;
        rowMask = ~rowMask;
        rowMask = INT_CLEAR_MASK & rowMask;
        rows[selRowPos] = rows[selRowPos] | rowMask;

        // Loop through the rows to mark the rest of the excluded positions
        for (int rPos = 0; rPos < M_SIZE; rPos ++) {
            if (rPos != selRowPos) {
                int colMask = getColumnMaskForCurrentRow(selRowPos, selColPos, rPos);
                rows[rPos] = rows[rPos] | colMask;
                // If all bits are set in a row then selection is invalid.
                if (rows[rPos] == ALL_POS_MASK) {
//                    System.out.printf("Row Invalid ");
//                    printRowValues();
                    return SELECTION_INVALID;
                }
            }
        }
        // If all bits are set in a column then selection is invalid.
        int checkRows = rows[0];
        for (int rPos = 1; rPos < M_SIZE; rPos ++) {
            checkRows = checkRows & rows[rPos];
        }
        if (checkRows != 0) {
//            System.out.printf("Column Invalid ");
//            printRowValues();
            return SELECTION_INVALID;
        }

        return SELECTION_VALID;
    }

    // Find the first row with column not marked
    private int getFirstRowForColumn (int column) {
        int mask = POS_SET_MASK << column;
        for (int rPos = 0; rPos < M_SIZE; rPos ++) {
            if ((rows[rPos] & mask) == 0) {
                return rPos;
            }
        }
        return ROW_INVALID;
    }

    // Find the next row with column not marked
    private int getNextRowForColumn (int column, int row) {
        int mask = POS_SET_MASK << column;
        for (int rPos = row+1; rPos < M_SIZE; rPos ++) {
            if ((rows[rPos] & mask) == 0) {
                return rPos;
            }
        }
        return ROW_INVALID;
    }

    public void solveForSelectedPositionRecurse (int rowPos, int colPos, int startColPos) {
        // Push the current selection and context.
        RowColumnContext context = new RowColumnContext(rowPos, colPos, rows);
        rowColumnContexts.push(context);
//        printRowValues();
        int selValidity = markPositionsAndCheckSelections(rowPos, colPos);
        if (selValidity == SELECTION_INVALID) {
            context = rowColumnContexts.pop();
            copyRowValues(context.getValues());
            int nextRow = getNextRowForColumn(colPos, rowPos);
            if (nextRow == ROW_INVALID) {
                return;
            } else {
                solveForSelectedPositionRecurse(nextRow, colPos, startColPos);
                return;
            }
        } else if (selValidity == SELECTION_VALID) {
//            System.out.println("Stack : " + rowColumnContexts);
            if (colPos == 7) {
                storeResultFromStack();
            }
            int newCol = colPos + 1;
            if (newCol == startColPos) {
                newCol = newCol + 1;
            }
            if (newCol >= M_SIZE) {
                return;
            }
            int firstRowForNewCol = getFirstRowForColumn(newCol);
            solveForSelectedPositionRecurse(firstRowForNewCol, newCol, startColPos);
            context = rowColumnContexts.pop();
            copyRowValues(context.getValues());
            int nextRow = getNextRowForColumn(colPos, rowPos);
            if (nextRow == ROW_INVALID) {
                return;
            } else {
                solveForSelectedPositionRecurse(nextRow, colPos, startColPos);
                return;
            }
        }
    }

    public void solveForSelectedPosition (int rowPos, int colPos) {
        int firstCol;
        int firstRow;

        if (colPos == 0) {
            firstCol = 1;
        } else {
            firstCol = 0;
        }
        firstRow = getFirstRowForColumn(firstCol);

        solveForSelectedPositionRecurse(firstRow, firstCol, colPos);
    }

    public void solve () {
//        Outer most loop for rows and columns to set the first starting position.
        for (int rowPos = 0; rowPos < M_SIZE; rowPos++) {
            for (int colPos = 0; colPos < M_SIZE; colPos++) {
                // Push the current selection and context.
                RowColumnContext context = new RowColumnContext(rowPos, colPos, rows);
                rowColumnContexts.push(context);
                markPositionsAndCheckSelections(rowPos, colPos);
                solveForSelectedPosition(rowPos, colPos);
                resetRowValues(rows);
                rowColumnContexts.empty();
            }
        }
        System.out.printf("Number of result found = %d \n", results.size());
        for (int i=0; i<results.size(); i++) {
            System.out.printf("Result %d : %s \n", i, results.get(i));
        }
    }

    private void printRowValues() {
        System. out. printf("RowValues: ");
        for (int rPos = 0; rPos < M_SIZE; rPos ++) {
            System.out.printf("%s ", Integer.toBinaryString(rows[rPos]));
        }
        System. out. printf("\n");
    }
}
