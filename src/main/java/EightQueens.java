import java.util.*;

public class EightQueens {

    public void solveUsingColumnForSelectedPositionRecurse (int rowPos, int colPos,
                                                            EightQueensExecutionContext executionContext) {
        // Push the current selection and context.
        executionContext.pushRowColumnValues(rowPos, colPos);
        int selValidity = executionContext.placeQueenAndCheckSelection(rowPos, colPos);
        if (selValidity == EightQueensExecutionConstants.SELECTION_INVALID) {
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos, executionContext);
                return;
            }
        } else if (selValidity == EightQueensExecutionConstants.SELECTION_VALID) {
            if (colPos == EightQueensExecutionConstants.M_SIZE-1) {
                executionContext.storeSolution();
                executionContext.popAndCopyRowColumnValues();
                // Continue for next row in same column.
                int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
                if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                    return;
                } else {
                    solveUsingColumnForSelectedPositionRecurse(nextRow, colPos, executionContext);
                    return;
                }
            }
            int nextColPos = colPos + 1;
            if (nextColPos >= EightQueensExecutionConstants.M_SIZE) {
                return;
            }
            int firstRowForNewCol = executionContext.getFirstRowForColumn(nextColPos);
            solveUsingColumnForSelectedPositionRecurse(firstRowForNewCol, nextColPos, executionContext);
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos, executionContext);
                return;
            }
        }
    }

    public void solveUsingColumnForSelectedPosition (EightQueensExecutionContext executionContext) {
        int firstColPos = 1;
        int firstRowPos = executionContext.getFirstRowForColumn(firstColPos);
        solveUsingColumnForSelectedPositionRecurse(firstRowPos, firstColPos, executionContext);
    }

    public HashSet<Integer> solve () {
        EightQueensExecutionContext executionContext = new EightQueensExecutionContext();
        int colPos = 0;
        for (int rowPos = 0; rowPos < EightQueensExecutionConstants.M_SIZE; rowPos++) {
            executionContext.pushRowColumnValues(rowPos, colPos);
            executionContext.placeQueenAndCheckSelection(rowPos, colPos);
            solveUsingColumnForSelectedPosition(executionContext);
            executionContext.reset();
        }

        HashSet<Integer> solutionSet = executionContext.getSolutionSet();
//        for (Integer sol : solutionSet) {
//            System.out.printf("%d \n", sol);
//        }
        return solutionSet;
    }
}
