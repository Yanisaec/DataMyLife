import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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


public class CSVManagerEating {
    private static List<String> ingredientsList = new ArrayList<>();
    private static List<String> mealsList = new ArrayList<>();
    private static final String INGREDIENTS_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Ingredients.csv";
    private static final String MEALS_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Meals.csv";
    private static final String EATING_CSV_PATH = "C:/Users/boite/Desktop/DataMyLife/Indexes/Eating.csv";
    private static Map<String, String> ingredientMap;

    static {
        ingredientMap = new HashMap<>();
        loadIngredients();
    }

    // Load ingredients and their grades from the CSV file into the map
    private static void loadIngredients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(INGREDIENTS_CSV_PATH))) {
            String line = reader.readLine();
            String[] ingredientsAndGrades = line.split(";");
            for (String ingredientAndGrade : ingredientsAndGrades) {
                String ingredient = ingredientAndGrade.substring(0, ingredientAndGrade.length() - 1);
                char gradeAsChar = ingredientAndGrade.charAt(ingredientAndGrade.length() - 1);
                String grade = Character.toString(gradeAsChar);
                ingredientMap.put(ingredient, grade);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
    }

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

    public static boolean isAnIngredient(String ingredientToTest) {
        try (BufferedReader ingredients = CSVManagerEating.openCSVFile(INGREDIENTS_CSV_PATH)) {
            List<String> data = CSVManagerEating.readCSVFile(ingredients);
            // ingredientsList = new String[data.size()] ;
            for (int i = 0; i < data.size(); i++ ) {
                ingredientsList.add(data.get(i).substring(0, data.get(i).length() - 1));
            }
            return ingredientsList.contains(ingredientToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAMeal(String mealToTest) {
        try (BufferedReader meals = CSVManagerEating.openCSVFile(MEALS_CSV_PATH)) {
            List<String> data = CSVManagerEating.readCSVFile(meals);
            for (int i = 0; i < data.size(); i++ ) {
                mealsList.add(data.get(i));
            }
            return mealsList.contains(mealToTest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addChoice(String newChoice, String grade) {
        String filePath = INGREDIENTS_CSV_PATH;
        String ChoiceGraded = newChoice + grade;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the existing ingredients from the file
            String line = reader.readLine();
            List<String> existingChoices = new ArrayList<>(Arrays.asList(line.split(";")));

            // Check if the new ingredient is already in the list
            if (!existingChoices.contains(ChoiceGraded)) {
                // Add the new ingredient to the list
                existingChoices.add(ChoiceGraded);

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

    // Method to close a CSV file
    public static void closeCSVFile(BufferedReader reader) throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    // Method to check if a string is present in the CSV file
    public static boolean isStringInCSV(String filePath, String searchString) throws IOException {
        try (BufferedReader reader = openCSVFile(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void addToFile(String date, String meal, String ingredient) throws CustomException{
        String filePath = EATING_CSV_PATH;
        if (!date.matches("\\d{8}")) {
            throw new CustomException("The date isn't in the right format.");
        }

        if (!isAnIngredient(ingredient)) {
            throw new CustomException(ingredient + " is not a valid ingredient, first add it to the dictionnary.");
        }

        if (!isAMeal(meal)) {
            throw new CustomException(meal + " is not a valid meal, select one from the list provided.");
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
                    if (!existingLine.contains("{" + meal + ":" + ingredient + "}")) {
                        // Line with the date already exists, append the new tuple
                        lines.set(i, existingLine + ",{" + meal + ":" + ingredient + "}");
                        dateExists = true;
                        break;
                    }
                }
            }

            // If the date doesn't exist, create a new line
            if (!dateExists) {
                String newLine = date + ";{" + meal + ":" + ingredient + "}";
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

    public static List<String[]> getMealsAndIngredients() {
        String filePath = EATING_CSV_PATH;
        List<String[]> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(";");
                ArrayList<String> midiList = new ArrayList<String>();
                ArrayList<String> soirList = new ArrayList<String>();
                ArrayList<String> snackList = new ArrayList<String>();
                ArrayList<String> matinList = new ArrayList<String>();
                if (parts.length > 0) {
                    String date = parts[0];
                    String[] couples = parts[1].split(",");
                    // Iterate over the remaining parts to extract meals and ingredients
                    for (int i = 0; i < couples.length; i++) {
                        String[] mealParts = couples[i].split(":");
                        if (mealParts.length >= 2) {
                            String mealTime = mealParts[0].replaceAll("[{}]","");
                            // System.out.println(mealTime);
                            if (mealTime.equals("matin")) {
                                matinList.add(mealParts[1].replaceAll("[{}]",""));
                            } else if (mealTime.equals("midi")) {
                                midiList.add(mealParts[1].replaceAll("[{}]",""));
                            } else if (mealTime.equals("soir")) {
                                soirList.add(mealParts[1].replaceAll("[{}]",""));
                            } else if (mealTime.equals("snack")) {
                                snackList.add(mealParts[1].replaceAll("[{}]",""));
                            }
                        }
                    }
                    
                    String[] tuple = {date, matinList.toString(), midiList.toString(), soirList.toString(), snackList.toString()};
                    result.add(tuple);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        return result;
    }

    public static String[] getAllElements(String Type) {
        String[] choiceList = new String[0];
        if (Type.equals("Ingredients")){
            try (BufferedReader ingredients = CSVManagerEating.openCSVFile(INGREDIENTS_CSV_PATH)) {
                List<String> data = CSVManagerEating.readCSVFile(ingredients);
                choiceList = new String[data.size()] ;
                for (int i = 0; i < data.size(); i++ ) {
                    choiceList[i] = data.get(i).substring(0, data.get(i).length() - 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (e.g., show an error message)
            }
        } else if (Type.equals("Meals")) {
            try (BufferedReader meals = CSVManagerEating.openCSVFile(MEALS_CSV_PATH)) {
                List<String> data = CSVManagerEating.readCSVFile(meals);
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
    
    public static void deleteLinesWithDate(String targetDate) {
        String filePath = EATING_CSV_PATH;
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

    public static String[][] getOccurencesOfIngredients() {
        Map<String, Integer> ingredientOccurrences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EATING_CSV_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line has a format like "26022024;{midi:pain},{midi:fromage},..."
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    String[] couples = parts[1].replaceAll("[{}]", "").split(",");
                    for (String couple : couples) {
                        String[] mealParts = couple.split(":");
                        if (mealParts.length == 2) {
                            String ingredient = mealParts[1].trim();
                            ingredientOccurrences.put(ingredient, ingredientOccurrences.getOrDefault(ingredient, 0) + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        // Convert the map to a String array
        return convertMapToArray(ingredientOccurrences);
    }

    private static String[][] convertMapToArray(Map<String, Integer> ingredientOccurrences) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(ingredientOccurrences.entrySet());

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

    public static int[] getOccurencesOfGrades() {
        loadIngredients();
        String filePath = EATING_CSV_PATH;
        int[] Occurences = new int[5];
        int grade;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    String[] couples = parts[1].split(",");
                    // Iterate over the remaining parts to extract occurences of ingredients grades
                    for (int i = 0; i < couples.length; i++) {
                        String[] mealParts = couples[i].split(":");
                        if (mealParts.length >= 2) {
                            String ingredient = mealParts[1].replaceAll("[{}]","");
                            String gradeAsString = getGradeForIngredient(ingredient);
                            grade = Integer.parseInt(gradeAsString);
                            Occurences[grade-1] += 1;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
        return Occurences;
    }

    // Method to search for an ingredient and return its grade
    public static String getGradeForIngredient(String ingredient) {
        // System.out.println(ingredient);
        return ingredientMap.getOrDefault(ingredient, "Not Found");
    }

    public static String[][] getAverageMealGrade() {
        Map<String, Integer> mealGradeSum = new HashMap<>();
        Map<String, Integer> mealCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(EATING_CSV_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line has a format like "26022024;{midi:3},{soir:4},..."
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    String[] couples = parts[1].replaceAll("[{}]", "").split(",");
                    for (String couple : couples) {
                        String[] mealParts = couple.split(":");
                        if (mealParts.length == 2) {
                            String mealType = mealParts[0].trim();
                            String ingredient = mealParts[1].trim();

                            int grade = Integer.parseInt(getGradeForIngredient(ingredient));

                            mealGradeSum.put(mealType, mealGradeSum.getOrDefault(mealType, 0) + grade);
                            mealCount.put(mealType, mealCount.getOrDefault(mealType, 0) + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        // Calculate the average grade for each meal type
        return calculateAverageMealGrade(mealGradeSum, mealCount);
    }

    private static String[][] calculateAverageMealGrade(Map<String, Integer> mealGradeSum, Map<String, Integer> mealCount) {
        String[][] result = new String[mealGradeSum.size()][2];
        int index = 0;

        for (Map.Entry<String, Integer> entry : mealGradeSum.entrySet()) {
            String mealType = entry.getKey();
            int totalGrade = entry.getValue();
            int count = mealCount.getOrDefault(mealType, 0);

            // Calculate the average grade (avoid division by zero)
            double averageGrade = (count > 0) ? (double) totalGrade / count : 0.0;

            result[index][0] = mealType;
            result[index][1] = String.format("%.2f", averageGrade); // Format average grade as String
            index++;
        }

        return result;
    }

    public static String[][] getAverageDailyGrade() {
        Map<String, Integer> dailyGradeSum = new HashMap<>();
        Map<String, Integer> dailyMealCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(EATING_CSV_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line has a format like "26022024;{midi:3},{soir:4},..."
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    String date = parts[0].trim();
                    String dateBadFormat = date;
                    DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                    LocalDate datess = LocalDate.parse(dateBadFormat, originalFormatter);
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedString = datess.format(outputFormatter);
                    String[] couples = parts[1].replaceAll("[{}]", "").split(",");
                    for (String couple : couples) {
                        String[] mealParts = couple.split(":");
                        if (mealParts.length == 2) {
                            String ingredient = mealParts[1].trim();
                            int grade = Integer.parseInt(getGradeForIngredient(ingredient));

                            String key = formattedString; // Use a unique key for each day and meal type
                            dailyGradeSum.put(key, dailyGradeSum.getOrDefault(key, 0) + grade);
                            dailyMealCount.put(key, dailyMealCount.getOrDefault(key, 0) + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }

        // Calculate the average grade for each day
        return calculateAverageDailyGrade(dailyGradeSum, dailyMealCount);
    }

    private static String[][] calculateAverageDailyGrade(Map<String, Integer> dailyGradeSum, Map<String, Integer> dailyMealCount) {
        
        String[][] result = new String[dailyGradeSum.size()][2];
        int index = 0;

        for (Map.Entry<String, Integer> entry : dailyGradeSum.entrySet()) {
            String key = entry.getKey();
            int totalGrade = entry.getValue();
            int count = dailyMealCount.getOrDefault(key, 0);

            // Calculate the average grade (avoid division by zero)
            double averageGrade = (count > 0) ? (double) totalGrade / count : 0.0;

            result[index][0] = key; // Use the unique key as the first element of the result
            result[index][1] = String.format("%.2f", averageGrade); // Format average grade as String
            index++;
        }

        Arrays.sort(result, Comparator.comparing((String[] a) -> a[1]).reversed());        
        return result;
    }

    public static float averageGrade(int[] occurences) {
        float average = 0;
        int counter = 1;
        int sum_occ = 0;
        for (int occ:occurences) {
            sum_occ += occ;
            average += occ*counter;
            counter +=1;
        }
        average = average / sum_occ;
        String avgString = String.format("%.2f", average);
        average = Float.parseFloat(avgString.replace(",","."));
        return average;
    }

    public static float empiricalVarianceGrade(int[] occurences, float average) {
        float variance = 0;
        int sum_occ = 0;
        int counter = 1;
        for (int occ:occurences) {
            sum_occ += occ;
            variance += (average - counter)*(average - counter)*occ;
            counter +=1;
        }
        variance = variance / sum_occ;
        String varString = String.format("%.2f", variance);
        variance = Float.parseFloat(varString.replace(",","."));
        return variance;
    }

    public static class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
}
