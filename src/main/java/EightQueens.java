import java.util.*;

public class EightQueens {

    public void solveUsingColumnForSelectedPositionRecurse (int rowPos, int colIdx, int[] colArr,
                                                            EightQueensExecutionContext executionContext) {
        // Push the current selection and context.
        executionContext.pushRowColumnValues(rowPos, colArr[colIdx]);
        int selValidity = executionContext.markPositionsAndCheckSelection(rowPos, colArr[colIdx]);
        if (selValidity == EightQueensExecutionConstants.SELECTION_INVALID) {
//            System.out.printf("SELECTION_INVALID\n");
            executionContext.popAndCopyRowColumnValues();
            int nextRow = executionContext.getNextRowForColumn(colArr[colIdx], rowPos);
            if (nextRow == EightQueensExecutionConstants.ROW_INVALID) {
                return;
            } else {
                solveUsingColumnForSelectedPositionRecurse(nextRow, colIdx, colArr, executionContext);
                return;
            }
        } else if (selValidity == EightQueensExecutionConstants.SELECTION_VALID) {
//            System.out.printf("SELECTION_VALID\n");
            if (colIdx == EightQueensExecutionConstants.M_SIZE-2) {
                executionContext.prepareAndStoreSolution();
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

    private void rotateArray(int[] cArr) {
        int i, val;
        val = cArr[0];
        for (i=0; i<cArr.length-1; i++) {
            cArr[i] = cArr[i+1];
        }
        cArr[i]=val;
    }

    public void solveUsingColumnForSelectedPosition (int colPos, int rotation, EightQueensExecutionContext executionContext) {
        int[] colArr = new int[EightQueensExecutionConstants.M_SIZE-1];
        int firstRow;

        for (int i=0; i<EightQueensExecutionConstants.M_SIZE-1; i++) {
            if (i >= colPos) {
                colArr[i] = i+1;
            } else {
                colArr[i] = i;
            }
        }
        for (int i=0; i<rotation; i++) {
            rotateArray(colArr);
        }
//        System.out.printf("Solving for column array = %s \n", Arrays.toString(colArr));
        firstRow = executionContext.getFirstRowForColumn(colArr[0]);
        solveUsingColumnForSelectedPositionRecurse(firstRow, 0, colArr, executionContext);
    }

    public void solve () {
        EightQueensExecutionContext executionContext = new EightQueensExecutionContext();
        for (int rowPos = 0; rowPos < EightQueensExecutionConstants.M_SIZE; rowPos++) {
            for (int colPos = 0; colPos < EightQueensExecutionConstants.M_SIZE; colPos++) {
                for (int rt=0; rt < EightQueensExecutionConstants.M_SIZE-1; rt++) {
                    executionContext.pushRowColumnValues(rowPos, colPos);
                    executionContext.markPositionsAndCheckSelection(rowPos, colPos);
                    solveUsingColumnForSelectedPosition(colPos, rt, executionContext);
                    executionContext.resetRowValues();
                    executionContext.emptyRowColumnValues();
                    executionContext.copySolutionSetToTotalSet();
                    executionContext.emptySolutionSet();
                }
            }
        }

        List<String> solutions = executionContext.getSolutions();
//        for (int i=0; i<solutions.size(); i++) {
//            System.out.printf("Result %d : %s \n", i, solutions.get(i));
//        }
        System.out.printf("Number of solutions found = %d \n", solutions.size());

        HashSet<String> solutionSet = executionContext.getTotalSolutionSet();
//        for (String sol : solutionSet) {
//            System.out.printf("Solution : %s \n", sol);
//        }
        System.out.printf("Total number of solutions found = %d \n", solutionSet.size());
    }
}
