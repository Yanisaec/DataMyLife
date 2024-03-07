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

public class IngredientsWindow extends JFrame{
    private JTable ingredientsTable;
    private String[][] gradesOfIngredients;

    public IngredientsWindow() {
        gradesOfIngredients = CSVManagerEating.getIngredientsAndGrades();
        
        setTitle("Ingredients");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window on the screen

        // Column names
        String[] ingredientsColumnNames = {"Ingredient", "Grade"};
        DefaultTableModel modelIngredients = new DefaultTableModel(gradesOfIngredients, ingredientsColumnNames);
        // Create the JTable with the DefaultTableModel
        ingredientsTable = new JTable(modelIngredients);
        // Add the table to a scroll pane
        JScrollPane scrollPaneIngredients = new JScrollPane(ingredientsTable);
        scrollPaneIngredients.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        add(scrollPaneIngredients);
        setVisible(true);
    }
}
