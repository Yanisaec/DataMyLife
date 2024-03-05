import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CSVManagerWorkout {
    private static List<String> exercisesList = new ArrayList<>();
    private static List<String> workoutTypesList = new ArrayList<>();
    private static final String WORKOUTS_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Workouts.csv";
    private static final String EXERCISES_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Exercises.csv";
    private static final String WORKOUTTYPES_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Workout_Types.csv";

    // Method to open a CSV file
    public static BufferedReader openCSVFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        return new BufferedReader(fileReader);
    }

    // Method to read data from a CSV file and return a List of String arrays
    public static List<String> readCSVFile(BufferedReader reader) throws IOException {
        List<String> data = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            for (String elt : values) {
                data.add(elt); 
            }
        }
        return data;
    }
}
