import java.util.*;

public class EightQueens {

    EightQueensExecutionContext executionContext = new EightQueensExecutionContext();

    public void solveUsingColumnForSelectedPositionRecurse (int rowPos, int colPos) {
        executionContext.pushRowColumnValues(rowPos, colPos);
        int selValidity = executionContext.placeQueenAndCheckSelection(rowPos, colPos);
        if (selValidity == EightQueensExecutionConstants.SELECTION_INVALID) {
            // Try next row in the same column.
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                // No more rows for this column.
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos);
                return;
            }
        } else if (selValidity == EightQueensExecutionConstants.SELECTION_VALID) {
            if (colPos == EightQueensExecutionConstants.M_SIZE-1) {
                executionContext.storeSolution();
                executionContext.popAndCopyRowColumnValues();
                // Continue for next row in last column.
                int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
                if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                    // No more rows for last column.
                    return;
                } else {
                    solveUsingColumnForSelectedPositionRecurse(nextRow, colPos);
                    return;
                }
            }
            // Continue for next column.
            int nextColPos = colPos + 1;
            if (nextColPos >= EightQueensExecutionConstants.M_SIZE) {
                // No more columns.
                return;
            }
            int firstRowForNewCol = executionContext.getFirstRowForColumn(nextColPos);
            solveUsingColumnForSelectedPositionRecurse(firstRowForNewCol, nextColPos);
            // Here because of back-tracking. Continue with next row in the same column.
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                // No more rows. Back track more.
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos);
                return;
            }
        }
    }

    public void solveUsingColumnForSelectedPosition () {
        int firstColPos = 1; // Inner processing from 2 to 8 columns.
        int firstRowPos = executionContext.getFirstRowForColumn(firstColPos);
        solveUsingColumnForSelectedPositionRecurse(firstRowPos, firstColPos);
    }

    public HashSet<Integer> solve (int rowStart, int rowEnd) {
        int colPos = 0; // Outer loop places queen in first column of any row.
        for (int rowPos = rowStart; rowPos < rowEnd+1; rowPos++) {
            executionContext.pushRowColumnValues(rowPos, colPos);
            executionContext.placeQueenAndCheckSelection(rowPos, colPos);
            solveUsingColumnForSelectedPosition();
            executionContext.reset();
        }

        HashSet<Integer> solutionSet = executionContext.getSolutionSet();
        return solutionSet;
    }

    public EightQueensExecutionContext getExecutionContext () {
        return executionContext;
    }

    public HashSet<Integer> getSolutions () {
        return executionContext.getSolutionSet();
    }
}
