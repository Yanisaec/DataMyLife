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

    public static String[] getAllElements(String Type) {
        String[] choiceList = new String[0];
        if (Type.equals("Workout")){
            try (BufferedReader workouts = CSVManagerWorkout.openCSVFile(WORKOUTTYPES_CSV_PATH)) {
                List<String> data = CSVManagerWorkout.readCSVFile(workouts);
                choiceList = new String[data.size()] ;
                for (int i = 0; i < data.size(); i++ ) {
                    choiceList[i] = data.get(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (e.g., show an error message)
            }
        } else if (Type.equals("Exercise")) {
            try (BufferedReader exercises = CSVManagerWorkout.openCSVFile(EXERCISES_CSV_PATH)) {
                List<String> data = CSVManagerWorkout.readCSVFile(exercises);
                choiceList = new String[data.size()] ;
                for (int i = 0; i < data.size(); i++ ) {
                    choiceList[i] = data.get(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (e.g., show an error message)
            }
        }
        return choiceList;
    }

    public static void addWorkout(String newWorkout) {
        String filePath = WORKOUTTYPES_CSV_PATH;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the existing ingredients from the file
            String line = reader.readLine();
            List<String> existingChoices = new ArrayList<>(Arrays.asList(line.split(";")));

            // Check if the new ingredient is already in the list
            if (!existingChoices.contains(newWorkout)) {
                // Add the new ingredient to the list
                existingChoices.add(newWorkout);

                // Write the updated list back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    String updatedChoices = existingChoices.stream().collect(Collectors.joining(";"));
                    writer.write(updatedChoices);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public static void addExercise(String newExercise) {
        String filePath = EXERCISES_CSV_PATH;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the existing ingredients from the file
            String line = reader.readLine();
            List<String> existingChoices = new ArrayList<>(Arrays.asList(line.split(";")));

            // Check if the new ingredient is already in the list
            if (!existingChoices.contains(newExercise)) {
                // Add the new ingredient to the list
                existingChoices.add(newExercise);

                // Write the updated list back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    String updatedChoices = existingChoices.stream().collect(Collectors.joining(";"));
                    writer.write(updatedChoices);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public static boolean isAWorkout(String workoutToTest) {
        try (BufferedReader workouts = CSVManagerWorkout.openCSVFile(WORKOUTTYPES_CSV_PATH)) {
            List<String> data = CSVManagerWorkout.readCSVFile(workouts);
            for (int i = 0; i < data.size(); i++ ) {
                workoutTypesList.add(data.get(i));
            }
            return workoutTypesList.contains(workoutToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isAnExercise(String exerciseToTest) {
        try (BufferedReader exercises = CSVManagerWorkout.openCSVFile(EXERCISES_CSV_PATH)) {
            List<String> data = CSVManagerWorkout.readCSVFile(exercises);
            for (int i = 0; i < data.size(); i++ ) {
                exercisesList.add(data.get(i));
            }
            return exercisesList.contains(exerciseToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addToWorkouts(String date, String workout, String exercise, String weight, String reps, String rest) throws CustomException{
        String filePath = WORKOUTS_CSV_PATH;
        if (!date.matches("\\d{8}")) {
            throw new CustomException("The date isn't in the right format.");
        }

        if (!isAWorkout(workout)) {
            throw new CustomException(workout + " is not a valid workout, first add it to the dictionnary.");
        }

        if (!isAnExercise(exercise)) {
            throw new CustomException(exercise + " is not a valid exercise, first add it to the dictionnary.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();

            // Read all existing lines from the file
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            // Check if there is a line with the specified date
            boolean dateExists = false;

            for (int i = 0; i < lines.size(); i++) {
                String existingLine = lines.get(i);
                String[] parts = existingLine.split(";");
                if (parts.length > 0 && parts[0].equals(date)) {
                    if (!existingLine.contains("{" + exercise + ":" + weight + ":" + reps + ":" + rest +"}")) {
                        // Line with the date already exists, append the new tuple
                        lines.set(i, existingLine + ",{" + exercise + ":" + weight + ":" + reps + ":" + rest +"}");
                        dateExists = true;
                        break;
                    }
                }
            }

            // If the date doesn't exist, create a new line
            if (!dateExists) {
                String newLine = date + ";" + workout +  ";{" + exercise + ":" + weight + ":" + reps + ":" + rest +"}";
                lines.add(newLine);
            }

            // Write the updated lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (e.g., show an error message)
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public static List<String[]> getLastWorkouts() {
        String filePath = WORKOUTS_CSV_PATH;
        List<String[]> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(";");

                if (parts.length > 0) {
                    result.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        return result;
    }

    public static void deleteLinesWithDate(String targetDate) {
        String filePath = WORKOUTS_CSV_PATH;
        try {
            // Read all lines from the file, filter out lines starting with the target date
            // and collect the remaining lines
            Path path = Path.of(filePath);
            List<String> lines = Files.lines(path)
                    .filter(line -> !line.startsWith(targetDate))
                    .collect(Collectors.toList());

            // Write the filtered lines back to the file
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public static class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
}
