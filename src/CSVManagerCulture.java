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

public class CSVManagerCulture {
    private static List<String> sourcesList = new ArrayList<>();
    private static List<String> mediaList = new ArrayList<>();
    private static final String MEDIA_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Media.csv";
    private static final String CULTURE_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Culture.csv";
    private static final String SOURCE_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Source.csv";

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
        if (Type.equals("Media")){
            try (BufferedReader ingredients = CSVManagerCulture.openCSVFile(MEDIA_CSV_PATH)) {
                List<String> data = CSVManagerCulture.readCSVFile(ingredients);
                choiceList = new String[data.size()] ;
                for (int i = 0; i < data.size(); i++ ) {
                    choiceList[i] = data.get(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (e.g., show an error message)
            }
        } else if (Type.equals("Source")) {
            try (BufferedReader meals = CSVManagerCulture.openCSVFile(SOURCE_CSV_PATH)) {
                List<String> data = CSVManagerCulture.readCSVFile(meals);
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

    public static void addMedium(String newMedium) {
        String filePath = MEDIA_CSV_PATH;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the existing ingredients from the file
            String line = reader.readLine();
            List<String> existingChoices = new ArrayList<>(Arrays.asList(line.split(";")));

            // Check if the new ingredient is already in the list
            if (!existingChoices.contains(newMedium)) {
                // Add the new ingredient to the list
                existingChoices.add(newMedium);

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

    public static void addSource(String newSource) {
        String filePath = SOURCE_CSV_PATH;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the existing ingredients from the file
            String line = reader.readLine();
            List<String> existingChoices = new ArrayList<>(Arrays.asList(line.split(";")));

            // Check if the new ingredient is already in the list
            if (!existingChoices.contains(newSource)) {
                // Add the new ingredient to the list
                existingChoices.add(newSource);

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

    public static boolean isAMedium(String mediumToTest) {
        try (BufferedReader media = CSVManagerCulture.openCSVFile(MEDIA_CSV_PATH)) {
            List<String> data = CSVManagerCulture.readCSVFile(media);
            for (int i = 0; i < data.size(); i++ ) {
                mediaList.add(data.get(i));
            }
            return mediaList.contains(mediumToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isASource(String sourceToTest) {
        try (BufferedReader sources = CSVManagerCulture.openCSVFile(SOURCE_CSV_PATH)) {
            List<String> data = CSVManagerCulture.readCSVFile(sources);
            for (int i = 0; i < data.size(); i++ ) {
                sourcesList.add(data.get(i));
            }
            return sourcesList.contains(sourceToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addToCulture(String date, String medium, String name, String source) throws CustomException{
        String filePath = CULTURE_CSV_PATH;
        if (!date.matches("\\d{8}")) {
            throw new CustomException("The date isn't in the right format.");
        }

        if (!isAMedium(medium)) {
            throw new CustomException(medium + " is not a valid medium, first add it to the dictionnary.");
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
                    if (!existingLine.contains(date + ";" + medium + ";" + name + ";" + source)) {
                    // Line with the date already exists, do nothing
                    dateExists = true;
                    break;
                    }
                }
            }

            // If the date doesn't exist, create a new line
            if (!dateExists) {
                String newLine = date + ";" + medium + ";" + name + ";" + source;
                lines.add(newLine);
                addSource(source);
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
    
    public static class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }

        public static void deleteLinesWithDate(String targetDate) {
        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(CULTURE_CSV_PATH));

            // Find the line containing the target date
            String lineToRemove = lines.stream()
                    .filter(line -> line.startsWith(targetDate))
                    .findFirst()
                    .orElse(null);

            if (lineToRemove != null) {
                // Extract and remove sources from the line
                String[] lineElements = lineToRemove.split(";");
                String source = lineElements[3];
                deleteSource(source);

                // Remove the line containing the target date from the "Eating.csv" file
                lines.remove(lineToRemove);

                // Write the updated lines back to the file
                Files.write(Paths.get(CULTURE_CSV_PATH), lines);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    private static void deleteSource(String sourceToDelete) {
        try {
            // Read the single line from "Source.csv"
            String sourceLine = Files.readString(Paths.get(SOURCE_CSV_PATH));

            // Remove the specified source from the line
            List<String> sources = List.of(sourceLine.split(";"));
            sources = sources.stream()
                .filter(source -> !source.equals(sourceToDelete))
                .collect(Collectors.toList());
            
            String sourcesString = String.join(";", sources);

            // Write the updated line back to "Source.csv"
            Files.write(Paths.get(SOURCE_CSV_PATH), List.of(sourcesString));

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

    public static List<String[]> getLastMedia() {
        String filePath = CULTURE_CSV_PATH;
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

    public static String[][] getOccurencesOfMedia() {
        Map<String, Integer> mediaOccurences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CULTURE_CSV_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    String medium = parts[1];
                    mediaOccurences.put(medium,mediaOccurences.getOrDefault(medium, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        // Convert the map to a String array
        return convertMapToArray(mediaOccurences);
    }

    private static String[][] convertMapToArray(Map<String, Integer> mediaOccurences) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(mediaOccurences.entrySet());

        // Sort the entries by decreasing occurrences
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Convert the sorted entries to a two-dimensional array
        String[][] result = new String[entryList.size()][2]; // Each entry contributes two elements (ingredient and count)
        int index = 0;

        for (Map.Entry<String, Integer> entry : entryList) {
            result[index][0] = entry.getKey(); // Ingredient name
            result[index][1] = String.valueOf(entry.getValue()); // Occurrence count as String
            index++;
        }

        return result;
    }
}
