import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.FocusAdapter;
// import java.awt.event.FocusEvent;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.Comparator;

public class ExercisesWindow extends JFrame {
    private JTable exercisesTable;
    private String[][] exercisesList;

    public ExercisesWindow() {
        
        exercisesList = convertToString2DArray(CSVManagerWorkout.getAllElements("Exercise"));
        Arrays.sort(exercisesList, Comparator.comparing(arr -> arr[0]));
        
        setTitle("Ingredients");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window on the screen

        // Column names
        String[] ingredientsColumnNames = {"Exercise"};
        DefaultTableModel modelIngredients = new DefaultTableModel(exercisesList, ingredientsColumnNames);
        // Create the JTable with the DefaultTableModel
        exercisesTable = new JTable(modelIngredients);
        // Add the table to a scroll pane
        JScrollPane scrollPaneExercises= new JScrollPane(exercisesTable);
        scrollPaneExercises.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        add(scrollPaneExercises);
        setVisible(true);
    }

    public static String[][] convertToString2DArray(String[] array) {
        String[][] result = new String[array.length][1];

        for (int i = 0; i < array.length; i++) {
            result[i][0] = array[i];
        }

        return result;
    }
}
