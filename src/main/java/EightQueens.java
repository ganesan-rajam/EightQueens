import java.util.*;

public class EightQueens {

    public void solveUsingColumnForSelectedPositionRecurse (int rowPos, int colIdx, int[] colArr,
                                                            EightQueensExecutionContext executionContext) {
        // Push the current selection and context.
        executionContext.pushRowColumnValues(rowPos, colArr[colIdx]);
        int selValidity = executionContext.markPositionsAndCheckSelection(rowPos, colArr[colIdx]);
        if (selValidity == EightQueensExecutionConstants.SELECTION_INVALID) {
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colArr[colIdx], rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colIdx, colArr, executionContext);
                return;
            }
        } else if (selValidity == EightQueensExecutionConstants.SELECTION_VALID) {
            if (colIdx == EightQueensExecutionConstants.M_SIZE-2) {
                executionContext.prepareAndStoreSolution();
                executionContext.popAndCopyRowColumnValues();
                // Continue for next row in same column.
                int nextRow = executionContext.getNextRowForColumn(colArr[colIdx], rowPos);
                if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
//                    System.out.printf("ROW_INVALID\n");
                    return;
                } else {
//                    System.out.printf("ROW_VALID\n");
                    solveUsingColumnForSelectedPositionRecurse(nextRow, colIdx, colArr, executionContext);
                    return;
                }
            }
            int newColIdx = colIdx + 1;
            if (newColIdx >= EightQueensExecutionConstants.M_SIZE-1) {
                return;
            }
            int firstRowForNewCol = executionContext.getFirstRowForColumn(colArr[newColIdx]);
            solveUsingColumnForSelectedPositionRecurse(firstRowForNewCol, newColIdx, colArr, executionContext);
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colArr[colIdx], rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colIdx, colArr, executionContext);
                return;
            }
        }
    }

    public void solveUsingColumnForSelectedPosition (int colPos, EightQueensExecutionContext executionContext) {
        int[] colArr = new int[EightQueensExecutionConstants.M_SIZE-1];
        int firstRow;

        for (int i=0; i<EightQueensExecutionConstants.M_SIZE-1; i++) {
            if (i >= colPos) {
                colArr[i] = i+1;
            } else {
                colArr[i] = i;
            }
        }
        firstRow = executionContext.getFirstRowForColumn(colArr[0]);
        solveUsingColumnForSelectedPositionRecurse(firstRow, 0, colArr, executionContext);
    }

    public HashSet<String> solve () {
        EightQueensExecutionContext executionContext = new EightQueensExecutionContext();
        int colPos = 0;
        for (int rowPos = 0; rowPos < EightQueensExecutionConstants.M_SIZE; rowPos++) {
            executionContext.pushRowColumnValues(rowPos, colPos);
            executionContext.markPositionsAndCheckSelection(rowPos, colPos);
            solveUsingColumnForSelectedPosition(colPos, executionContext);
            executionContext.popAndCopyRowColumnValues();
            executionContext.resetRowValues();
            executionContext.emptyRowColumnValues();
            executionContext.copySolutionSetToTotalSet();
            executionContext.emptySolutionSet();
        }

        List<String> solutions = executionContext.getSolutions();
        System.out.printf("Number of solutions found = %d \n", solutions.size());

        HashSet<String> solutionSet = executionContext.getTotalSolutionSet();
        List<String> solutionList = new ArrayList<String>(solutionSet);
        Collections.sort(solutionList);
        for (String sol : solutionList) {
            System.out.printf("%s \n", sol);
        }
        System.out.printf("Total number of solutions found = %d \n", solutionSet.size());
        return solutionSet;
    }
}
