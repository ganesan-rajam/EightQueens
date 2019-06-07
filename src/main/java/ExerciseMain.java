import java.util.HashSet;
import java.util.List;

public class ExerciseMain {
    public static void main(String[] arguments) {
        EightQueens eightQueens = new EightQueens();
        long startTime = System.nanoTime();
        HashSet<String> solutionSet = eightQueens.solve();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println("Total number of solutions found: " + solutionSet.size() + " in duration(millisecs): " + duration/1000000);

        KPSol sol = new KPSol();
        startTime = System.nanoTime();
        List<Integer> solList = sol.solve();
        endTime = System.nanoTime();
        duration = (endTime - startTime);

        System.out.println("Total number of solutions found: " + solList.size() + " in duration(millisecs): " + duration/1000000);
    }
}
