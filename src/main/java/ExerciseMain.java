import java.util.HashSet;
import java.util.List;

public class ExerciseMain {
    public static void main(String[] arguments) {
        EightQueens eightQueens = new EightQueens();
        HashSet<String> mySet = eightQueens.solve();

        System.out.println("My set: " + mySet.size());
    }
}
