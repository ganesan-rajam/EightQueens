import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class EightQueens {

    public void solveUsingColumnForSelectedPositionRecurse (int rowPos, int colPos, int startColPos,
                                                            EightQueensExecutionContext executionContext) {
        // Push the current selection and context.
        executionContext.pushRowColumnValues(rowPos, colPos);
        int selValidity = executionContext.markPositionsAndCheckSelection(rowPos, colPos);
        if (selValidity == EightQueensExecutionConstants.SELECTION_INVALID) {
//            System.out.printf("SELECTION_INVALID\n");
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos, startColPos, executionContext);
                return;
            }
        } else if (selValidity == EightQueensExecutionConstants.SELECTION_VALID) {
//            System.out.printf("SELECTION_VALID\n");
            if (colPos == 7) {
                executionContext.prepareAndStoreSolution();
            }
            int newCol = colPos + 1;
            if (newCol == startColPos) {
                newCol = newCol + 1;
            }
            if (newCol >= EightQueensExecutionConstants.M_SIZE) {
                return;
            }
            int firstRowForNewCol = executionContext.getFirstRowForColumn(newCol);
            solveUsingColumnForSelectedPositionRecurse(firstRowForNewCol, newCol, startColPos, executionContext);
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colPos, rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colPos, startColPos, executionContext);
                return;
            }
        }
    }

    public void solveUsingColumnForSelectedPosition (int rowPos, int colPos, EightQueensExecutionContext executionContext) {
        int firstCol;
        int firstRow;

        if (colPos == 0) {
            firstCol = 1;
        } else {
            firstCol = 0;
        }
        firstRow = executionContext.getFirstRowForColumn(firstCol);

        solveUsingColumnForSelectedPositionRecurse(firstRow, firstCol, colPos, executionContext);
    }

    public void solve () {
        EightQueensExecutionContext executionContext = new EightQueensExecutionContext();
        for (int rowPos = 0; rowPos < EightQueensExecutionConstants.M_SIZE; rowPos++) {
            for (int colPos = 0; colPos < EightQueensExecutionConstants.M_SIZE; colPos++) {
                executionContext.pushRowColumnValues(rowPos, colPos);
                executionContext.markPositionsAndCheckSelection(rowPos, colPos);
                solveUsingColumnForSelectedPosition(rowPos, colPos, executionContext);
                executionContext.resetRowValues();
                executionContext.emptyRowColumnValues();
            }
        }
        List<String> solutions = executionContext.getSolutions();
        System.out.printf("Number of solutions found = %d \n", solutions.size());
        for (int i=0; i<solutions.size(); i++) {
            System.out.printf("Result %d : %s \n", i, solutions.get(i));
        }
    }
}
