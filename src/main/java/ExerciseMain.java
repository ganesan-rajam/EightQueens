import java.util.HashSet;

public class ExerciseMain {
    public static void main(String[] arguments) {
        long startTime;
        long endTime;
        long duration;
        HashSet<Integer> solutionSet;
        int numOfSols;

        startTime = System.nanoTime();
        EightQueensRunner er1 = new EightQueensRunner(0, 3);
        EightQueensRunner er2 = new EightQueensRunner(4, 7);
        er1.start();
        er2.start();
        try {
            er1.join();
            er2.join();
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime);

        solutionSet = er1.getRunResults();
        numOfSols = solutionSet.size();
        solutionSet = er2.getRunResults();
        numOfSols = numOfSols + solutionSet.size();

        System.out.println("Total number of solutions found: " + numOfSols + " in duration(millisecs): " + duration/1000000);
    }
}
